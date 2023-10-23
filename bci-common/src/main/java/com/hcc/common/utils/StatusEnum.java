package com.hcc.common.utils;

public enum StatusEnum {
    CODE_STATUS_SUBMITTED(0),
    CODE_STATUS_INIT(1),
    CODE_STATUS_RUNNING(2),
    CODE_STATUS_COMPLETED(3),
    CODE_STATUS_FILED(4),

    TASK_STATUS_INIT(0),
    TASK_STATUS_RUNNING(1),
    TASK_STATUS_COMPLETED(2),
    TASK_STATUS_FILED(3)
    ;

    private int status;

    StatusEnum(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }
}
