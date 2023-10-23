package com.hcc.bciauthserver.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String teamName;
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
     * 状态
     */
    private Integer role;
    /**
     * 年龄
     */
    private Integer age;
}
