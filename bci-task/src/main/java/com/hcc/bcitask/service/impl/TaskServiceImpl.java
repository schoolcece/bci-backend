package com.hcc.bcitask.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import com.hcc.bcitask.async.AsyncTask;
import com.hcc.bcitask.feign.CodeFeign;
import com.hcc.bcitask.feign.CompetitionFeign;
import com.hcc.bcitask.feign.AuthFeign;
import com.hcc.bcitask.mapper.CommonMapper;
import com.hcc.bcitask.service.TaskService;
import com.hcc.common.component.RedisComponent;
import com.hcc.common.config.BCIConfig;
import com.hcc.common.constant.CustomConstants;
import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import com.hcc.common.model.R;
import com.hcc.common.model.bo.UserInfoBO;
import com.hcc.common.model.dto.ParadigmDTO;
import com.hcc.common.model.dto.TaskDTO;
import com.hcc.common.model.dto.TaskFinalDTO;
import com.hcc.common.model.entity.ComputeNodeDO;
import com.hcc.common.model.entity.TaskDO;
import com.hcc.common.model.entity.TaskFinalDO;
import com.hcc.common.model.entity.TaskGroupFinalDO;
import com.hcc.common.model.vo.RankVO;
import com.hcc.common.model.vo.RecordVo;
import com.hcc.common.model.vo.TaskFinalVO;
import com.hcc.common.utils.KeyConvertUtils;
import com.hcc.common.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

