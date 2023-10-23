package com.hcc.bciauthserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.hcc.bciauthserver.entity.LoginUser;

import com.hcc.bciauthserver.entity.RegisterInfo;
import com.hcc.bciauthserver.entity.UserAddEntity;
import com.hcc.bciauthserver.exception.MobileExistException;
import com.hcc.bciauthserver.exception.UserNotExistException;
import com.hcc.bciauthserver.exception.UsernameExistException;
import com.hcc.bciauthserver.mapper.TeamRightMapper;
import com.hcc.bciauthserver.mapper.UserAddMapper;
import com.hcc.bciauthserver.mapper.UserMapper;
import com.hcc.bciauthserver.service.AuthService;
import com.hcc.bciauthserver.entity.UserEntity;
import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.exception.RRException;
import com.hcc.common.utils.AesUtil;
import com.hcc.common.utils.R;
import com.hcc.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements AuthService  {

    private AuthenticationManager authenticationManager;

    private StringRedisTemplate redisTemplate;

    private UserMapper userMapper;

    private UserAddMapper userAddMapper;

    private TeamRightMapper teamRightMapper;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, StringRedisTemplate redisTemplate,
                           UserMapper userMapper, UserAddMapper userAddMapper, TeamRightMapper teamRightMapper){
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
        this.userMapper = userMapper;
        this.userAddMapper = userAddMapper;
        this.teamRightMapper = teamRightMapper;
    }

//    @Override
//    public R login(String loginAcct, String password) {
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginAcct, password);
//        Authentication authentication;
//        try{
//            authentication = authenticationManager.authenticate(authenticationToken);
//        }catch (Exception e){
//            return R.error(BizCodeEnume.LOGINACCT_PASSEORD_INVALID_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSEORD_INVALID_EXCEPTION.getMsg());
//        }
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        UserEntity user = loginUser.getUser();
//        if(Objects.isNull(user)){
//            return R.error(BizCodeEnume.LOGINACCT_PASSEORD_INVALID_EXCEPTION.getCode(),BizCodeEnume.LOGINACCT_PASSEORD_INVALID_EXCEPTION.getMsg());
//        }else{
//            String token = UUID.randomUUID().toString().replace("-","");
//            UserInfo userInfo = new UserInfo(user.getUsername(), user.getUserId(), loginUser.getPermissions(), loginUser.getUndoPermissions(),loginUser.getTeamId(), loginUser.getRole());
//            redisTemplate.opsForValue().set(token, JSON.toJSONString(userInfo),1, TimeUnit.HOURS);
//            return R.ok().put("token",token);
//        }
//    }

