package com.hcc.common.exception;



public enum BizCodeEnum {
    USER_EXIST(15018, "用户信息已存在"),
    COMMIT_OVER_TIMES(15001,"今日提交次数超过限制"),
    TOKEN_ILLEGAL_EXCEPTION(15002,"非法token"),
    LOGINACCT_PASSEORD_INVALID_EXCEPTION(15003,"账户或密码错误"),
    IDENTITY_LAPSE(15004,"身份失效，请重新登录"),
    USERNAME_EXIST(15005,"用户名已存在"),
    PHONE_EXIST(15006,"手机号已存在"),
    FILE_EMPTY(15007, "上传文件为空"),
    FILE_SAVE_FAILED(15008, "代码保存失败"),
    CODE_NOT_EXIST(15009, "代码不存在"),
    NO_PERMISSION(15010, "无操作权限"),
    LOG_NOT_EXIST(15011, "日志不存在"),
    TEAMNAME_EXIST(15012, "队伍名已存在"),
    TEAM_NOT_EXIST(15013, "要加入的队伍不存在"),
    TEAM_MEMBER_OVER(15014, "队伍满员"),
    NO_COMPUTE_RESOURCES(15015, "暂无计算资源，请稍后再试"),
    COMPUTE_RESOURCE_FAILD(15016, "分配的计算资源不可用，请联系管理员"),
    HAS_TASK_RUNNING(15017, "有任务正在执行， 待执行完毕再提交"),
    INFO_DECODE_FAILED(15019, "用户信息参数解析失败"),
    PARTICIPATED(15020, "已报名该范式"),
    INNER_REJECT(15021, "内部接口不允许访问"),
    NOT_PERMITED(15022, "功能不可用"),
    UN_KNOW_EXCEPTION(15000, "");


    private int code;
    private String msg;
    BizCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
