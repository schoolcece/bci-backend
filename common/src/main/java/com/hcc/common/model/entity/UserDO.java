package com.hcc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hcc.common.constant.CustomConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Description: 用户实体类
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("bci_user")
public class UserDO {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 学校
     */
    private String university;
    /**
     * 专业
     */
    private String profession;
    /**
     * uid
     */
    private String uid;
    /**
     * 年龄（身份证认证后的补充信息）
     */
    private Date birthday;
    /**
     * 状态  0：禁用   1：正常
     */
    private int status;
    /**
     * 角色: 0 代表普通用户， 1 代表管理员
     */
    private int role;
    /**
     * 创建时间
     */
    private Date createTime;

    public boolean isAdmin() {
        return role == CustomConstants.UserRole.ADMIN;
    }
}
