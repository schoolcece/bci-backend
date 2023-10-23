package com.hcc.bciauthserver.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterInfo implements Serializable {

    private static final long serialVersionUID = 1L;
//    注册人姓名、注册人电话、电子邮箱、学校、所学专业、uid
    /**
     * 注册人姓名
     */
    private String username;
    /**
     * 注册人电话
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
    private Integer age;
}
