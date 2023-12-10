package com.hcc.common.utils;

import com.hcc.common.constant.CustomConstants;
import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import com.hcc.common.model.bo.UserInfoBO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

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
