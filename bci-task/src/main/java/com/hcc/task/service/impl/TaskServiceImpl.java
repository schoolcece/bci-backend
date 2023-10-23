package com.hcc.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.utils.R;
import com.hcc.common.utils.StatusEnum;
import com.hcc.common.utils.TokenHolder;
import com.hcc.common.utils.UserUtils;
import com.hcc.common.vo.NodeVo;
import com.hcc.common.vo.UserInfo;
import com.hcc.task.entity.*;
import com.hcc.task.exception.RRException;
import com.hcc.task.feign.CodeFeign;
import com.hcc.task.feign.LogFeign;
import com.hcc.task.feign.SchedulerFeign;
import com.hcc.task.feign.UserFeign;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.hcc.task.mapper.TaskMapper;
import com.hcc.task.service.TaskService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import static java.util.concurrent.TimeUnit.SECONDS;
@Slf4j
@RefreshScope
@Service("taskService")
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskEntity> implements TaskService {

    private CodeFeign codeFeign;

    private TaskMapper taskMapper;

    private SchedulerFeign schedulerFeign;

    private LogFeign logFeign;

    private UserFeign userFeign;

    private ThreadPoolExecutor executor;

    private StringRedisTemplate redisTemplate;
    private RedisTemplate<String, Long> redisTemplateForCount;

    @Value("${image.names}")
    String[] images;

    @Value("${task.names}")
    String[] taskNames;

    @Value("${image.scoreUrl}")
    String scoreUrl;

    @Value("${container.maxTime}")
    int[] maxTime;

    @Value("${container.cmd}")
    String cmd;

    @Value("${container.codePath}")
    String codePath;

    @Value("${map.hostUrls}")
    String[] hostUrls;

    @Value("${map.containerUrls}")
    String[] containerUrls;

    @Value("${online.kafkaIp}")
    String kafkaIp;

    @Value("${online.codePath}")
    String codePathOnline;

    @Value("${dataset}")
    Integer dataset;

    @Autowired
    public TaskServiceImpl (ThreadPoolExecutor executor, CodeFeign codeFeign, TaskMapper taskMapper,
                            SchedulerFeign schedulerFeign, LogFeign logFeign, UserFeign userFeign,
                            StringRedisTemplate redisTemplate, RedisTemplate<String, Long> redisTemplateForCount){
        this.executor = executor;
        this.codeFeign = codeFeign;
        this.taskMapper = taskMapper;
        this.schedulerFeign = schedulerFeign;
        this.logFeign = logFeign;
        this.userFeign = userFeign;
        this.redisTemplate = redisTemplate;
        this.redisTemplateForCount = redisTemplateForCount;
    }

//    @Override
//    public void compute(int codeId, int userId, int teamId, String countKey){
//        //创建本次执行的任务
//        TaskEntity task = new TaskEntity();
//        task.setCreateTime(new Timestamp(new Date().getTime()));
//        task.setStatus(StatusEnume.TASK_STATUS_INIT.getStatus());
//        task.setUserId(userId);
//        task.setTeamId(teamId);
//
//        //更新代码状态，并获取本次执行的代码信息填充到任务 todo: 代码不应该有状态，提交之后应该以任务为核心。
//        CodeEntity codeEntity = codeFeign.updateStatusReturnCode(codeId, StatusEnume.CODE_STATUS_INIT.getStatus());
//        String url = codeEntity.getUrl();
//        Integer type = codeEntity.getType();
//        task.setType(type);
//        task.setImage(images[type]);
//        task.setDataset(dataset);
//
//        //初始化任务
//        // 1. 选择计算节点并建立连接
//        NodeVo node = schedulerFeign.getOne();
//        if(node == null){
//            //状态回滚：代码状态、今日提交次数；抛出异常，交给controller层处理。
//            throw new InitializationException("暂无计算资源，请稍后再试！", codeEntity, countKey);
//        }
//        String ip = node.getNodeIp();
//        DockerClient dockerClient = null;
//        try{
//            //尝试与计算节点建立连接
//            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
//                    .withDockerHost("tcp://"+ip+":2375")
//                    .build();
//            dockerClient = DockerClientBuilder.getInstance(config).build();
//        }catch (Exception e){
//            //与计算节点建立连接失败，状态回滚：代码状态、今日提交次数，抛出异常，交给controller层处理。
//            throw new LoadingException("计算节点"+ip+"不可用,请联系管理员！"+e.getLocalizedMessage(), codeEntity, countKey, node.getNodeId());
//        }
//        task.setComputeNode(ip);
//
//        //将新任务存入数据库并与代码绑定
//        task.setCodeId(codeId);
//        taskMapper.insert(task);
//        codeEntity.setTaskId(task.getTaskId());
//
//        //创建本次执行的容器
//        HostConfig hostConfig = new HostConfig();
//        hostConfig.withBinds(new Bind(hostUrls[type],new Volume(containerUrls[type])));
//        CreateContainerResponse container = null;
//        try{
//            container = dockerClient.createContainerCmd(images[type])
//                    .withEnv("TASK_ID="+task.getTaskId(), scoreUrl)
//                    .withHostConfig(hostConfig)
//                    .withCmd("/bin/sh" , "-c", cmd).exec();}
//        catch (Exception e){
//            throw new LoadingException("创建算法容器失败,请联系管理员！"+e.getLocalizedMessage(), codeEntity, countKey, node.getNodeId());
//        }
//        task.setContainerId(container.getId());
//
//        //添加赛队算法
//        GZIPInputStream gis = null;
//        try {
//            gis = new GZIPInputStream(new FileInputStream(url));
//        }catch (Exception e){
//            //状态更新：代码错误，任务执行错误，状态回滚：计算节点状态，抛出异常，交给controller层处理
//            throw new LoadingException("找不到上传的算法", codeEntity, countKey, node.getNodeId());
//        }
//        dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(gis).withRemotePath(codePath).exec();
//
//        DockerClient finalDockerClient = dockerClient;
//
//        //新任务初始化完成， 更新任务和代码状态， 并开始异步运行任务
//        task.setStatus(StatusEnume.TASK_STATUS_RUNNING.getStatus());
//        codeEntity.setStatus(StatusEnume.CODE_STATUS_RUNNING.getStatus());
//        codeFeign.updateCode(codeEntity);
//
//        //将主线程的token更新到子线程中
//        ServletRequestAttributes requestAttributes =
//                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        TokenHolder.saveToken(requestAttributes.getRequest().getHeader("token"));
//
//        CompletableFuture.runAsync(()->{
//            submitTask(finalDockerClient, task, codeEntity, node.getNodeId());
//        }, executor);
//    }

//    @Override
//    public void updateCodeStatus(int codeId) {
//        CodeEntity code = new CodeEntity();
//        code.setStatus(StatusEnume.CODE_STATUS_FILED.getStatus());
//        code.setCodeId(codeId);
//        codeFeign.updateCode(code);
//
//        TaskEntity task = new TaskEntity();
//        task.setCodeId(codeId);
//        task.setStatus(StatusEnume.TASK_STATUS_FILED.getStatus());
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("code_id",codeId);
//        taskMapper.update(task,queryWrapper);
//    }

    @Override
    public R getScoresRank(int type, int dataset, int curPage) {
        // 获取当前时间
        Calendar cal = Calendar.getInstance();

        // 将时间设置为今天
        cal.add(Calendar.DATE, dataset==0?1:0);

        // 重置时分秒毫秒
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // 转换为 Date 对象
        Date yesterdayMidnight = cal.getTime();
        Timestamp dateLine = new Timestamp(yesterdayMidnight.getTime());
        int total = taskMapper.getTotal(type, dataset, dateLine);

        List<RankVo> rank = taskMapper.rankByGroup(type, dataset, (curPage-1)*10, 10, dateLine);
//        List<TaskEntity> taskEntities = taskMapper.selectList(new QueryWrapper<TaskEntity>()
//                .eq("type",type)
//                .eq("status",StatusEnume.TASK_STATUS_COMPLETED.getStatus())
//                .eq("dataset", dataset)
//                .isNotNull("score"));
//        List<RankVo> rank = new ArrayList<>();
//        taskEntities.stream()
//                .collect(Collectors.groupingBy(TaskEntity::getTeamId, Collectors.maxBy((a, b) -> a.getScore().compareTo(b.getScore()))))
//                .forEach((teamId,score)->{
//                    RankVo rankVo = new RankVo();
//                    rankVo.setTeamId(teamId);
//                    rankVo.setTeamName(userFeign.getTeamName(teamId));
//                    rankVo.setScore(score.get().getScore());
//                    rank.add(rankVo);
//                });
//        rank.sort((a, b)->b.getScore().compareTo(a.getScore()));
        rank.forEach(
                rankVo -> {
                    rankVo.setTeamName(userFeign.getTeamName(rankVo.getTeamId()));
                }
        );
        return R.ok().put("total", total).put("rank", rank);
    }

    @Override
    public R getScoresRecord(int teamId, int type, int dataset, int curPage) {
        // 获取当前时间
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, dataset==0?1:0);
        // 重置时分秒毫秒
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        // 转换为 Date 对象
        Date yesterdayMidnight = cal.getTime();
        Timestamp dateLine = new Timestamp(yesterdayMidnight.getTime());
        int total = taskMapper.recordCount(teamId, type, dataset, dateLine);

        List<RecordVo> recordVo = taskMapper.recordByTeam(teamId, type, dataset, (curPage-1)*10, 10, dateLine);
//        QueryWrapper<TaskEntity> wrapper = new QueryWrapper<>();
//        wrapper.eq("team_id",teamId)
//                .eq("type",type)
//                .eq("status",StatusEnume.TASK_STATUS_COMPLETED.getStatus())
//                .eq("dataset", dataset)
//                .isNotNull("score");
//        List<RecordVo> collect = taskMapper.selectList(wrapper).stream()
//                .sorted((a, b) -> a.getCreateTime().compareTo(b.getCreateTime()))
//                .map(task -> new RecordVo(task.getCreateTime(),task.getScore())).collect(Collectors.toList());
        return R.ok().put("total", total).put("record", recordVo);
    }

