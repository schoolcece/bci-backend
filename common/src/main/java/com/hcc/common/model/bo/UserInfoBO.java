package com.hcc.common.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户完成登录认证后缓存信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfoBO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private int userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 是否为管理员
     */
    private boolean isAdmin;

    /**
     * 用户队伍信息 key为赛事id， value为对应赛事的队伍信息
     */
    private Map<Integer, TeamInfo> teamInfoMap;

    /**
     * 用户权限信息 key为范式id， value为对应范式的权限: 0 代表待审核， 1 代表审核已通过， 2 代表审核未通过， 3代表范式负责人
     */
    private Map<Integer, Integer> permissions;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TeamInfo {
        /**
         * 队伍id
         */
        private int teamId;

        /**
         * 队伍角色: 0 代表队员， 1 代表队长
         */
        private int role;

        /**
         * 审核状态： 0 代表待审核， 1 代表审核通过
         */
        private int status;
    }
}
