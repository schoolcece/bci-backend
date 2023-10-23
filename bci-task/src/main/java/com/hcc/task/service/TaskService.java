package com.hcc.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.common.utils.R;
import com.hcc.common.vo.UserInfo;
import com.hcc.task.entity.CodeEntity;
import com.hcc.task.entity.RankVo;
import com.hcc.task.entity.RecordVo;
import com.hcc.task.entity.TaskEntity;

import java.util.List;

/**
 * 任务表
 *
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
public interface TaskService extends IService<TaskEntity> {


//    void compute(int codeId, int userId, int teamId, String countKey);

//    void updateCodeStatus(int codeId);

    R getScoresRank(int type, int dataset, int curPage);

    R getScoresRecord(int teamId, int type, int dataset, int curPage);

//    void cancel(int codeId, int userId);

    List<Integer> getTaskIds(int codeId);

//    void handlerCPException(CodeEntity codeEntity, String countKey);
//
//    void handlerLeException(CodeEntity codeEntity, String countKey, int nodeId);
//
//    void handlerRuException(boolean cancel, CodeEntity codeEntity, String countKey, int nodeId, int taskId);

//    void computeOnline(int codeId, UserInfo userInfo);

    void taskInitialization(int codeId, int type, int competition);

    void deleteByUserId(int userId);
}

