package com.hcc.bcitask.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import com.hcc.bcitask.feign.CodeFeign;
import com.hcc.bcitask.feign.CompetitionFeign;
import com.hcc.bcitask.mapper.CommonMapper;
import com.hcc.bcitask.service.TaskService;
import com.hcc.common.component.RedisComponent;
import com.hcc.common.config.BCIConfig;
import com.hcc.common.constant.CustomConstants;
import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import com.hcc.common.model.bo.UserInfoBO;
import com.hcc.common.model.dto.ParadigmDTO;
import com.hcc.common.model.dto.TaskDTO;
import com.hcc.common.model.entity.ComputeNodeDO;
import com.hcc.common.model.entity.ContainerLogDO;
import com.hcc.common.model.entity.TaskDO;
import com.hcc.common.model.vo.TaskVO;
import com.hcc.common.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static java.util.concurrent.TimeUnit.SECONDS;

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

    @Override
    public void createTask(int paradigmId, int codeId, String taskName) {
        UserInfoBO user = UserUtils.getUser();
        //1. 鉴权
        checkPermissions(user, paradigmId);
        //2. 查询范式信息
        ParadigmDTO paradigmInfo = competitionFeign.getInfoByParadigmId(paradigmId);
        //3. 初始化任务
        //3.1 选择计算节点
        String nodeIp = getComputeNode();
        //3.2 任务信息入库
        TaskDO taskDO = TaskDO.builder()
                .userId(user.getUserId())
                .teamId(user.getTeamInfoMap().get(paradigmInfo.getEventId()).getTeamId())
                .paradigmId(paradigmId)
                .codeId(codeId)
                .dataset(new Date(System.currentTimeMillis()).before(paradigmInfo.getChangeTime()) ? 0 : 1)
                .computeNodeIp(nodeIp)
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
        String countKey = "teamId"+ taskDO.getTeamId() + "paradigm" + taskDO.getParadigmId();
        String taskingKey = countKey + "on";
        checkCommitTimes(countKey, taskDO.getParadigmId());
        //4. 检查该用户所在队伍是否有正在运行的任务
        if (!redisComponent.setIfAbsent(taskingKey, 1L, taskConfig.getMaxTime().get(taskDO.getParadigmId())+60, TimeUnit.SECONDS)) {
            throw new RTException(ErrorCodeEnum.HAS_TASK_RUNNING.getCode(), ErrorCodeEnum.HAS_TASK_RUNNING.getMsg());
        }
        //5. 获取代码信息
        String codeUrl = codeFeign.getCodeUrlById(taskDO.getCodeId());
        //6. 尝试与计算节点建立连接
        DockerClient dockerClient;
        try{
            //尝试与计算节点建立连接
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("tcp://"+taskDO.getComputeNodeIp()+":2375")
                    .build();
            dockerClient = DockerClientBuilder
                    .getInstance(config)
                    .withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
                    .build();
        }catch (Exception e){
            throw new RTException(ErrorCodeEnum.COMPUTE_RESOURCE_FAILD.getCode(), ErrorCodeEnum.COMPUTE_RESOURCE_FAILD.getMsg());
        }
        //7. 创建容器并完成各项配置
        HostConfig hostConfig = new HostConfig();
        //7.1 配置gpu
//        setGpu(hostConfig);
        //7.2 配置文件挂载
//        setFileBinds(hostConfig, taskDO.getDataset() == 0?paradigmInfo.getAData():paradigmInfo.getBData());

        CreateContainerResponse container = dockerClient.createContainerCmd(paradigmInfo.getImage())
                .withEnv("TASK_ID="+taskDO.getId(), "URL="+taskConfig.getUpdateScoreURl(), "PARADIGM_NAME="+paradigmInfo.getParadigmName())
                .withHostConfig(hostConfig)
                .withCmd("/bin/sh" , "-c", taskConfig.getCmd()).exec();
        //8. 添加赛队算法
        GZIPInputStream gis = null;
        try {
            gis = new GZIPInputStream(new FileInputStream(codeUrl));
        }catch (Exception e){
            throw new RTException(ErrorCodeEnum.CODE_NOT_EXIST.getCode(), ErrorCodeEnum.CODE_NOT_EXIST.getMsg());
        }
        dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(gis).withRemotePath(taskConfig.getCodePath()).exec();
        //9. 获取计算节点资源并将任务提交线程池执行
        commonMapper.usingNodeByIp(taskDO.getComputeNodeIp());
        submitTask(dockerClient, taskDO);
        //10. 任务信息补充并入库
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
        UserInfoBO user = UserUtils.getUser();
        if (!user.isAdmin()) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }
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

    private void setFileBinds(HostConfig hostConfig, String dataUrl) {
        hostConfig.withBinds(new Bind(dataUrl,new Volume(taskConfig.getDataPath())),
                new Bind("/usr/local/cuda", new Volume("/usr/local/cuda")),
                new Bind("/usr/local/cuda-11.7", new Volume("/usr/local/cuda-11.7"))
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

    @Async
    public void submitTask(DockerClient dockerClient, TaskDO task){
        ContainerLogDO containerLogDO = new ContainerLogDO();
        containerLogDO.setTaskId(task.getId());
        StringBuilder logContent = new StringBuilder();
        String countKey = "teamId"+ task.getTeamId() + "type" + task.getParadigmId();
        String taskingKey = countKey + "on";
        //1. 运行算法并等待运行结果
        try{
            dockerClient.startContainerCmd(task.getContainerId()).exec();
            dockerClient.waitContainerCmd(task.getContainerId())
                    .exec(new WaitContainerResultCallback())
                    .awaitStatusCode(taskConfig.getMaxTime().get(task.getParadigmId()), SECONDS);
        }catch (Exception e){
            logger.error("任务异常中断，原因：{}", e.getLocalizedMessage());
            // 容器执行超时, 手动停止任务
            try {
                dockerClient.stopContainerCmd(task.getContainerId()).exec();
            }catch (Exception ee){
                logger.debug("容器{}已经停止!", task.getContainerId());
            }
            logContent.append("算法运行超时！！！");
            containerLogDO.setContent(logContent.toString());
            clearAfterTask(CustomConstants.BCITaskStatus.FILED, containerLogDO, taskingKey, task, dockerClient);
            return;
        }

        //2. 记录运行日志
        try {
            dockerClient.logContainerCmd(task.getContainerId())
                    .withTimestamps(false)
                    .withStdOut(false)
                    .withStdErr(true).exec(new LogContainerResultCallback() {
                        @Override
                        public void onNext(Frame item) {
                            if (item.toString().contains("运行成功")){
                                long expire = redisComponent.getExpireForLong(countKey, SECONDS);
                                if (expire>=0){
                                    redisComponent.increment(countKey);
                                }
                            }
                            logContent.append(item.toString()
                                    .replaceFirst("STDERR:","\n"));
                        }
                    }).awaitCompletion();
        } catch (Exception e) {
            logger.error("记录容器日志过程失败, 原因: {}", e.getLocalizedMessage());
            logContent.append("任务运行成功, 获取容器日志失败, 请联系管理员!");
            containerLogDO.setContent(logContent.toString());
            clearAfterTask(CustomConstants.BCITaskStatus.SUCCESS, containerLogDO, taskingKey, task, dockerClient);
            return;
        }

        //3. 任务正常结束
        containerLogDO.setContent(logContent.toString());
        clearAfterTask(CustomConstants.BCITaskStatus.SUCCESS, containerLogDO, taskingKey, task, dockerClient);
    }

    private void clearAfterTask(int taskStatus, ContainerLogDO containerLogDO, String taskingKey, TaskDO task, DockerClient dockerClient) {
        //记录容器日志
        try{
            commonMapper.saveLog(containerLogDO);
        }catch (Exception ee){
            //系统存储日志失败
            logger.error("日志存储失败， 原因： {}", ee.getLocalizedMessage());
            //记录容器非正常停止原因失败， 通过日志记录
            logger.error("任务id{}, 容器非正常停止, 原因：{}", task.getId(), ee.getLocalizedMessage());
        }
        //更新任务状态
        task.setStatus(taskStatus);
        try {
            commonMapper.updateStatusById(task);
        }catch (Exception ee){
            logger.error("更新任务id{}状态为失败的操作失败, 原因： {}", task.getId(), ee.getLocalizedMessage());
        }
        //释放节点资源
        try{
            commonMapper.usedNodeByIp(task.getComputeNodeIp());
        }catch (Exception ee){
            logger.error("释放节点{}的资源失败, 原因: {}", task.getComputeNodeIp(), ee.getLocalizedMessage());
        }
        //关闭该次任务资源
        try {
            dockerClient.close();
        } catch (IOException ee) {
            logger.error("释放dockerClient资源异常, 原因：{}", ee.getLocalizedMessage());
        }
        //移除队伍正运行任务的标志
        try {
            redisComponent.deleteForLong(taskingKey);
        } catch (Exception ee) {
            logger.error("移除队伍正运行任务的标志失败, 原因：{}", ee.getLocalizedMessage());
        }
    }
}
