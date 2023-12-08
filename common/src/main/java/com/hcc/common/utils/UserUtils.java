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

    private static UserInfoBO userInfoBO;

    public static UserInfoBO getUser() {
        if (Objects.isNull(userInfoBO)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userInfoBO = (UserInfoBO) authentication.getPrincipal();
        }
        return userInfoBO;
    }

    public static boolean isCaptain(int event) {
        UserInfoBO.TeamInfo teamInfo = Optional.ofNullable(userInfoBO.getTeamInfoMap()).
                orElseThrow(() -> new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg()))
                .get(event);
        return !Objects.isNull(teamInfo) && teamInfo.getRole() == CustomConstants.UserTeamRelationRole.LEADER;
    }
}
