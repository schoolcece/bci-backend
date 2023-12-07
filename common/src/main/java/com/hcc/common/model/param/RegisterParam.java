package com.hcc.common.model.param;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * Description: 用户注册传递参数
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterParam {
    private static final long serialVersionUID = 1L;

    /**
     * 注册人姓名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 注册人电话
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不正确")
    private String mobile;
    /**
     * 电子邮箱
     */
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
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
     * 出生年月（身份证认证后的补充信息）
     */
    private Timestamp birthday;
}
