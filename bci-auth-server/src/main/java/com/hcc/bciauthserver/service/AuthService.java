package com.hcc.bciauthserver.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hcc.bciauthserver.entity.RegisterInfo;
import com.hcc.bciauthserver.entity.UserEntity;
import com.hcc.common.utils.R;

public interface AuthService extends IService<UserEntity> {
//    R login(String loginAcct, String password);

//    void register(String username, String password, String mobile);

    R login(String uid);

    void register(String registerInfoStr);
}
