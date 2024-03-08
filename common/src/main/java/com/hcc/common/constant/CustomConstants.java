package com.hcc.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * Description: 自定义常量
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
public interface CustomConstants {

    /**
     * 用户角色
     */
    class UserRole {
        /**
         * 普通用户
         */
        public static final int NORMAL_USER = 0;
        /**
         * 管理员
         */
        public static final int ADMIN = 1;
    }

    /**
     * 加入队伍审核状态
     */
    class JoinTeamStatus {
        /**
         * 待审核
         */
        public static final int PENDING = 0;

        /**
         * 审核通过
         */
        public static final int APPROVED = 1;
    }

    /**
     * 队伍状态
     */
    class TeamStatus {
        /**
         * 正常可用
         */
        public static final int NORMAL = 0;

        /**
         * 禁用
         */
        public static final int DISABLED = 1;

        /**
         * 队伍已注销
         */
        public static final int DISBAND = 2;
    }

    /**
     * 参赛申请审核状态
     */
    class ApplicationStatus {
        /**
         * 待审核
         */
        public static final int PENDING = 0;

        /**
         * 审核通过
         */
        public static final int APPROVED = 1;

        /**
         * 审核不通过
         */
        public static final int REJECTED = 2;
    }

    /**
     * 用户队伍关系状态
     */
    class UserTeamRelationStatus {
        /**
         * 待审核
         */
        public static final int PENDING = 0;

        /**
         * 审核通过
         */
        public static final int APPROVED = 1;
    }

    /**
     * 用户队伍关系角色
     */
    class UserTeamRelationRole {
        /**
         * 队员
         */
        public static final int MEMBER = 0;

        /**
         * 队长
         */
        public static final int LEADER = 1;
    }

    /**
     * 范式负责人
     */
    class ParadigmLeader {
        public static final int LEADER = 3;
    }

    /**
     * 内部访问token
     */
    class InnerToken {
        public static final String TOKEN = "masterKeyHcc";
    }

    /**
     * 代码状态
     */
    class BCITaskStatus {
        public static final int PENDING = 0;
        public static final int PROCESSING = 1;
        public static final int SUCCESS = 2;
        public static final int FILED = 3;
    }

    /**
     * token配置
     */
    class TokenConfig {
        public static final long TIMEOUT = 1;
        public static final TimeUnit TIME_UNIT = TimeUnit.HOURS;
    }

    class PageSize {
        public static final int CODE_SIZE = 5;
        public static final int TEAM_SIZE = 10;
        public static final int TASK_SIZE = 5;
        public static final int RANK_SIZE = 10;
    }
}
