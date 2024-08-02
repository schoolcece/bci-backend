package com.hcc.bcitask.mapper;

import com.hcc.common.model.entity.*;
import com.hcc.common.model.vo.RankVO;
import com.hcc.common.model.vo.RecordVo;
import com.hcc.common.model.vo.TaskFinalVO;
import com.hcc.common.model.vo.TaskVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

/**
 * Description: 公共mapper层 任务、日志、计算节点相关数据操作
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@Mapper
public interface CommonMapper {
    List<ComputeNodeDO> selectNodes();

    void insertTask(@Param("taskDO") TaskDO taskDO);

    TaskDO selectTaskById(@Param("taskId") int taskId);

    void deleteTaskById(@Param("taskId") int taskId);

    void updateScoreById(@Param("taskId")int taskId, @Param("score")float score);

    void updateTaskById(@Param("taskDO") TaskDO taskDO);

    void saveLog(@Param("containerLogDO") ContainerLogDO containerLogDO);

    void updateStatusById(@Param("task")TaskDO task);

    void usingNodeByIp(@Param("computeNodeIp")String computeNodeIp);

    void usedNodeByIp(@Param("computeNodeIp")String computeNodeIp);

    List<TaskVO> selectTaskByUserIdAndParadigm(@Param("userId")int userId, @Param("paradigm")int paradigm, @Param("index")int index, @Param("pageSize")int pageSize);

    long selectCount(@Param("userId")int userId, @Param("paradigm")int paradigm);

    int getTotal(@Param("paradigmId")int paradigmId, @Param("dataset")int dataset, @Param("dateLine") Timestamp dateLine);

    List<RankVO> rankByGroup(@Param("paradigmId")int paradigmId, @Param("dataset")int dataset, @Param("index") int index, @Param("pageSize")int rankSize, @Param("dateLine")Timestamp dateLine);

    int recordCount(@Param("teamId")int teamId, @Param("paradigmId")int paradigm, @Param("dataset")int dataset, @Param("dateLine")Timestamp dateLine);

    List<RecordVo> recordByTeam(@Param("teamId")int teamId, @Param("paradigmId")int paradigm, @Param("dataset")int dataset, @Param("index")int index, @Param("pageSize")int pageSize, @Param("dateLine")Timestamp dateLine);

    String selectLogByTaskId(@Param("taskId")int taskId);

    void insertTaskFinal(@Param("taskFinalDO")TaskFinalDO taskFinalDO);

    TaskFinalDO selectTaskFinalById(@Param("taskId") int taskId);

    String selectComputeNodeForFinalsByTeamId(@Param("teamId") int teamId);

    void insertTaskGroupFinal(@Param("taskGroupFinalDO") TaskGroupFinalDO taskGroupFinalDO);

    String selectContainerIdByTaskIdAndGroupId(@Param("taskId") int taskId, @Param("groupid") int groupid);

    TaskGroupFinalDO selectTaskGroupFinalByTaskIdAndGroupId(@Param("taskId") int taskId, @Param("groupid") int groupid);

    void updateTaskFinalById(@Param("taskFinalDO") TaskFinalDO taskFinalDO);

    List<TaskFinalDO> selectTaskFinalByParadigmIdAndStatus(@Param("paradigmId") int paradigmId);

    void updateTaskGroupFinalById(@Param("taskGroupFinalDO") TaskGroupFinalDO taskGroupFinalDO);

    List<TaskFinalVO> selectTaskFinalByUserIdAndParadigm(@Param("userId")int userId, @Param("paradigm")int paradigm, @Param("index")int index, @Param("pageSize")int pageSize);

    long selectCountForFinals(@Param("userId") int userId, @Param("paradigm")int paradigm);
}
