package com.hcc.bciauth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.common.model.entity.TeamDO;
import com.hcc.common.model.param.CreateTeamParam;

/**
 * Description: 服务层 队伍相关操作
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
public interface TeamService extends IService<TeamDO> {
    /**
     * 创建队伍
     * @param createTeamParam
     */
    void createTeam(CreateTeamParam createTeamParam);

    /**
     * 申请加入队伍
     * @param teamId
     */
    void joinTeam(int teamId);

    /**
     * 审核入队申请
     * @param userId
     * @param event
     */
    void captainReview(int userId, int event);

    /**
     * 移交队长身份
     * @param userId
     * @param event
     */
    void transferCaptain(int userId, int event);

    /**
     * 离队
     * @param event
     */
    void leaveTeam(int event);

    /**
     * 注销队伍
     * @param event
     */
    void disbandTeam(int event);

    /**
     * 报名参赛
     * @param event
     * @param paradigm
     */
    void registerForCompetition(int event, int paradigm);
}
