package com.hcc.task.mapper;

import com.hcc.task.entity.RankVo;
import com.hcc.task.entity.RecordVo;
import com.hcc.task.entity.TaskEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

/**
 * 任务表
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
@Mapper
public interface TaskMapper extends BaseMapper<TaskEntity> {

    List<RankVo> rankByGroup(@Param("type") int type, @Param("dataset") int dataset, @Param("index") int index, @Param("pageNum") int pageNum, @Param("dateLine")Timestamp dateLine);

    List<RecordVo> recordByTeam(@Param("teamId")int teamId, @Param("type")int type, @Param("dataset")int dataset, @Param("index") int index, @Param("pageNum") int pageNum, @Param("dateLine")Timestamp dateLine);

    int recordCount(@Param("teamId")int teamId, @Param("type")int type, @Param("dataset")int dataset, @Param("dateLine")Timestamp dateLine);

    int getTotal(@Param("type")int type, @Param("dataset")int dataset, @Param("dateLine")Timestamp dateLine);

    void deleteByUserId(@Param("userId") int userId);
}
