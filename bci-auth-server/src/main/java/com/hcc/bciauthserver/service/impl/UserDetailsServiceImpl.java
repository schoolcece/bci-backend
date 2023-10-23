//package com.hcc.bciauthserver.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.hcc.bciauthserver.entity.LoginUser;
//import com.hcc.bciauthserver.entity.TeamEntity;
//import com.hcc.bciauthserver.entity.UserAddEntity;
//import com.hcc.bciauthserver.entity.UserEntity;
//import com.hcc.bciauthserver.exception.AuthenticateException;
//import com.hcc.bciauthserver.exception.UserNotExistException;
//import com.hcc.bciauthserver.mapper.TeamMapper;
//import com.hcc.bciauthserver.mapper.TeamRightMapper;
//import com.hcc.bciauthserver.mapper.UserAddMapper;
//import com.hcc.bciauthserver.mapper.UserMapper;
//import com.hcc.bciauthserver.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private UserMapper userMapper;
//    private TeamRightMapper teamRightMapper;
//    private UserAddMapper userAddMapper;
//
//    @Autowired
//    public UserDetailsServiceImpl(UserMapper userMapper, TeamRightMapper teamRightMapper, UserAddMapper userAddMapper){
//        this.userMapper = userMapper;
//        this.teamRightMapper = teamRightMapper;
//        this.userAddMapper = userAddMapper;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String loginAcct) throws UsernameNotFoundException {
//        // 查询用户信息
//        UserEntity user = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", loginAcct).or().eq("mobile", loginAcct));
//        if(Objects.isNull(user)){
//            throw new UserNotExistException();
//        }
//        LoginUser loginUser = new LoginUser();
//
//        // 查询权限信息
//        List<Integer> permissions = new ArrayList<>();
//        List<Integer> undoPermissions = new ArrayList<>();
//        Integer[] team = new Integer[2];
//        Integer[] role = new Integer[2];
//        List<UserAddEntity> userAddEntities = userAddMapper.selectList(new QueryWrapper<UserAddEntity>().eq("user_id", user.getUserId()));
//        for(UserAddEntity userAddEntity:userAddEntities){
//            if(userAddEntity.getCompetition() == 0){
//                team[0] = userAddEntity.getTeamId();
//                role[0] = userAddEntity.getRole();
//                if(!Objects.isNull(userAddEntity.getTeamId())&&userAddEntity.getRole()!=0){
//                    List<Integer> rights = teamRightMapper.selectRightsByTeamId(userAddEntity.getTeamId());
//                    for(Integer right:rights){
//                        permissions.add(right);
//                    }
//                    List<Integer> undoRights = teamRightMapper.selectUndoRightsByTeamId(userAddEntity.getTeamId());
//                    for(Integer undoRight:undoRights){
//                        undoPermissions.add(undoRight);
//                    }
//                }
//            }else{
//                team[1] = userAddEntity.getTeamId();
//                role[1] = userAddEntity.getRole();
//                if(!Objects.isNull(userAddEntity.getTeamId())&&userAddEntity.getRole()==2){
//                    List<Integer> rights = teamRightMapper.selectRightsByTeamId(userAddEntity.getTeamId());
//                    for(Integer right:rights){
//                        permissions.add(right);
//                    }
//                    List<Integer> undoRights = teamRightMapper.selectUndoRightsByTeamId(userAddEntity.getTeamId());
//                    for(Integer undoRight:undoRights){
//                        undoPermissions.add(undoRight);
//                    }
//                }
//            }
//        }
//        loginUser.setTeamId(team);
//        loginUser.setRole(role);
//        loginUser.setUser(user);
//        loginUser.setPermissions(permissions);
//        loginUser.setUndoPermissions(undoPermissions);
//        return loginUser;
//    }
//}
