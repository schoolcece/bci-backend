package com.hcc.bciauthserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.bciauthserver.entity.MemberInfoVo;
import com.hcc.bciauthserver.entity.TeamEntity;
import com.hcc.bciauthserver.entity.TeamInfoVo;
import com.hcc.bciauthserver.entity.UserEntity;
import com.hcc.common.utils.R;

import java.util.List;

public interface TeamService extends IService<TeamEntity> {
    void buildTeam(TeamInfoVo teamInfoVo);

    void joinTeam(int teamId);

    void leaveTeam(int teamId, int competition);

    void auditTeamMeber(int userId, int competition);

    void transferTeamLeader(int userId, int competition);

    void deleteTeam(int competition);

    void participate(int type, int competition);

    List<MemberInfoVo> getTeamMembers(Integer competition);

    String getTeamName(int teamId);

    List<TeamEntity> getTeamInfo(int competition, int type);

    List<MemberInfoVo> getTeamMembersByTeamId(int competition, int teamId);

    void auditTeam(int teamId, int type, int status);
}
