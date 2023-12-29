package com.hcc.common.model.param;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 用户登录传递参数
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginParam {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}

