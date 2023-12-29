package com.hcc.bciauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hcc.bciauth.feign.CompetitionFeign;
import com.hcc.common.constant.CustomConstants;
import com.hcc.bciauth.mapper.TeamMapper;
import com.hcc.bciauth.mapper.UserTeamMapper;
import com.hcc.common.model.entity.ApplicationDO;
import com.hcc.common.model.entity.TeamDO;
import com.hcc.common.model.entity.UserTeamDO;
import com.hcc.common.model.param.CreateTeamParam;
import com.hcc.bciauth.service.TeamService;
import com.hcc.common.component.RedisComponent;
import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import com.hcc.common.model.bo.UserInfoBO;
import com.hcc.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * Description: 服务层 队伍相关操作实现
 *
 * @Author: hcc
 * @Date: 2023/12/5
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, TeamDO> implements TeamService {

    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private UserTeamMapper userTeamMapper;
    @Autowired
    private CompetitionFeign competitionFeign;

    @Override
    @Transactional
    public void createTeam(CreateTeamParam createTeamParam) {
        UserInfoBO user = UserUtils.getUser();
        // 1. 鉴权 （用户在该范式下未创建队伍且未加入任何队伍）
        UserInfoBO.TeamInfo teamInfo = Optional.ofNullable(user.getTeamInfoMap())
                .orElseThrow(() -> new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg()))
                .get(createTeamParam.getEventId());
        if (teamInfo != null && teamInfo.getStatus() == CustomConstants.UserTeamRelationStatus.APPROVED) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }

        // 2. 检查队名唯一性
        checkTeamNameUnique(createTeamParam.getTeamName(), createTeamParam.getEventId());

        // 3. 移除可能存在的同赛事历史入队申请, 新增队伍信息和用户队伍关联信息
        if (!Objects.isNull(teamInfo)) {
            userTeamMapper.delete(new QueryWrapper<UserTeamDO>().eq("user_id", user.getUserId()).eq("team_id", teamInfo.getTeamId()));
        }
        TeamDO teamDO = TeamDO.builder()
                .teamName(createTeamParam.getTeamName())
                .university(createTeamParam.getUniversity())
                .eventId(createTeamParam.getEventId())
                .instructor(createTeamParam.getInstructor())
                .leaderId(user.getUserId())
                .build();
        teamMapper.insert(teamDO);
        UserTeamDO userTeamDO = UserTeamDO.builder()
                .status(CustomConstants.UserTeamRelationStatus.APPROVED)
                .teamId(teamDO.getId())
                .userId(user.getUserId())
                .role(CustomConstants.UserTeamRelationRole.LEADER)
                .build();
        userTeamMapper.insert(userTeamDO);

        // 4. 更新登录态缓存信息
        user.getTeamInfoMap().put(createTeamParam.getEventId(), new UserInfoBO.TeamInfo(teamDO.getId(), CustomConstants.UserTeamRelationRole.LEADER, CustomConstants.UserTeamRelationStatus.APPROVED));
        redisComponent.setObject(redisComponent.getString(String.valueOf(user.getUserId())), user, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
        redisComponent.expireForString(String.valueOf(user.getUserId()), CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
    }

    @Transactional
    @Override
    public void joinTeam(int teamId) {
        UserInfoBO user = UserUtils.getUser();
        // 1. 鉴权 （申请加入的team是否存在， 用户是否符合加队条件， 队伍成员是否超过上限）
        TeamDO teamDO = teamMapper.selectById(teamId);
        if (Objects.isNull(teamDO)) {
            throw new RTException(ErrorCodeEnum.TEAM_NOT_EXIST.getCode(), ErrorCodeEnum.TEAM_NOT_EXIST.getMsg());
        }
        UserInfoBO.TeamInfo teamInfo = Optional.ofNullable(user.getTeamInfoMap()).orElseThrow(() ->
                new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg())
        ).get(teamDO.getEventId());
        if (!Objects.isNull(teamInfo) && teamInfo.getStatus() == CustomConstants.UserTeamRelationStatus.APPROVED) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }
        checkTeamMemberOver(teamId);

        // 2. 清除未被通过的历史入队申请新增新的入队申请
        if (!Objects.isNull(teamInfo)) {
            userTeamMapper.delete(new QueryWrapper<UserTeamDO>().eq("user_id", user.getUserId()).eq("team_id", teamInfo.getTeamId()));
        }
        UserTeamDO userTeam = UserTeamDO.builder()
                .userId(user.getUserId())
                .teamId(teamId)
                .role(CustomConstants.UserTeamRelationRole.MEMBER)
                .status(CustomConstants.UserTeamRelationStatus.PENDING)
                .build();
        userTeamMapper.insert(userTeam);
    }

    @Override
    public void captainReview(int userId, int event) {
        UserInfoBO user = UserUtils.getUser();
        // 1. 鉴权（是否为队长）
        UserInfoBO.TeamInfo teamInfo = Optional.ofNullable(user.getTeamInfoMap()).
                orElseThrow(() -> new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg()))
                .get(event);
        if (Objects.isNull(teamInfo) || teamInfo.getRole() != CustomConstants.UserTeamRelationRole.LEADER) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }

        // 2. 更改用户申请入队的状态为已审核
        userTeamMapper.updateStatusByUserIdAndTeamId(userId, teamInfo.getTeamId(), CustomConstants.UserTeamRelationStatus.APPROVED);

        // 3. todo： 如果被审核人已登录，更新被审核人的登录态缓存
    }

    @Transactional
    @Override
    public void transferCaptain(int userId, int event) {
        UserInfoBO user = UserUtils.getUser();
        // 1. 鉴权（是否为队长）
        UserInfoBO.TeamInfo teamInfo = Optional.ofNullable(user.getTeamInfoMap()).
                orElseThrow(() -> new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg()))
                .get(event);
        if (Objects.isNull(teamInfo) || teamInfo.getRole() != CustomConstants.UserTeamRelationRole.LEADER) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }

        // 2. 变更用户的队伍角色
        userTeamMapper.updateRoleByUserIdAndTeamIdWithStatus(userId, teamInfo.getTeamId(), CustomConstants.UserTeamRelationRole.LEADER, CustomConstants.UserTeamRelationStatus.APPROVED);
        userTeamMapper.updateRoleByUserIdAndTeamIdWithStatus(user.getUserId(), teamInfo.getTeamId(), CustomConstants.UserTeamRelationRole.MEMBER, CustomConstants.UserTeamRelationStatus.APPROVED);

        // 3. 更新用户的登录态缓存
        user.getTeamInfoMap().get(event).setRole(CustomConstants.UserTeamRelationRole.MEMBER);
        redisComponent.setObject(redisComponent.getString(String.valueOf(user.getUserId())), user, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
        redisComponent.expireForString(String.valueOf(user.getUserId()), CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);

        // 4. todo: 如果新队长已登录， 更新新队长的登录态缓存
    }

    @Override
    public void leaveTeam(int event) {
        UserInfoBO user = UserUtils.getUser();
        // 1. 获取用户对应赛事的队伍id
        UserInfoBO.TeamInfo teamInfo = Optional.ofNullable(user.getTeamInfoMap()).
                orElseThrow(() -> new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg()))
                .get(event);
        if (Objects.isNull(teamInfo) || teamInfo.getStatus() == CustomConstants.UserTeamRelationStatus.PENDING) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }

        // 2. 解除用户和对应赛事下队伍的关系 队长不允许离队
        if (teamInfo.getRole() == CustomConstants.UserTeamRelationRole.LEADER) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }
        userTeamMapper.delete(new QueryWrapper<UserTeamDO>().eq("user_id", user.getUserId()).eq("team_id", teamInfo.getTeamId()));

        // 3. todo：逻辑删除该用户的所有历史提交代码和任务（涉及到远程调用，需要考虑整体操作的原子性）

        // 4. 更新登录态缓存
        user.getTeamInfoMap().remove(event);
        redisComponent.setObject(redisComponent.getString(String.valueOf(user.getUserId())), user, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
        redisComponent.expireForString(String.valueOf(user.getUserId()), CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
    }

    @Override
    @Transactional
    public void disbandTeam(int event) {
        UserInfoBO user = UserUtils.getUser();
        // 1. 鉴权（是否为队长）
        UserInfoBO.TeamInfo teamInfo = Optional.ofNullable(user.getTeamInfoMap()).
                orElseThrow(() -> new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg()))
                .get(event);
        if (Objects.isNull(teamInfo) || teamInfo.getRole() != CustomConstants.UserTeamRelationRole.LEADER) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }

        // 2. 确认队伍是否不包含其他队员，如果有其他队员不允许注销
        Long count = userTeamMapper.selectCount(new QueryWrapper<UserTeamDO>()
                .eq("team_id", teamInfo.getTeamId())
                .eq("status", CustomConstants.UserTeamRelationStatus.APPROVED));
        if (count > 1) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }

        // 3. todo：逻辑删除该用户的所有历史提交代码和任务（涉及到远程调用，需要考虑整体操作的原子性）

        // 4. 逻辑删除队伍信息，接触队伍与用户关系
        teamMapper.updateStatusByTeamId(teamInfo.getTeamId(), CustomConstants.TeamStatus.DISBAND);
        userTeamMapper.delete(new QueryWrapper<UserTeamDO>().eq("team_id", teamInfo.getTeamId()));

        // 5. 更新用户登录态缓存
        user.getTeamInfoMap().remove(event);
        redisComponent.setObject(redisComponent.getString(String.valueOf(user.getUserId())), user, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
        redisComponent.expireForString(String.valueOf(user.getUserId()), CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
    }

    @Override
    public void registerForCompetition(int event, int paradigm) {
        UserInfoBO user = UserUtils.getUser();
        // 1. 鉴权（是否为队长）
        UserInfoBO.TeamInfo teamInfo = Optional.ofNullable(user.getTeamInfoMap()).
                orElseThrow(() -> new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg()))
                .get(event);
        if (Objects.isNull(teamInfo) || teamInfo.getRole() != CustomConstants.UserTeamRelationRole.LEADER) {
            throw new RTException(ErrorCodeEnum.NO_PERMISSION.getCode(), ErrorCodeEnum.NO_PERMISSION.getMsg());
        }

        // 2. 查询当前时刻是否允许报名（是否在比赛期间）
        if (!competitionFeign.inTime(new Date(), event)) {
            throw new RTException(ErrorCodeEnum.EVENT_NOT_IN_TIME.getCode(), ErrorCodeEnum.EVENT_NOT_IN_TIME.getMsg());
        }

        // 3. 如果已有报名记录不操作，否则新增报名记录
        ApplicationDO applicationDO = ApplicationDO.builder()
                .paradigmId(paradigm)
                .teamId(teamInfo.getTeamId())
                .updateUser(user.getUserId()).build();
        teamMapper.insertApplicationIfNotExist(applicationDO);

        // 4. 更新登录态缓存
        user.getPermissions().putIfAbsent(event, CustomConstants.ApplicationStatus.PENDING);
        redisComponent.setObject(redisComponent.getString(String.valueOf(user.getUserId())), user, CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
        redisComponent.expireForString(String.valueOf(user.getUserId()), CustomConstants.TokenConfig.TIMEOUT, CustomConstants.TokenConfig.TIME_UNIT);
    }

    private void checkTeamMemberOver(int teamId) {
        Long count = userTeamMapper.selectCount(new QueryWrapper<UserTeamDO>().eq("team_id", teamId));
        if (count > 0) {
            throw new RTException(ErrorCodeEnum.TEAM_MEMBER_OVER.getCode(), ErrorCodeEnum.TEAM_MEMBER_OVER.getMsg());
        }
    }

    private void checkTeamNameUnique(String teamName, int eventId) {
        Long count = teamMapper.selectCount(new QueryWrapper<TeamDO>().eq("team_name", teamName).eq("event_id", eventId));
        if (count > 0) {
            throw new RTException(ErrorCodeEnum.TEAM_NAME_EXIST.getCode(), ErrorCodeEnum.TEAM_NAME_EXIST.getMsg());
        }
    }
}
