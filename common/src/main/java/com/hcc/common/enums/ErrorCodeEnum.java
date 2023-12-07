package com.hcc.common.enums;

import lombok.AllArgsConstructor;

/**
 * 异常错误枚举, 包含错误码和错误信息
 */
@AllArgsConstructor
public enum ErrorCodeEnum {

    // 1. 系统异常
    // 1.1 未知异常
    UN_KNOW_EXCEPTION(100500, "系统未知异常， 请联系管理员解决！"),

    // 2. 客户端异常
    // 2.1 请求参数异常
    PARAM_EXCEPTION(100400, "请求参数异常"),
    // 2.2 登录身份认证异常
    USER_NOT_EXIST(100401, "用户不存在"),
    ACCOUNT_PASSWORD_INVALID(100402, "用户名或密码错误"),
    // 2.3 用户无权限
    NO_PERMISSION(100403, "用户无权限"),
    // 2.4 队伍名已存在
    TEAM_NAME_EXIST(100404, "队伍名重复"),
    // 2.5 队伍不存在
    TEAM_NOT_EXIST(100405, "队伍不存在"),
    // 2.6 队伍人数超上限
    TEAM_MEMBER_OVER(100406, "队伍人数超上限");


    /**
     * 错误码
     */
    private int code;

    /**
     * 错误信息
     */
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
