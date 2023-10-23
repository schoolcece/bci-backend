package com.hcc.bciauthserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hcc.bciauthserver.entity.MemberInfoVo;
import com.hcc.bciauthserver.entity.TeamEntity;
import com.hcc.bciauthserver.entity.TeamInfoVo;
import com.hcc.bciauthserver.entity.TeamRightEntity;
import com.hcc.bciauthserver.exception.NoPermissionException;
import com.hcc.bciauthserver.exception.TeamMemberOverException;
import com.hcc.bciauthserver.exception.TeamNameExistException;
import com.hcc.bciauthserver.feign.CodeFeign;
import com.hcc.bciauthserver.feign.TaskFeign;
import com.hcc.bciauthserver.mapper.TeamMapper;
import com.hcc.bciauthserver.mapper.TeamRightMapper;
import com.hcc.bciauthserver.mapper.UserAddMapper;
import com.hcc.bciauthserver.mapper.UserMapper;
import com.hcc.bciauthserver.service.TeamService;
import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.exception.RRException;
import com.hcc.common.utils.RoleEnum;
import com.hcc.common.utils.UserUtils;
import com.hcc.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, TeamEntity> implements TeamService{

    private TeamMapper teamMapper;

    private UserMapper userMapper;

    private UserAddMapper userAddMapper;

    private TeamRightMapper teamRightMapper;

    private StringRedisTemplate redisTemplate;

    private CodeFeign codeFeign;

    private TaskFeign taskFeign;

    @Autowired
    public TeamServiceImpl(TeamMapper teamMapper, UserMapper userMapper, UserAddMapper userAddMapper,
                           TeamRightMapper teamRightMapper, StringRedisTemplate redisTemplate,
                           CodeFeign codeFeign, TaskFeign taskFeign){
        this.teamMapper = teamMapper;
        this.userMapper = userMapper;
        this.userAddMapper = userAddMapper;
        this.teamRightMapper = teamRightMapper;
        this.redisTemplate = redisTemplate;
        this.codeFeign = codeFeign;
        this.taskFeign = taskFeign;
    }

    @Transactional
    @Override
    public void buildTeam(TeamInfoVo teamInfoVo) {
        UserInfo userInfo = UserUtils.getUser();
        //1. 鉴权
        if(userInfo.getRole()[teamInfoVo.getCompetition()]!=0){
            throw(new NoPermissionException());
        }
        //2. 检查队名唯一性
        checkTeamNameUnique(teamInfoVo.getTeamName());

        //3. 新增队伍信息
        TeamEntity team = new TeamEntity();
        team.setTeamName(teamInfoVo.getTeamName());
        team.setCreateTime(new Timestamp(new Date().getTime()));
        team.setInstructor(teamInfoVo.getInstructor());
        team.setUniversity(teamInfoVo.getUniversity());
        team.setUserId(userInfo.getUserId());
        team.setCompetition(teamInfoVo.getCompetition());
        teamMapper.insert(team);

        //4. 新增队伍权限信息
        TeamRightEntity teamRightEntity = new TeamRightEntity();
        teamRightEntity.setTeamId(team.getTeamId());
        teamRightEntity.setCreateTime(new Timestamp(new Date().getTime()));
        if(team.getCompetition() == 0){
            for(int i=0;i<5; i++){
                teamRightEntity.setRight(i);
                teamRightMapper.insert(teamRightEntity);
            }
        }else{
            teamRightEntity.setRight(5);
            teamRightMapper.insert(teamRightEntity);
        }

        //5. 更新用户角色以及队伍信息
        userAddMapper.updateTeamIdAndRoleByUserIdAndCompetition(userInfo.getUserId(), teamInfoVo.getCompetition(), team.getTeamId(), 2);

        //6. 更新登录态保存信息
        if(teamInfoVo.getCompetition() == 0){
            userInfo.setTeamId(new Integer[]{team.getTeamId(), userInfo.getTeamId()[1]});
            userInfo.setRole(new Integer[]{2, userInfo.getRole()[1]});
        }else{
            userInfo.setTeamId(new Integer[]{userInfo.getTeamId()[0], team.getTeamId()});
            userInfo.setRole(new Integer[]{userInfo.getRole()[0], 2});
        }
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        redisTemplate.opsForValue().set(requestAttributes.getRequest().getHeader("token"), JSON.toJSONString(userInfo),1, TimeUnit.HOURS);
    }

    @Override
    public void joinTeam(int teamId) {
        UserInfo userInfo = UserUtils.getUser();

        //1. 校验team是否存在, 用户是否有权限加队 ,以及队伍成员是否超出上限
        TeamEntity teamEntity = teamMapper.selectById(teamId);
        if(Objects.isNull(teamEntity)){
            throw (new TeamNameExistException());
        }
        if(userInfo.getRole()[teamEntity.getCompetition()]!=0){
            throw (new NoPermissionException());
        }
        Integer count = userAddMapper.countByTeamId(teamId);
        if(teamEntity.getCompetition() == 0&&count>=5){
            throw (new TeamMemberOverException());
        }
        if(teamEntity.getCompetition() == 1&&count>=1){
            throw (new TeamMemberOverException());
        }

        //2. 变更用户队伍信息，不变更角色
        userAddMapper.updateTeamIdByUserIdAndCompetition(userInfo.getUserId(), teamEntity.getCompetition(), teamId);
    }

    @Transactional
    @Override
    public void leaveTeam(int teamId, int competition) {
        UserInfo userInfo = UserUtils.getUser();
        // 1. 鉴权
        if(userInfo.getRole()[competition]!=1||!userInfo.getTeamId()[competition].equals(teamId)){
            throw (new NoPermissionException());
        }

        //2. 变更用户队伍信息并变更角色
        userAddMapper.updateTeamIdAndRoleByUserIdAndTeamId(userInfo.getUserId(), teamId, 0);

        //3. 逻辑删除该用户历史提交任务
        codeFeign.deleteByUserId(userInfo.getUserId());
        taskFeign.deleteByUserId(userInfo.getUserId());

        //4. 更新登录态信息
        userInfo.getRole()[competition] = 0;
        userInfo.getTeamId()[competition] = null;
        if(competition == 0){
            Iterator<Integer> it = userInfo.getPermissions().iterator();
            while(it.hasNext()){
                Integer x = it.next();
                if(x<5){
                    it.remove();
                }
            }
            Iterator<Integer> it1 = userInfo.getUndoPermissions().iterator();
            while(it1.hasNext()){
                Integer x = it1.next();
                if(x<5){
                    it1.remove();
                }
            }
        }else{
            Iterator<Integer> it = userInfo.getPermissions().iterator();
            while(it.hasNext()){
                Integer x = it.next();
                if(x==5){
                    it.remove();
                }
            }
            Iterator<Integer> it1 = userInfo.getUndoPermissions().iterator();
            while(it1.hasNext()){
                Integer x = it1.next();
                if(x==5){
                    it1.remove();
                }
            }
        }

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        redisTemplate.opsForValue().set(requestAttributes.getRequest().getHeader("token"), JSON.toJSONString(userInfo),1, TimeUnit.HOURS);
    }

    @Override
    public void auditTeamMeber(int userId, int competition) {
        UserInfo userInfo = UserUtils.getUser();
        //1. 权限校验
        if(userInfo.getRole()[competition]!=2){
            throw (new NoPermissionException());
        }

        //2. 校验要审核的队员是否是申请加入己方队伍
        Integer teamId = userAddMapper.selectTeamIdByUserIdAndCompetition(userId, competition);
        if(competition!=0||!userInfo.getTeamId()[0].equals(teamId)){
            throw (new NoPermissionException());
        }

        //3. 修改队员角色信息
        userAddMapper.updateRoleByUserIdAndCompetition(userId, competition, 1);
    }

    @Transactional
    @Override
    public void transferTeamLeader(int userId, int competition) {
        UserInfo userInfo = UserUtils.getUser();
        //1. 权限校验
        if(userInfo.getRole()[competition]!=2){
            throw (new NoPermissionException());
        }

        //2. 校验要移交的队员是否是已加入或正申请加入己方队伍
        Integer teamId = userAddMapper.selectTeamIdByUserIdAndCompetition(userId, competition);
        if(competition!=0||!userInfo.getTeamId()[0].equals(teamId)){
            throw (new NoPermissionException());
        }

        //3. 修改队员角色信息以及自己的角色信息
        userAddMapper.updateRoleByUserIdAndCompetition(userId, competition, 2);
        userAddMapper.updateRoleByUserIdAndCompetition(userInfo.getUserId(), competition, 1);

        //4. 更新登录态信息
        userInfo.getRole()[competition] = 1;
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        redisTemplate.opsForValue().set(requestAttributes.getRequest().getHeader("token"), JSON.toJSONString(userInfo),1, TimeUnit.HOURS);
    }

    @Transactional
    @Override
    public void deleteTeam(int competition) {
        UserInfo userInfo = UserUtils.getUser();

        //1. 权限校验
        if(userInfo.getRole()[competition]!=2){
            throw (new NoPermissionException());
        }

        //2. 校验队伍是不是只有一个人
        Integer count = userAddMapper.countByTeamId(userInfo.getTeamId()[competition]);
        if(count > 1){
            throw (new NoPermissionException());
        }

        //2.1. 逻辑删除该用户历史提交任务
        codeFeign.deleteByUserId(userInfo.getUserId());
        taskFeign.deleteByUserId(userInfo.getUserId());

        //3. 删除队伍信息，变更用户身份
        teamMapper.deleteById(userInfo.getTeamId()[competition]);
        teamRightMapper.deleteByTeamId(userInfo.getTeamId()[competition]);
        userAddMapper.updateTeamIdAndRoleByUserIdAndCompetition(userInfo.getUserId(), competition, null, 0);

        //4. 更新登录态信息
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        userInfo.getRole()[competition] = 0;
        userInfo.getTeamId()[competition] = null;
        if(competition == 0){
            Iterator<Integer> it = userInfo.getPermissions().iterator();
            while(it.hasNext()){
                Integer x = it.next();
                if(x<5){
                    it.remove();
                }
            }
            Iterator<Integer> it1 = userInfo.getUndoPermissions().iterator();
            while(it1.hasNext()){
                Integer x = it1.next();
                if(x<5){
                    it1.remove();
                }
            }
        }else{
            Iterator<Integer> it = userInfo.getPermissions().iterator();
            while(it.hasNext()){
                Integer x = it.next();
                if(x==5){
                    it.remove();
                }
            }
            Iterator<Integer> it1 = userInfo.getUndoPermissions().iterator();
            while(it1.hasNext()){
                Integer x = it1.next();
                if(x==5){
                    it1.remove();
                }
            }
        }
        redisTemplate.opsForValue().set(requestAttributes.getRequest().getHeader("token"), JSON.toJSONString(userInfo),5, TimeUnit.HOURS);
    }

    @Override
    public void participate(int type, int competition) {
        UserInfo userInfo = UserUtils.getUser();
        //1. 鉴权
        if (userInfo.getRole()[competition]!= RoleEnum.TEAM_LEADER.getRole()){
            throw (new NoPermissionException());
        }
        if (userInfo.getPermissions().contains(type)||userInfo.getUndoPermissions().contains(type)){
            throw new RRException(BizCodeEnum.PARTICIPATED.getCode(), BizCodeEnum.PARTICIPATED.getMsg());
        }

        //2. 修改队伍权限表
        teamRightMapper.updateStatusByTeamIdAndRight(userInfo.getTeamId()[competition], type, 1);

        //3. 更新用户登录态权限
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        userInfo.getUndoPermissions().add(type);
        redisTemplate.opsForValue().set(requestAttributes.getRequest().getHeader("token"), JSON.toJSONString(userInfo),5, TimeUnit.HOURS);
    }

    @Override
    public List<MemberInfoVo> getTeamMembers(Integer competition) {
        UserInfo userInfo = UserUtils.getUser();
        return teamMapper.selectByIdAndCompetition(userInfo.getTeamId()[competition], competition);
    }

    @Override
    public String getTeamName(int teamId) {
        return teamMapper.selectTeamNameByTeamId(teamId);
    }

    @Override
    public List<TeamEntity> getTeamInfo(int competition, int type) {
        return teamMapper.getTeamInfo(competition, type);
    }

    @Override
    public List<MemberInfoVo> getTeamMembersByTeamId(int competition, int teamId) {
        return teamMapper.selectByIdAndCompetition(teamId, competition);
    }

    @Override
    public void auditTeam(int teamId, int type, int status) {
        teamRightMapper.updateStatusByTeamIdAndRight(teamId, type, status);
    }

    private void checkTeamNameUnique(String teamName) {
        Integer count = teamMapper.countByTeamName(teamName);
        if(count>0){
            throw(new TeamNameExistException()) ;
        }
    }
}
