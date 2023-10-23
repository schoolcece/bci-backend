package com.hcc.task.exception;


import com.hcc.task.entity.CodeEntity;
import lombok.Data;


@Data
public class LoadingException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private int code = 500;
    private String msg;
    private CodeEntity codeEntity;
    private String countKey;
    private int nodeId;

    public LoadingException(String msg, CodeEntity codeEntity, String countKey, int nodeId){
        super(msg);
        this.msg = msg;
        this.codeEntity = codeEntity;
        this.countKey = countKey;
        this.nodeId = nodeId;
    }

}