//    @Transactional
//    @Override
//    public void register(String username, String password, String mobile) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//        UserEntity user = new UserEntity();
//        checkUsernameUnique(username);
//        checkMobileUnique(mobile);
//        user.setUsername(username);
//        user.setMobile(mobile);
//        user.setCreateTime(new Timestamp(new Date().getTime()));
//        String passwordDb = passwordEncoder.encode(password);
//        user.setPassword(passwordDb);
//        userMapper.insert(user);
//        UserAddEntity userAddEntity = new UserAddEntity();
//        userAddEntity.setUserId(user.getUserId());
//        userAddEntity.setCompetition(0);
//        userAddEntity.setCreateTime(new Timestamp(new Date().getTime()));
//        userAddMapper.insert(userAddEntity);
//        userAddEntity.setCompetition(1);
//        userAddMapper.insert(userAddEntity);
//    }

    @Override
    public R login(String uid) {
        // 身份校验
        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("uid", uid));
        if(Objects.isNull(user)){
            throw new UserNotExistException();
        }

        // 查询该用户未过期token，如果存在则移除
        String oldToken = redisTemplate.opsForValue().get(user.getUserId().toString());
        if (oldToken != null){
            redisTemplate.delete(oldToken);
        }

        // 查询权限信息
        List<Integer> permissions = new ArrayList<>();
        List<Integer> undoPermissions = new ArrayList<>();
        Integer[] team = new Integer[2];
        Integer[] role = new Integer[2];
        List<UserAddEntity> userAddEntities = userAddMapper.selectList(new QueryWrapper<UserAddEntity>().eq("user_id", user.getUserId()));
        for(UserAddEntity userAddEntity:userAddEntities){
            if(userAddEntity.getCompetition() == 0){
                team[0] = userAddEntity.getTeamId();
                role[0] = userAddEntity.getRole();
                if(!Objects.isNull(userAddEntity.getTeamId())&&userAddEntity.getRole()!=0){
                    List<Integer> rights = teamRightMapper.selectRightsByTeamId(userAddEntity.getTeamId());
                    for(Integer right:rights){
                        permissions.add(right);
                    }
                    List<Integer> undoRights = teamRightMapper.selectUndoRightsByTeamId(userAddEntity.getTeamId());
                    for(Integer undoRight:undoRights){
                        undoPermissions.add(undoRight);
                    }
                }
            }else{
                team[1] = userAddEntity.getTeamId();
                role[1] = userAddEntity.getRole();
                if(!Objects.isNull(userAddEntity.getTeamId())&&userAddEntity.getRole()==2){
                    List<Integer> rights = teamRightMapper.selectRightsByTeamId(userAddEntity.getTeamId());
                    for(Integer right:rights){
                        permissions.add(right);
                    }
                    List<Integer> undoRights = teamRightMapper.selectUndoRightsByTeamId(userAddEntity.getTeamId());
                    for(Integer undoRight:undoRights){
                        undoPermissions.add(undoRight);
                    }
                }
            }
        }

        // 封装用户信息并生成token，将token与用户信息以键值对的形式存入redis
        UserInfo userInfo = new UserInfo(user.getUsername(), user.getUserId(), permissions, undoPermissions, team, role);
        String token = UUID.randomUUID().toString().replace("-","");
        redisTemplate.opsForValue().set(token, JSON.toJSONString(userInfo),5, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(userInfo.getUserId().toString(), token, 5, TimeUnit.HOURS);
        return R.ok().put("token", token);
    }

    @Transactional
    @Override
    public void register(String registerInfoStr) {

        String decrypt = AesUtil.decrypt(registerInfoStr);
        if (decrypt == null){
            throw new RRException(BizCodeEnum.INFO_DECODE_FAILED.getCode(), BizCodeEnum.INFO_DECODE_FAILED.getMsg());
        }
        RegisterInfo registerInfo = JSON.parseObject(decrypt, new TypeReference<RegisterInfo>() {});
        UserEntity user = new UserEntity();
        checkUidUnique(registerInfo.getUid());
        user.setUsername(registerInfo.getUsername());
        user.setMobile(registerInfo.getMobile());
        user.setUniversity(registerInfo.getUniversity());
        user.setAge(registerInfo.getAge());
        user.setEmail(registerInfo.getEmail());
        user.setProfession(registerInfo.getProfession());
        user.setUid(registerInfo.getUid());
        user.setCreateTime(new Timestamp(new Date().getTime()));

        userMapper.insert(user);
        UserAddEntity userAddEntity = new UserAddEntity();
        userAddEntity.setUserId(user.getUserId());
        userAddEntity.setCompetition(0);
        userAddEntity.setCreateTime(new Timestamp(new Date().getTime()));
        userAddMapper.insert(userAddEntity);
        userAddEntity.setCompetition(1);
        userAddMapper.insert(userAddEntity);
    }


    private void checkMobileUnique(String mobile) {
        Integer count = userMapper.selectCount(new QueryWrapper<UserEntity>().eq("mobile", mobile));
        if(count>0){
            throw(new MobileExistException());
        }
    }

    private void checkUsernameUnique(String username) {
        Integer count = userMapper.selectCount(new QueryWrapper<UserEntity>().eq("username", username));
        if(count>0){
            throw(new UsernameExistException());
        }

    }

    private void checkUidUnique(String uid) {
        int count = userMapper.selectCount(new QueryWrapper<UserEntity>().eq("uid", uid));
        if(count>0){
            throw new RRException(BizCodeEnum.USER_EXIST.getCode(), BizCodeEnum.USER_EXIST.getMsg());
        }
    }

}
