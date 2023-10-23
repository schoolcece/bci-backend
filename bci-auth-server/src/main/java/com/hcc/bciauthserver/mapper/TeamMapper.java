package com.hcc.bciauthserver.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.bciauthserver.entity.MemberInfoVo;
import com.hcc.bciauthserver.entity.TeamEntity;
import com.hcc.bciauthserver.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeamMapper extends BaseMapper<TeamEntity> {

    Integer countByTeamName(String teamName);

    List<MemberInfoVo> selectByIdAndCompetition(@Param("teamId")Integer teamId, @Param("competition")Integer competition);

    String selectTeamNameByTeamId(@Param("teamId")int teamId);

    List<TeamEntity> getTeamInfo(@Param("competition")int competition, @Param("type")int type);
}
