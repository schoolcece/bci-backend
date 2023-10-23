package com.hcc.bciauthserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.bciauthserver.entity.UserAddEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAddMapper extends BaseMapper<UserAddEntity> {
    void updateTeamIdAndRoleByUserIdAndCompetition(@Param("userId")Integer userId, @Param("competition")Integer competition, @Param("teamId")Integer teamId, @Param("role")Integer role);

    Integer countByTeamId(Integer teamId);

    void updateTeamIdByUserIdAndCompetition(@Param("userId")Integer userId, @Param("competition")Integer competition, @Param("teamId")Integer teamId);

    void updateTeamIdAndRoleByUserIdAndTeamId(@Param("userId")Integer userId, @Param("teamId")Integer teamId, @Param("role")Integer role);

    Integer selectTeamIdByUserIdAndCompetition(@Param("userId")Integer userId, @Param("competition")Integer competition);

    void updateRoleByUserIdAndCompetition(@Param("userId")Integer userId, @Param("competition")Integer competition, @Param("role")Integer role);

    String selectTeamNameByUserId(@Param("userId")int userId, @Param("competition")int competition);
}
