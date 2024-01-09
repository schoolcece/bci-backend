package com.hcc.bcitask.service;

/**
 * Description: service层接口 任务相关操作
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
public interface TaskService {
    void createTask(int paradigmId, int codeId);

    void execTask(int taskId);

    void deleteTaskById(int taskId);

    void updateScoreById(int taskId, float score);
}
