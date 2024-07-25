package com.hcc.bcitask.service;

import com.hcc.common.model.R;
import com.hcc.common.model.dto.TaskDTO;
import com.hcc.common.model.vo.RankVO;
import com.hcc.common.model.vo.TaskVO;

import java.util.List;

/**
 * Description: service层接口 任务相关操作
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
public interface TaskService {
    void createTask(int paradigmId, int codeId, String taskName, int taskType);

    void execTask(int taskId);

    void deleteTaskById(int taskId);

    void updateScoreById(int taskId, float score);

    TaskDTO getTask(int paradigm, int curPage);

    R rank(int paradigmId, int dataset, int curPage);

    R record(int teamId, int paradigm, int dataset, int curPage);

    String getLog(int taskId);

    void createTaskForFinals(int paradigmId, int codeId, String taskName, int taskType);

    void confirmTask(int taskId);

    void execTaskForFinals(int taskId);
}
