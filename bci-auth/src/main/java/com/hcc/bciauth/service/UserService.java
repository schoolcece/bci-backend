package com.hcc.bciauth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hcc.common.model.entity.UserDO;
import com.hcc.common.model.param.LoginParam;

/**
 * 服务层：用户操作接口
 */
public interface UserService extends IService<UserDO> {

    /**
     * 用户登录服务
     */
    String login(LoginParam loginInfoDTO);
}
