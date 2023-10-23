package com.hcc.bciauthserver.exception.handler;

import com.hcc.bciauthserver.exception.*;
import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.exception.RRException;
import com.hcc.common.utils.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MobileExistException.class)
    public R handlerPEException(MobileExistException me){
        return R.error(BizCodeEnum.PHONE_EXIST.getCode(), BizCodeEnum.PHONE_EXIST.getMsg());
    }

    @ExceptionHandler(UsernameExistException.class)
    public R handlerUEException(UsernameExistException ue){

        return R.error(BizCodeEnum.USERNAME_EXIST.getCode(), BizCodeEnum.USERNAME_EXIST.getMsg());
    }

    @ExceptionHandler(IllegalToken.class)
    public R handlerITException(IllegalToken it){

        return R.error(BizCodeEnum.TOKEN_ILLEGAL_EXCEPTION.getCode(), BizCodeEnum.TOKEN_ILLEGAL_EXCEPTION.getMsg());
    }
    @ExceptionHandler(AuthenticateException.class)
    public R handlerAException(AuthenticateException ae){

        return R.error(BizCodeEnum.LOGINACCT_PASSEORD_INVALID_EXCEPTION.getCode(), BizCodeEnum.LOGINACCT_PASSEORD_INVALID_EXCEPTION.getMsg());
    }

    @ExceptionHandler(UserNotExistException.class)
    public R handlerUNException(UserNotExistException une){
        return R.error(BizCodeEnum.LOGINACCT_PASSEORD_INVALID_EXCEPTION.getCode(), BizCodeEnum.LOGINACCT_PASSEORD_INVALID_EXCEPTION.getMsg());
    }

    @ExceptionHandler(TeamNameExistException.class)
    public R handlerTeamNameExistException(TeamNameExistException tne){
        return R.error(BizCodeEnum.TEAMNAME_EXIST.getCode(), BizCodeEnum.TEAMNAME_EXIST.getMsg());
    }

    @ExceptionHandler(NoPermissionException.class)
    public R handlerNoPermissionException(NoPermissionException npe){
        return R.error(BizCodeEnum.NO_PERMISSION.getCode(), BizCodeEnum.NO_PERMISSION.getMsg());
    }

    @ExceptionHandler(TeamNotExistException.class)
    public R handlerTeamNotExistException(TeamNotExistException tne){
        return R.error(BizCodeEnum.TEAM_NOT_EXIST.getCode(), BizCodeEnum.TEAM_NOT_EXIST.getMsg());
    }

    @ExceptionHandler(TeamMemberOverException.class)
    public R handlerTeamMemberOverException(TeamMemberOverException tmoe){
        return R.error(BizCodeEnum.TEAM_MEMBER_OVER.getCode(), BizCodeEnum.TEAM_MEMBER_OVER.getMsg());
    }

    @ExceptionHandler(RRException.class)
    public R handlerRRException(RRException re){
        return R.error(re.getCode(),re.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public R handlerUnKnowException(Exception uke){
        return R.error(BizCodeEnum.UN_KNOW_EXCEPTION.getCode(), uke.getMessage());
    }

}
