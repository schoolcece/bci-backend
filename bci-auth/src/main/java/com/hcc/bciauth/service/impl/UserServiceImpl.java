package com.hcc.bciauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hcc.common.constant.CustomConstants;
import com.hcc.bciauth.mapper.TeamMapper;
import com.hcc.bciauth.mapper.UserMapper;
import com.hcc.common.model.bo.UserInfoBO;
import com.hcc.common.model.dto.PermissionInfoDTO;
import com.hcc.common.model.dto.TeamInfoDTO;
import com.hcc.common.model.entity.UserDO;
import com.hcc.common.model.param.LoginParam;
import com.hcc.bciauth.service.UserService;
import com.hcc.common.component.RedisComponent;
import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import com.hcc.common.model.param.RegisterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private RedisComponent redisComponent;


//    @Override
//    public String login(LoginParam loginParam) {
//        // 1. 认证，核验身份信息
//        UserDO userDO = userMapper.
//                selectOne(new QueryWrapper<UserDO>().
//                        eq("username", loginParam.getUsername()).
//                        or().
//                        eq("mobile", loginParam.getUsername()));
//        if (userDO == null) {
//            throw new RTException(ErrorCodeEnum.USER_NOT_EXIST.getCode(), ErrorCodeEnum.USER_NOT_EXIST.getMsg());
//        }
//        if (!loginParam.getPassword().equals(userDO.getUid())) {
//            throw new RTException(ErrorCodeEnum.ACCOUNT_PASSWORD_INVALID.getCode(), ErrorCodeEnum.ACCOUNT_PASSWORD_INVALID.getMsg());
//        }
//
//        // 1.1 查询该用户是否存在未过期token， 存在则移除
//        String oldToken = redisComponent.getString(userDO.getId().toString());
//        if (oldToken != null) {
//            redisComponent.deleteForObject(oldToken);
//        }
//
//        // 2. 鉴权
//        Map<Integer, UserInfoBO.TeamInfo> teamInfoMap = new HashMap<>();
//        Map<Integer, Integer> permissions = new HashMap<>();
//        UserInfoBO userInfoBO = UserInfoBO.builder()
//                .userId(userDO.getId())
//                .username(userDO.getUsername())
//                .teamInfoMap(teamInfoMap)
//                .permissions(permissions)
//                .build();
//
//        // 2.1 普通用户
//        if (!userDO.isAdmin()) {
//            userInfoBO.setAdmin(false);
//            // 2.2 查询各个赛事的队伍信息
//            List<TeamInfoDTO> teamInfoDTOS = teamMapper.selectTeamInfoByUserId(userDO.getId());
//            teamInfoDTOS.forEach(o ->
//                    teamInfoMap.put(o.getEventId(), new UserInfoBO.TeamInfo(o.getTeamId(), o.getRole(), o.getStatus())));
//            // 2.3 查询各个范式的参赛权限信息
//            List<PermissionInfoDTO> permissionInfoDTOS = new ArrayList<>();
//            teamInfoDTOS.forEach(o -> {
//                permissionInfoDTOS.addAll(teamMapper.selectPermissionByTeamId(o.getTeamId()));
//            });
//            permissionInfoDTOS.forEach(o -> permissions.put(o.getParadigmId(), o.getStatus()));
//            // 2.4 查询用户是否具有赛题负责人权限
//            List<Integer> paradigms = userMapper.selectByUserId(userDO.getId());
//            paradigms.forEach(o -> permissions.put(o, CustomConstants.ParadigmLeader.LEADER));
//        } else {
//            userInfoBO.setAdmin(true);
//        }
//
//        // 3. 缓存用户信息， 以随机token为key， 以用户信息为value， 将用户信息缓存到redis 返回token
//        String token = UUID.randomUUID().toString().replace("-","");
//        redisComponent.setObject(token, userInfoBO, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
//        redisComponent.setString(userDO.getId().toString(), token, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
//        return token;
//    }

    @Override
    public String login(String uid) {
        UserDO userDO = userMapper.selectOne(new QueryWrapper<UserDO>().eq("uid", uid));
        if (Objects.isNull(userDO)) {
            throw new RTException(ErrorCodeEnum.USER_NOT_EXIST.getCode(), ErrorCodeEnum.USER_NOT_EXIST.getMsg());
        }

        String oldToken = redisComponent.getString(userDO.getId().toString());
        if (oldToken != null) {
            redisComponent.deleteForObject(oldToken);
        }

        Map<Integer, UserInfoBO.TeamInfo> teamInfoMap = new HashMap<>();
        Map<Integer, Integer> permissions = new HashMap<>();
        UserInfoBO userInfoBO = UserInfoBO.builder()
                .userId(userDO.getId())
                .username(userDO.getUsername())
                .teamInfoMap(teamInfoMap)
                .permissions(permissions)
                .build();

        // 2.1 普通用户
        if (!userDO.isAdmin()) {
            userInfoBO.setAdmin(false);
            // 2.2 查询各个赛事的队伍信息
            List<TeamInfoDTO> teamInfoDTOS = teamMapper.selectTeamInfoByUserId(userDO.getId());
            teamInfoDTOS.forEach(o ->
                    teamInfoMap.put(o.getEventId(), new UserInfoBO.TeamInfo(o.getTeamId(), o.getRole(), o.getStatus())));
            // 2.3 查询各个范式的参赛权限信息
            List<PermissionInfoDTO> permissionInfoDTOS = new ArrayList<>();
            teamInfoDTOS.forEach(o -> {
                permissionInfoDTOS.addAll(teamMapper.selectPermissionByTeamId(o.getTeamId()));
            });
            permissionInfoDTOS.forEach(o -> permissions.put(o.getParadigmId(), o.getStatus()));
            // 2.4 查询用户是否具有赛题负责人权限
            List<Integer> paradigms = userMapper.selectByUserId(userDO.getId());
            paradigms.forEach(o -> permissions.put(o, CustomConstants.ParadigmLeader.LEADER));
        } else {
            userInfoBO.setAdmin(true);
        }

        // 3. 缓存用户信息， 以随机token为key， 以用户信息为value， 将用户信息缓存到redis 返回token
        String token = UUID.randomUUID().toString().replace("-","");
        redisComponent.setObject(token, userInfoBO, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
        redisComponent.setString(userDO.getId().toString(), token, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
        return token;
    }

    @Override
    public void logout() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = Objects.requireNonNull(requestAttributes).getRequest().getHeader("token");
        redisComponent.deleteForObject(token);
    }

    @Transactional
    @Override
    public void register(RegisterParam registerParam) {
        checkUidUnique(registerParam.getUid());
        UserDO user = UserDO.builder()
                .username(registerParam.getUsername())
                .mobile(registerParam.getMobile())
                .university(registerParam.getUniversity())
                .birthday(registerParam.getBirthday())
                .email(registerParam.getEmail())
                .profession(registerParam.getProfession())
                .uid(registerParam.getUid())
                .status(1)
                .role(0)
                .createTime(new Timestamp(new Date().getTime()))
                .build();
        userMapper.insert(user);
    }

    private void checkUidUnique(String uid) {
        Long count = userMapper.selectCount(new QueryWrapper<UserDO>().eq("uid", uid));
        if (count > 0) {
            throw new RTException(ErrorCodeEnum.USER_ALREADY_EXITS.getCode(), ErrorCodeEnum.USER_ALREADY_EXITS.getMsg());
        }
    }

}
