package com.hcc.common.expression;

import com.hcc.common.vo.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component("JER")
public class JoinExpressionRoot {

    public boolean hasJoin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        Map<String, String[]> parameterMap = (Map<String, String[]>) authentication.getCredentials();
        String[] types = parameterMap.get("type");
        if(Objects.isNull(types)){
            return false;
        }
        int type = Integer.valueOf(types[0]);
        if(type>=5){
            return userInfo.getRole()[1]==9||userInfo.getRole()[1]==8||userInfo.getPermissions().contains(type);
        }else {
            return userInfo.getRole()[0]==9||userInfo.getRole()[0]-3==type||userInfo.getPermissions().contains(type);
        }
    }

    public boolean isNotAllTeamLeader(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        return userInfo.getRole()[0] == 2&&userInfo.getRole()[1] == 2;
    }

    public boolean isNormalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        return userInfo.getRole()[0] == 0||userInfo.getRole()[1]==0;
    }

    public boolean isTeamMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        return userInfo.getRole()[0] == 1;
    }
}
