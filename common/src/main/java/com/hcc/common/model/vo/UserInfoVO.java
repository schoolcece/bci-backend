package com.hcc.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @Author: hcc
 * @Date: 2024/1/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {
    /**
     * 用户Id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 学校、单位
     */
    private String university;
    /**
     * 角色
     */
    private int role;
    /**
     * 状态
     */
    private int status;
}
