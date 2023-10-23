package com.hcc.bciauthserver.exception;

public class TeamNameExistException extends RuntimeException{
    public TeamNameExistException(){
        super("队伍名已存在");
    }
}
