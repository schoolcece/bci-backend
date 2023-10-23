package com.hcc.task.exception;

import lombok.Data;

@Data
public class RRException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private int code = 500;
    private String msg;

    public RRException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
    public RRException(String msg){
        super(msg);
        this.msg = msg;
    }
}
