package com.hcc.common.exception;

import lombok.Data;

@Data
public class RRException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;

    public RRException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
