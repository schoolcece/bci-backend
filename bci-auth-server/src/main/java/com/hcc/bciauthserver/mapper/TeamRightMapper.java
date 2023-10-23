package com.hcc.bciauthserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.bciauthserver.entity.TeamEntity;
import com.hcc.bciauthserver.entity.TeamRightEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface TeamRightMapper extends BaseMapper<TeamRightEntity> {

    List<Integer> selectRightsByTeamId(Integer teamId);

    List<Integer> selectUndoRightsByTeamId(Integer teamId);

    void deleteByTeamId(Integer teamId);

    void updateStatusByTeamIdAndRight(@Param("teamId")Integer teamId, @Param("type")Integer type, @Param("status")Integer status);
}