//    @Override
//    public void cancel(int codeId, int userId) {
//        List<TaskEntity> tasks = taskMapper.selectList(new QueryWrapper<TaskEntity>()
//                        .eq("code_id", codeId))
//                .stream()
//                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
//                .collect(Collectors.toList());
//        if(tasks.size() == 0){
//            throw new RRException("无法取消，稍后重试");
//        }
//        TaskEntity task = tasks.get(0);
//        taskMapper.updateById(task);
//        CodeEntity codeEntity = new CodeEntity();
//        codeEntity.setCodeId(codeId);
//        codeEntity.setStatus(StatusEnume.CODE_STATUS_SUBMITTED.getStatus());
//        codeFeign.updateCode(codeEntity);
//    }

    @Override
    public List<Integer> getTaskIds(int codeId) {
        List<Integer> codeIds = taskMapper.selectList(new QueryWrapper<TaskEntity>().eq("code_id", codeId))
                .stream().map(task -> task.getTaskId()).collect(Collectors.toList());
        return codeIds;
    }

//    @Override
//    public void handlerCPException(CodeEntity codeEntity, String countKey) {
//        codeEntity.setStatus(StatusEnume.CODE_STATUS_SUBMITTED.getStatus());
//        codeFeign.updateCode(codeEntity);
//        redisTemplateForCount.opsForValue().decrement(countKey);
//    }

