package com.hcc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 用户队伍关系
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
@Data
@Builder
@TableName("bci_user_team")
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 队伍id
     */
    private int teamId;

    /**
     * 赛事id
     */
    private int eventId;

    /**
     * 角色
     */
    private int role;

    /**
     * 状态
     */
    private int status;
}
