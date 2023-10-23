package com.hcc.bciauthserver.controller;

//import com.hcc.bciauthserver.entity.LoginUser;
import com.hcc.bciauthserver.entity.RegisterInfo;
import com.hcc.bciauthserver.mapper.UserAddMapper;
import com.hcc.bciauthserver.service.AuthService;
import com.hcc.common.utils.UserUtils;
import com.hcc.common.vo.UserInfo;
import com.hcc.bciauthserver.entity.UserEntity;
import com.hcc.common.utils.R;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    private StringRedisTemplate redisTemplate;

    @Autowired
    public AuthController(AuthService authService, StringRedisTemplate redisTemplate){
        this.authService = authService;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 用户登录接口 post请求
     * @param loginAcct
     * @param password
     * @return
     */
//    @PostMapping ("/login")
//    public R login(@RequestParam("loginAcct") String loginAcct, @RequestParam("password") String password){
//        return authService.login(loginAcct,password);
//    }

    /**
     * 获取用户信息接口 get请求
     * @return
     */
    @GetMapping("/getInfo")
    public R getInfo(){
        return R.ok().put("userInfo", UserUtils.getUser());
    }

    /**
     * 用户注册接口 post请求
     * @param username
     * @param password
     * @param mobile
     * @return
     */
//    @PostMapping("/register")
//    public R register(@RequestParam("username") String username, @RequestParam("password") String password,
//                      @RequestParam("mobile") String mobile){
//
//        authService.register(username,password,mobile);
//        return R.ok();
//    }

    /**
     * 小鹅营地获取token接口 post请求
     * @param uid
     * @return
     */
    @GetMapping ("/login")
    public R login(@RequestParam("uid") String uid){
        return authService.login(uid);
    }

    /**
     * 小鹅营地同步用户数据接口 post请求
     * @param registerInfoStr
     * @return
     */
    @PostMapping ("/register")
    public R register(@RequestParam String registerInfoStr){
        authService.register(registerInfoStr);
        return R.ok();
    }

    /**
     * 用户注销接口 post请求
     */
    @PostMapping("/logout")
    public R logout(){
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        redisTemplate.delete(token);
        return R.ok();
    }

}
