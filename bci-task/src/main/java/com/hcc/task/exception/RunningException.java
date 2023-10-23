package com.hcc.task.exception;

import com.hcc.task.entity.CodeEntity;
import lombok.Data;

@Data
public class RunningException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private int code = 500;
    private String msg;
    boolean cancel;
    private CodeEntity codeEntity;
    private String countKey;
    private int nodeId;
    private int taskId;

    public RunningException(String msg, boolean cancel, CodeEntity codeEntity, String countKey, int nodeId, int taskId){
        super(msg);
        this.msg = msg;
        this.cancel = cancel;
        this.codeEntity = codeEntity;
        this.countKey = countKey;
        this.nodeId = nodeId;
        this.taskId = taskId;
    }
}
