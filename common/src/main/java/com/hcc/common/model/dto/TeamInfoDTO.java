package com.hcc.common.model.dto;

import lombok.Data;

/**
 * Description: 队伍信息
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@Data
public class TeamInfoDTO {

    /**
     * 赛事id
     */
    private int eventId;

    /**
     * 队伍id
     */
    private int teamId;

    /**
     * 队伍角色
     */
    private int role;

    /**
     * 审核状态： 0 代表待审核， 1 代表审核通过
     */
    private int status;
}
