package com.hcc.bciauth.controller;

import com.hcc.common.model.param.LoginParam;
import com.hcc.common.model.param.RegisterParam;
import com.hcc.bciauth.service.UserService;
import com.hcc.common.annotation.Loggable;
import com.hcc.common.model.R;
import com.hcc.common.utils.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description: controller层 用户相关请求
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * @param registerParam
     * @return
     */
    @Loggable("用户注册")
    @PostMapping("/register")
    public R register(@RequestBody @Valid RegisterParam registerParam) {
        return R.ok();
    }

    /**
     * 用户登录接口
     * @param loginParam
     * @return
     */
    @Loggable("用户登录")
    @PostMapping("/login")
    public R login(@RequestBody @Valid LoginParam loginParam) {
        return R.ok().put("data", userService.login(loginParam));
    }

    /**
     * 获取用户信息接口
     * @return
     */
    @GetMapping("/getInfo")
    public R getInfo() {
        return R.ok().put("data", UserUtils.getUser());
    }

    /**
     * 用户注销接口
     * @return
     */
    public R logout(){
        userService.logout();
        return R.ok();
    }
}