/**
 * Description: service层具体实现 任务相关操作
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@Service
public class TaskServiceImpl implements TaskService {

    private final Logger logger = LoggerFactory.getLogger("任务服务日志记录");

    @Autowired
    private CodeFeign codeFeign;

    @Autowired
    private CompetitionFeign competitionFeign;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private BCIConfig.TaskConfig taskConfig;

    @Autowired
    private AsyncTask asyncTask;

    @Autowired
    private AuthFeign authFeign;

    @Override
    public void createTask(int paradigmId, int codeId, String taskName, int taskType) {
        UserInfoBO user = UserUtils.getUser();
        //1. 鉴权
        checkPermissions(user, paradigmId);
        //2. 查询范式信息
        ParadigmDTO paradigmInfo = competitionFeign.getInfoByParadigmId(paradigmId);
        //3. 初始化任务
//        //3.1 选择计算节点
//        String nodeIp = getComputeNode();
        //3.1 任务信息入库
        TaskDO taskDO = TaskDO.builder()
                .userId(user.getUserId())
                .teamId(user.getTeamInfoMap().get(paradigmInfo.getEventId()).getTeamId())
                .paradigmId(paradigmId)
                .codeId(codeId)
                .taskType(taskType)
                .dataset(new Date(System.currentTimeMillis()).before(paradigmInfo.getChangeTime()) ? 0 : 1)
//                .computeNodeIp(nodeIp)
                .taskName(taskName)
                .build();
        commonMapper.insertTask(taskDO);
    }

    @Override
    public void execTask(int taskId) {
        UserInfoBO user = UserUtils.getUser();
        //1. 查询任务信息和范式信息
        TaskDO taskDO = commonMapper.selectTaskById(taskId);
        ParadigmDTO paradigmInfo = competitionFeign.getInfoByParadigmId(taskDO.getParadigmId());
        //2. 执行任务是否属于当前用户
        if (taskDO.getUserId() != user.getUserId()) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }
        //3. 提交次数检查
        String countKey = KeyConvertUtils.countKeyConvert(taskDO.getTeamId(), taskDO.getParadigmId());
        String taskingKey = KeyConvertUtils.taskingKeyConvert(taskDO.getTeamId(), taskDO.getParadigmId());
        checkCommitTimes(countKey, taskDO.getParadigmId());
        //4. 检查该用户所在队伍是否有正在运行的任务
        if (!redisComponent.setIfAbsent(taskingKey, 1L, taskConfig.getMaxTime().get(taskDO.getParadigmId())+60, TimeUnit.SECONDS)) {
            throw new RTException(ErrorCodeEnum.HAS_TASK_RUNNING.getCode(), ErrorCodeEnum.HAS_TASK_RUNNING.getMsg());
        }
        //5. 获取代码信息
        String codeUrl = codeFeign.getCodeUrlById(taskDO.getCodeId());
        //6. 尝试获取计算节点建立连接并创建容器
        DockerClient dockerClient;
        CreateContainerResponse container;
        try{
            //6.1 尝试获取计算节点并建立连接
            String computeNodeIp = getComputeNode();
            taskDO.setComputeNodeIp(computeNodeIp);
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("tcp://"+taskDO.getComputeNodeIp()+":2375")
                    .build();
            dockerClient = DockerClientBuilder
                    .getInstance(config)
                    .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                    .build();
            //6.2. 创建容器并完成各项配置
            HostConfig hostConfig = new HostConfig();
            //6.2.1 配置gpu
            setGpu(hostConfig);
            //6.2.2 配置数据文件挂载
            setFileBinds(hostConfig, taskDO.getDataset() == 0?paradigmInfo.getAData():paradigmInfo.getBData(), taskDO.getParadigmId());
            //6.3. 创建容器
            container = dockerClient.createContainerCmd(paradigmInfo.getImage())
                    .withEnv("TASK_ID="+taskDO.getId(), taskConfig.getUpdateScoreURl(), "PARADIGM_NAME="+paradigmInfo.getParadigmName())
                    .withHostConfig(hostConfig)
                    .withCmd("/bin/sh" , "-c", taskConfig.getCmd()).exec();
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
            redisComponent.deleteForLong(taskingKey);
            throw new RTException(ErrorCodeEnum.COMPUTE_RESOURCE_FAILD.getCode(), ErrorCodeEnum.COMPUTE_RESOURCE_FAILD.getMsg());
        }

        //7. 添加赛队算法
        GZIPInputStream gis = null;
        try {
            gis = new GZIPInputStream(new FileInputStream(codeUrl));
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
            redisComponent.deleteForLong(taskingKey);
            throw new RTException(ErrorCodeEnum.CODE_NOT_EXIST.getCode(), ErrorCodeEnum.CODE_NOT_EXIST.getMsg());
        }
        dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(gis).withRemotePath(taskConfig.getCodePath()).exec();
        //8. 获取计算节点资源并将任务提交线程池执行
        commonMapper.usingNodeByIp(taskDO.getComputeNodeIp());
        asyncTask.submitTask(dockerClient, taskDO);
        //9. 任务信息补充并入库
        taskDO.setContainerId(container.getId());
        taskDO.setStatus(CustomConstants.BCITaskStatus.PROCESSING);
        commonMapper.updateTaskById(taskDO);
    }

    @Override
    public void deleteTaskById(int taskId) {
        UserInfoBO user = UserUtils.getUser();
        //1. 检查任务是否属于当前用户
        TaskDO taskDO = commonMapper.selectTaskById(taskId);
        if (taskDO.getUserId() != user.getUserId()) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }
        //2. 逻辑删除任务
        commonMapper.deleteTaskById(taskId);
    }

    @Override
    public void updateScoreById(int taskId, float score) {
        //1. 内部调用密钥校验
//        UserInfoBO user = UserUtils.getUser();
//        if (!user.isAdmin()) {
//            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
//        }
        commonMapper.updateScoreById(taskId, score);
    }

    @Override
    public TaskDTO getTask(int paradigm, int curPage) {
        UserInfoBO user = UserUtils.getUser();
        return TaskDTO.builder()
                .tasks(commonMapper.selectTaskByUserIdAndParadigm(user.getUserId(), paradigm, (curPage-1)*CustomConstants.PageSize.TASK_SIZE, CustomConstants.PageSize.TASK_SIZE))
                .total(commonMapper.selectCount(user.getUserId(), paradigm))
                .build();
    }

    @Override
    public R rank(int paradigmId, int dataset, int curPage) {
        // 1. 获取当前时间
        Calendar cal = Calendar.getInstance();

        // 2. 根据数据集获取成绩截至时间
        cal.add(Calendar.DATE, dataset==0?1:0);

        // 3. 重置时分秒毫秒
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // 4. 截至时间转换
        Date yesterdayMidnight = cal.getTime();
        Timestamp dateLine = new Timestamp(yesterdayMidnight.getTime());

        // 5. 获取成绩总数
        int total = commonMapper.getTotal(paradigmId, dataset, dateLine);

        // 6. 获取当页成绩
        List<RankVO> rank = commonMapper.rankByGroup(paradigmId, dataset, (curPage-1)*CustomConstants.PageSize.RANK_SIZE, CustomConstants.PageSize.RANK_SIZE, dateLine);

        // 7. 队名转换
        rank.forEach(
                rankVo -> {
                    rankVo.setTeamName(authFeign.getTeamName(rankVo.getTeamId()));
                }
        );
        return R.ok().put("record", rank).put("total", total);
    }

    @Override
    public R record(int teamId, int paradigm, int dataset, int curPage) {
        // 1. 获取当前时间
        Calendar cal = Calendar.getInstance();

        // 2. 根据数据集获取成绩截至时间
        cal.add(Calendar.DATE, dataset==0?1:0);

        // 3. 重置时分秒毫秒
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // 4. 截至时间转换
        Date yesterdayMidnight = cal.getTime();
        Timestamp dateLine = new Timestamp(yesterdayMidnight.getTime());

        // 5. 获取成绩记录总数
        int total = commonMapper.recordCount(teamId, paradigm, dataset, dateLine);

        // 5. 获取成绩记录当页数据
        List<RecordVo> recordVo = commonMapper.recordByTeam(teamId, paradigm, dataset, (curPage-1)*CustomConstants.PageSize.RANK_SIZE, CustomConstants.PageSize.RANK_SIZE, dateLine);
        return R.ok().put("total", total).put("record", recordVo);
    }

    @Override
    public String getLog(int taskId) {
        return commonMapper.selectLogByTaskId(taskId);
    }

    @Override
    public void createTaskForFinals(int paradigmId, int codeId, String taskName, int taskType) {
        UserInfoBO user = UserUtils.getUser();
        //1. 鉴权
        checkPermissions(user, paradigmId);
        //2. 查询范式信息
        ParadigmDTO paradigmInfo = competitionFeign.getInfoByParadigmId(paradigmId);
        //3. 任务信息入库
        TaskFinalDO taskFinalDO = TaskFinalDO.builder()
                .userId(user.getUserId())
                .teamId(user.getTeamInfoMap().get(paradigmInfo.getEventId()).getTeamId())
                .codeId(codeId)
                .paradigmId(paradigmId)
                .taskName(taskName)
                .taskType(taskType)
                .status(0)
                .build();
        commonMapper.insertTaskFinal(taskFinalDO);
    }

    @Override
    public void confirmTask(int taskId) {
        UserInfoBO user = UserUtils.getUser();
        //1. 查询任务信息和范式信息
        TaskFinalDO taskFinalDO = commonMapper.selectTaskFinalById(taskId);
        ParadigmDTO paradigmInfo = competitionFeign.getInfoByParadigmId(taskFinalDO.getParadigmId());
        //2. 执行任务是否属于当前用户
        if (taskFinalDO.getUserId() != user.getUserId()) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }
        //3. 获取代码信息
        String codeUrl = codeFeign.getCodeUrlById(taskFinalDO.getCodeId());

        String computeNodeIp = getComputeNodeForFinals(taskFinalDO.getTeamId());
        taskFinalDO.setComputeNodeIp(computeNodeIp);

        //4. 容器组信息入库
        for (int groupid = 1; groupid <= taskConfig.getFinalGroup(); groupid ++) {
            TaskGroupFinalDO taskGroupFinalDO = TaskGroupFinalDO.builder()
                    .taskId(taskFinalDO.getId())
                    .groupId(groupid)
                    .containerName("team_" + user.getTeamInfoMap().get(paradigmInfo.getEventId()).getTeamId() + ".group_" + groupid)
                    .build();

            //5. 获取计算节点并创建三个容器
            DockerClient dockerClient;
            CreateContainerResponse container;
            try {
                DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                        .withDockerHost("tcp://"+taskFinalDO.getComputeNodeIp()+":2375")
                        .build();
                dockerClient = DockerClientBuilder
                        .getInstance(config)
                        .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                        .build();
                HostConfig hostConfig = new HostConfig();
//                setGpu(hostConfig);
                container = dockerClient.createContainerCmd(paradigmInfo.getImage())
                        .withEnv("COMPONENT_ID=" + taskGroupFinalDO.getContainerName(), "TEAM_NAME=" + authFeign.getTeamName(user.getTeamInfoMap().get(paradigmInfo.getEventId()).getTeamId()), "ALGORITHM_NUMBER=" + groupid)
                        .withHostConfig(hostConfig)
                        .withCmd("/bin/sh" , "-c", taskConfig.getCmd()).exec();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
                throw new RTException(ErrorCodeEnum.COMPUTE_RESOURCE_FAILD.getCode(), ErrorCodeEnum.COMPUTE_RESOURCE_FAILD.getMsg());
            }
            GZIPInputStream gis = null;
            try {
                gis = new GZIPInputStream(new FileInputStream(codeUrl));
            }catch (Exception e){
                logger.error(e.getLocalizedMessage());
                throw new RTException(ErrorCodeEnum.CODE_NOT_EXIST.getCode(), ErrorCodeEnum.CODE_NOT_EXIST.getMsg());
            }
            dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(gis).withRemotePath(taskConfig.getCodePath()).exec();
            taskGroupFinalDO.setContainerId(container.getId());
            taskGroupFinalDO.setStatus(CustomConstants.BCITaskStatus.PENDING);
            commonMapper.insertTaskGroupFinal(taskGroupFinalDO);
            taskFinalDO.setStatus(1);
            commonMapper.updateTaskFinalById(taskFinalDO);
        }
    }

    @Override
    public void execTaskForFinals(int taskId) {
        TaskFinalDO taskFinalDO = commonMapper.selectTaskFinalById(taskId);
        if (taskFinalDO.getStatus() != 1) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://"+taskFinalDO.getComputeNodeIp()+":2375")
                .build();
        DockerClient dockerClient = DockerClientBuilder
                .getInstance(config)
                .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                .build();
        for (int groupid = 1; groupid <= taskConfig.getFinalGroup(); groupid ++) {
            TaskGroupFinalDO taskGroupFinalDO = commonMapper.selectTaskGroupFinalByTaskIdAndGroupId(taskId, groupid);
            String containerId = taskGroupFinalDO.getContainerId();
            dockerClient.startContainerCmd(containerId).exec();
            taskGroupFinalDO.setStatus(CustomConstants.BCITaskStatus.PROCESSING);
            commonMapper.updateTaskGroupFinalById(taskGroupFinalDO);
        }
    }

    @Override
    public void execAllTaskForFinals(int paradigmId) {
        List<TaskFinalDO> taskFinalDOS = commonMapper.selectTaskFinalByParadigmIdAndStatus(paradigmId);
        for (TaskFinalDO taskFinalDO : taskFinalDOS) {
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("tcp://"+taskFinalDO.getComputeNodeIp()+":2375")
                    .build();
            DockerClient dockerClient = DockerClientBuilder
                    .getInstance(config)
                    .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                    .build();
            for (int groupid = 1; groupid <= taskConfig.getFinalGroup(); groupid ++) {
                TaskGroupFinalDO taskGroupFinalDO = commonMapper.selectTaskGroupFinalByTaskIdAndGroupId(taskFinalDO.getId(), groupid);
                String containerId = taskGroupFinalDO.getContainerId();
                dockerClient.startContainerCmd(containerId).exec();
                taskGroupFinalDO.setStatus(CustomConstants.BCITaskStatus.PROCESSING);
                commonMapper.updateTaskGroupFinalById(taskGroupFinalDO);
            }
        }
    }

    @Override
    public TaskFinalDTO getTaskForFinals(int paradigm, int curPage) {
        UserInfoBO user = UserUtils.getUser();
        List<TaskFinalVO> taskFinalVOs = commonMapper.selectTaskFinalByUserIdAndParadigm(
                user.getUserId(),
                paradigm,
                (curPage - 1) * CustomConstants.PageSize.TASK_SIZE,
                CustomConstants.PageSize.TASK_SIZE
        );

        for (TaskFinalVO taskFinalVO : taskFinalVOs) {
            TaskFinalDO taskFinalDO = commonMapper.selectTaskFinalById(taskFinalVO.getId());
            taskFinalVO.setMd5(codeFeign.getMd5ById(taskFinalDO.getCodeId()));
        }

        return TaskFinalDTO.builder()
                .taskFinals(taskFinalVOs)
                .total(commonMapper.selectCountForFinals(user.getUserId(), paradigm))
                .build();
    }

    private void checkPermissions(UserInfoBO user, int paradigmId) {
        if (user.isAdmin()) {
            return;
        }
        if (Optional.ofNullable(user.getPermissions()).orElseThrow(() ->
                        new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg()))
                .getOrDefault(paradigmId, 0) != CustomConstants.ApplicationStatus.APPROVED) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }
    }

    private String getComputeNode(){
        List<ComputeNodeDO> nodes = commonMapper.selectNodes()
                .stream()
                .filter(item -> item.getStatus() == 1)
                .filter(item -> item.getRunningTasks()  < item.getMaxTasks())
                .sorted((a, b) -> (b.getMaxTasks()-b.getRunningTasks()) - (a.getMaxTasks()-a.getRunningTasks())).collect(Collectors.toList());
        if(nodes.size() == 0){
           throw new RTException(ErrorCodeEnum.NO_COMPUTE_NODE.getCode(), ErrorCodeEnum.NO_COMPUTE_NODE.getMsg());
        }
        return nodes.get(0).getIp();
    }

    private String getComputeNodeForFinals(int teamId) {
        return commonMapper.selectComputeNodeForFinalsByTeamId(teamId);
    }

    private void setGpu(HostConfig hostConfig){
        List<DeviceRequest> deviceRequests = new ArrayList<>();
        DeviceRequest deviceRequest = new DeviceRequest();
        List<List<String>> capabilities = new ArrayList<>();
        List<String> capability = new ArrayList<>();
        capability.add("gpu");
        capabilities.add(capability);
        deviceRequest.withDriver("").withCount(-1).withCapabilities(capabilities).withOptions(new HashMap<>());
        deviceRequests.add(deviceRequest);
        hostConfig.withDeviceRequests(deviceRequests);
    }

    private void setFileBinds(HostConfig hostConfig, String dataUrl, int paradigmId) {
        hostConfig.withBinds(new Bind(dataUrl,new Volume(taskConfig.getDataPath().get(paradigmId)))
//                , new Bind("/usr/local/cuda", new Volume("/usr/local/cuda"))
//                , new Bind("/usr/local/cuda-11.7", new Volume("/usr/local/cuda-11.7"))
        );
    }

    private void checkCommitTimes(String countKey, int paradigmId) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.MILLISECOND,0);
        //1.2 当前时间与晚上十二点的秒差
        Long timeOut = (calendar.getTimeInMillis()-System.currentTimeMillis()) / 1000;
        redisComponent.setIfAbsent(countKey,0L, timeOut, TimeUnit.SECONDS);
        Long count = redisComponent.getLong(countKey);
        if(count >= taskConfig.getMaxExec().get(paradigmId)){
            throw new RTException(ErrorCodeEnum.COMMIT_OVER_TIMES.getCode(), ErrorCodeEnum.COMMIT_OVER_TIMES.getMsg());
        }
    }

}
