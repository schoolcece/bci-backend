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
}
