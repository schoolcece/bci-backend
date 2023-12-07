package com.hcc.common.utils;

import com.hcc.common.model.bo.UserInfoBO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Description: 用户信息工具类
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
public class UserUtils {

    public static UserInfoBO getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserInfoBO) authentication.getPrincipal();
    }
}