//    @Override
//    public void handlerLeException(CodeEntity codeEntity, String countKey, int nodeId) {
//        codeEntity.setStatus(StatusEnume.CODE_STATUS_FILED.getStatus());
//        codeFeign.updateCode(codeEntity);
//        redisTemplateForCount.opsForValue().decrement(countKey);
//        schedulerFeign.updateNode(nodeId);
//    }

//    @Override
//    public void handlerRuException(boolean cancel, CodeEntity codeEntity, String countKey, int nodeId, int taskId) {
//        if(cancel){
//            redisTemplateForCount.opsForValue().decrement(countKey);
//            schedulerFeign.updateNode(nodeId);
//        }else{
//            codeEntity.setStatus(StatusEnume.CODE_STATUS_COMPLETED.getStatus());
//            codeFeign.updateCode(codeEntity);
//            schedulerFeign.updateNode(nodeId);
//            LogEntity log = new LogEntity();
//            log.setTaskId(taskId);
//            log.setContent("运行超时！！！，最大运行时长：" + maxTime + 's');
//            logFeign.saveLog(log);
//        }
//    }

//    @Override
//    public void computeOnline(int codeId, UserInfo userInfo) {
//        CodeEntity codeEntity = codeFeign.updateStatusReturnCode(codeId, StatusEnume.CODE_STATUS_RUNNING.getStatus());
//        String url = codeEntity.getUrl();
//
//        //创建本次执行的任务
//        TaskEntity task = new TaskEntity();
//        task.setCreateTime(new Timestamp(new Date().getTime()));
//        task.setStatus(StatusEnume.TASK_STATUS_INIT.getStatus());
//        task.setUserId(userInfo.getUserId());
//        task.setCodeId(codeId);
//        taskMapper.insert(task);
//
//        //选择计算节点并建立连接
//        NodeVo node = schedulerFeign.getOne();
//        if(node == null){
//            //状态回滚：代码状态、今日提交次数，抛出异常，交给controller层处理。
//            throw new RRException("暂无计算资源，请稍后再试！");
//        }
//        String ip = node.getNodeIp();
//        DockerClient dockerClient = null;
//        try{
//            //尝试与计算节点建立连接
//            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
//                    .withDockerHost("tcp://"+ip+":2375")
//                    .build();
//            dockerClient = DockerClientBuilder.getInstance(config).build();
//        }catch (Exception e){
//            //与计算节点建立连接失败，状态回滚：代码状态、今日提交次数，抛出异常，交给controller层处理。
//            throw new RRException("计算节点"+ip+"不可用,请联系管理员！");
//        }
//        task.setComputeNode(ip);
//
//        //创建本次执行的容器
//        String cmd = "python AlgorithmSystemMain.py";
//        HostConfig hostConfig = new HostConfig();
//        hostConfig.withExtraHosts(new String[]{"server:"+kafkaIp});
//        CreateContainerResponse container = dockerClient.createContainerCmd("bci-ssveponline:v1.0")
//                .withEnv("algId="+userInfo.getUsername())
//                .withHostConfig(hostConfig)
//                .withCmd("/bin/sh" , "-c", cmd).exec();
//
//        //添加赛队算法
//        GZIPInputStream gis = null;
//        try {
//            gis = new GZIPInputStream(new FileInputStream(url));
//        }catch (Exception e){
//            throw new RRException("找不到上传的算法");
//        }
//        dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(gis).withRemotePath(codePathOnline).exec();
//
//        dockerClient.startContainerCmd(container.getId()).exec();
//    }

    @GlobalTransactional
    @Override
    public void taskInitialization(int codeId, int type, int competition) {
        UserInfo userInfo = UserUtils.getUser();
        //1.3 获取代码信息
        CodeEntity code = codeFeign.getCodeById(codeId);
        if (Objects.isNull(code) || !code.getUserId().equals(userInfo.getUserId())){
            throw new RRException(BizCodeEnum.NO_PERMISSION.getCode(), BizCodeEnum.NO_PERMISSION.getMsg());
        }
        code.setStatus(StatusEnum.CODE_STATUS_INIT.getStatus());


        //2. 初始化任务
        //2.1 选择计算节点并建立连接
        NodeVo node = schedulerFeign.getOne();
        if(Objects.isNull(node)){
            //状态回滚：代码状态、今日提交次数；抛出异常，交给controller层处理。(用事务 不用手动回滚啦)
            throw new RRException(BizCodeEnum.NO_COMPUTE_RESOURCES.getCode(), BizCodeEnum.NO_COMPUTE_RESOURCES.getMsg());
        }
        String ip = node.getNodeIp();
        DockerClient dockerClient;
        try{
            //尝试与计算节点建立连接
            DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("tcp://"+ip+":2375")
                    .build();
            dockerClient = DockerClientBuilder.getInstance(config).build();
        }catch (Exception e){
            //与计算节点建立连接失败，状态回滚：代码状态、今日提交次数，抛出异常，交给controller层处理。
            //throw new LoadingException("计算节点"+ip+"不可用,请联系管理员！"+e.getLocalizedMessage(), codeEntity, countKey, node.getNodeId());
            throw new RRException(BizCodeEnum.COMPUTE_RESOURCE_FAILD.getCode(), "计算节点"+ip+"不可用,请联系管理员！");
        }

        //3. 任务初始化信息入库
        TaskEntity task = new TaskEntity();
        task.setUserId(userInfo.getUserId());
        task.setTeamId(userInfo.getTeamId()[competition]);
        task.setImage(images[type]);
        task.setType(type);
        task.setDataset(dataset);
        task.setCodeId(codeId);
        task.setStatus(StatusEnum.TASK_STATUS_INIT.getStatus());
        task.setCreateTime(new Timestamp(new Date().getTime()));
        task.setComputeNodeId(node.getNodeId());
        taskMapper.insert(task);

        //4. 创建本次执行的容器
        HostConfig hostConfig = getHostConfig();
//        HostConfig hostConfig = new HostConfig();
        hostConfig.withBinds(new Bind(hostUrls[type],new Volume(containerUrls[type])),
                             new Bind("/usr/local/cuda", new Volume("/usr/local/cuda")),
                             new Bind("/usr/local/cuda-11.7", new Volume("/usr/local/cuda-11.7"))
        );
        CreateContainerResponse container = dockerClient.createContainerCmd(images[type])
                .withEnv("TASK_ID="+task.getTaskId(), scoreUrl, "TASKNAME="+taskNames[type])
                .withHostConfig(hostConfig)
                .withCmd("/bin/sh" , "-c", cmd).exec();;


        //5. 添加赛队算法
        GZIPInputStream gis = null;
        try {
            gis = new GZIPInputStream(new FileInputStream(code.getUrl()));
        }catch (Exception e){
            //状态更新：代码错误，任务执行错误，状态回滚：计算节点状态，抛出异常，交给controller层处理
            throw new RRException(BizCodeEnum.CODE_NOT_EXIST.getCode(), BizCodeEnum.CODE_NOT_EXIST.getMsg());
        }
        dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(gis).withRemotePath(codePath).exec();

        //6. 初始化完成， 开始异步运行任务, 并更新任务和代码状态
        //6.1 将主线程的token更新到子线程中
        task.setContainerId(container.getId());
        task.setStatus(StatusEnum.TASK_STATUS_RUNNING.getStatus());
        taskMapper.updateById(task);
        code.setTaskId(task.getTaskId());
        code.setStatus(StatusEnum.CODE_STATUS_RUNNING.getStatus());
        codeFeign.updateCode(code);

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");

        CompletableFuture.runAsync(()->{
            submitTask(dockerClient, task, code, token);
        }, executor);
    }

    @Override
    public void deleteByUserId(int userId) {
        taskMapper.deleteByUserId(userId);
    }

    private void submitTask(DockerClient dockerClient, TaskEntity task, CodeEntity code, String token){
//        TokenHolder.saveToken(token);
        LogEntity logEntity = new LogEntity();
        logEntity.setTaskId(task.getTaskId());
        StringBuilder sb = new StringBuilder();
        String taskingKey = "teamId" + task.getTeamId() + "type" + task.getType() + "on";
        String countKey = "teamId"+ task.getTeamId() + "type" + task.getType();
        //1. 运行算法并等待运行结果
        try{
            dockerClient.startContainerCmd(task.getContainerId()).exec();
            dockerClient.waitContainerCmd(task.getContainerId())
                    .exec(new WaitContainerResultCallback())
                    .awaitStatusCode(maxTime[task.getType()], SECONDS);
        }catch (Exception e){
            // 容器非正常结束运行
            try {
                dockerClient.stopContainerCmd(task.getContainerId()).exec();
            }catch (Exception ee){
                log.debug("容器{}已经停止!",task.getContainerId());
            }
            sb.append(e.getLocalizedMessage());
            logEntity.setContent(sb.toString());
            try{
                //记录容器非正常停止原因
                logFeign.saveLog(logEntity);
            }catch (Exception eee){
                //记录容器非正常停止原因失败， 通过日志记录
                log.error("任务id{}, 容器非正常停止, 原因：{}", task.getTaskId(), e.getLocalizedMessage(), eee.getLocalizedMessage());
            }
            //容器非正常停止， 更新任务和代码状态为失败
            task.setStatus(StatusEnum.TASK_STATUS_FILED.getStatus());
            try {
                taskMapper.updateById(task);
            }catch (Exception eeee){
                log.error("任务id{}, 更正状态为失败", task.getTaskId());
            }
            code.setStatus(StatusEnum.CODE_STATUS_FILED.getStatus());
            try{
                codeFeign.updateCode(code);
            }catch (Exception eeeee){
                log.error("代码id{}, 更正状态为失败", code.getCodeId(), eeeee.getLocalizedMessage());
            }
            try{
                schedulerFeign.updateNode(task.getComputeNodeId());
            }catch (Exception eeeeee){
                log.error("nodeId{}, 释放节点资源失败", task.getComputeNodeId());
            }
            //关闭该次任务资源
            try {
                dockerClient.close();
            } catch (IOException ee) {
                log.error("该次提交已完成！dockerclient关闭异常，请联系管理员！{}", ee.getLocalizedMessage().toString());
            }
            redisTemplate.delete(taskingKey);
//            TokenHolder.removeToken();
            return;
        }

        //记录运行日志
        try {
            dockerClient.logContainerCmd(task.getContainerId())
                    .withTimestamps(false)
                    .withStdOut(false)
                    .withStdErr(true).exec(new LogContainerResultCallback() {
                        @Override
                        public void onNext(Frame item) {
                            if (item.toString().contains("运行成功")){
                                long expire = redisTemplateForCount.getExpire(countKey, SECONDS);
                                if (expire>=0){
                                    redisTemplateForCount.opsForValue().increment(countKey);
                                }
                            }
                            sb.append(item.toString()
                                    .replaceFirst("STDERR:","\n"));
                        }
                    }).awaitCompletion();
        } catch (Exception e) {
            //todo: 获取日志失败的处理？？？
            sb.append("获取容器日志失败，请联系管理员");
            sb.append(e.getLocalizedMessage());
            logEntity.setContent(sb.toString());
            try{
                logFeign.saveLog(logEntity);
            }catch (Exception ee){
                log.error("任务id{}, 容器正常运行结束,获取日志失败, 原因：{}", task.getTaskId(), e.getLocalizedMessage());
            }
            //更新代码和任务状态
            task.setStatus(StatusEnum.TASK_STATUS_FILED.getStatus());
            try {
                taskMapper.updateById(task);
            }catch (Exception eee){
                log.error("任务id{}, 更新状态为失败", task.getTaskId());
            }
            code.setStatus(StatusEnum.CODE_STATUS_FILED.getStatus());
            try{
                codeFeign.updateCode(code);
            }catch (Exception eeee){
                log.error("代码id{}, 更新状态为失败", code.getCodeId());
            }
            try{
                schedulerFeign.updateNode(task.getComputeNodeId());
            }catch (Exception eeeee){
                log.error("nodeId{}, 释放节点资源失败", task.getComputeNodeId());
            }
            //关闭该次任务资源
            try {
                dockerClient.close();
            } catch (IOException ee) {
                log.error("该次提交已完成！dockerclient关闭异常，请联系管理员！{}", ee.getLocalizedMessage().toString());
            }
            redisTemplate.delete(taskingKey);
//            TokenHolder.removeToken();
            return;
        }

        //任务正常结束
        logEntity.setContent(sb.toString());
        try{
            logFeign.saveLog(logEntity);
        }catch (Exception e){
            log.error("任务id{}, 容器正常运行结束,存储日志失败, 日志：{}", task.getTaskId(),  sb.toString());
        }

        task.setStatus(StatusEnum.TASK_STATUS_COMPLETED.getStatus());
        try {
            taskMapper.updateById(task);
        }catch (Exception e){
            log.error("任务id{}, 更新状态为完成", task.getTaskId());
        }
        code.setStatus(StatusEnum.CODE_STATUS_COMPLETED.getStatus());
        try{
            codeFeign.updateCode(code);
        }catch (Exception e){
            log.error("代码id{}, 更新状态为完成", code.getCodeId());
        }

        try{
            schedulerFeign.updateNode(task.getComputeNodeId());
        }catch (Exception e){
            log.error("nodeId{}, 释放节点资源失败", task.getComputeNodeId());
        }

        //关闭该次任务资源
        try {
            dockerClient.close();
        } catch (IOException e) {
            log.error("该次提交已完成！dockerclient关闭异常，请联系管理员！{}", e.getLocalizedMessage().toString());
        }
        redisTemplate.delete(taskingKey);
//        TokenHolder.removeToken();
    }

    private HostConfig getHostConfig(){
        HostConfig hostConfig = new HostConfig();
        List<DeviceRequest> deviceRequests = new ArrayList<>();
        DeviceRequest deviceRequest = new DeviceRequest();
        List<List<String>> capabilities = new ArrayList<>();
        List<String> capability = new ArrayList<>();
        capability.add("gpu");
        capabilities.add(capability);
        deviceRequest.withDriver("").withCount(-1).withCapabilities(capabilities).withOptions(new HashMap<>());
        deviceRequests.add(deviceRequest);
        hostConfig.withDeviceRequests(deviceRequests);

        return hostConfig;
    }


}