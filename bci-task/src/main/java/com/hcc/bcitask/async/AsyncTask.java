package com.hcc.bcitask.async;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.hcc.bcitask.mapper.CommonMapper;
import com.hcc.common.component.RedisComponent;
import com.hcc.common.config.BCIConfig;
import com.hcc.common.constant.CustomConstants;
import com.hcc.common.model.entity.ContainerLogDO;
import com.hcc.common.model.entity.TaskDO;
import com.hcc.common.utils.KeyConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Description: 异步任务类
 *
 * @Author: hcc
 * @Date: 2024/1/20
 */
@Component
public class AsyncTask {

    private final Logger logger = LoggerFactory.getLogger("算法任务异步执行日志记录");

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private BCIConfig.TaskConfig taskConfig;

    @Async("alg_task_executor")
    public void submitTask(DockerClient dockerClient, TaskDO task){
        ContainerLogDO containerLogDO = new ContainerLogDO();
        containerLogDO.setTaskId(task.getId());
        StringBuilder logContent = new StringBuilder();
        String countKey = KeyConvertUtils.countKeyConvert(task.getTeamId(), task.getParadigmId());
        String taskingKey = KeyConvertUtils.taskingKeyConvert(task.getTeamId(), task.getParadigmId());
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
