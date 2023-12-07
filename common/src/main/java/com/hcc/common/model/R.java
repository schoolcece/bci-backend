package com.hcc.common.model;

import com.hcc.common.enums.ErrorCodeEnum;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String,Object> {

    private static final long serialVersionUID = 1L;

    private final int SUCCESS_CODE = 100200;

    public R(){
        put("code", SUCCESS_CODE);
        put("msg","success");
    }

    public static R error(int code, String msg){
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(){
        return R.error(ErrorCodeEnum.UN_KNOW_EXCEPTION.getCode(), ErrorCodeEnum.UN_KNOW_EXCEPTION.getMsg());
    }

    public static R ok(int code, String msg){
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String,Object> map){
        R r= new R();
        r.putAll(map);
        return r;
    }

    public static R ok(){
        return new R();
    }

    public R put(String key,Object value){
        super.put(key,value);
        return this;
    }
}
