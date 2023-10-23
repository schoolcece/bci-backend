package com.hcc.common.utils;

import java.util.HashMap;
import java.util.Map;

public class R<T> extends HashMap<String,Object> {
    private static final long serialVersionUID = 1L;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public R(){
        put("code", 200);
        put("msg","success");
    }

    public static R error(int code, String msg){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

    public static R error(){
        return R.error(500,"未知异常，请联系管理员");
    }

    public static R ok(int code, String msg){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
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
