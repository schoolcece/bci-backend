package com.hcc.bciauth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hcc.common.constant.CustomConstants;
import com.hcc.bciauth.mapper.TeamMapper;
import com.hcc.bciauth.mapper.UserTeamMapper;
import com.hcc.common.model.entity.TeamDO;
import com.hcc.common.model.entity.UserTeamDO;
import com.hcc.common.model.param.CreateTeamParam;
import com.hcc.bciauth.service.TeamService;
import com.hcc.common.component.RedisComponent;
import com.hcc.common.config.BCIConfig;
import com.hcc.common.enums.ErrorCodeEnum;
import com.hcc.common.exception.RTException;
import com.hcc.common.model.bo.UserInfoBO;
import com.hcc.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private BCIConfig.TokenConfig tokenConfig;

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
        checkTeamNameUnique(createTeamParam.getTeamName());

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
        redisComponent.setObject(redisComponent.getString(String.valueOf(user.getUserId())), user, tokenConfig.getTimeout(), tokenConfig.getTimeUnit());
    }


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

    private void checkTeamMemberOver(int teamId) {
        Long count = userTeamMapper.selectCount(new QueryWrapper<UserTeamDO>().eq("team_id", teamId));
        if (count > 0) {
            throw new RTException(ErrorCodeEnum.TEAM_MEMBER_OVER.getCode(), ErrorCodeEnum.TEAM_MEMBER_OVER.getMsg());
        }
    }

    private void checkTeamNameUnique(String teamName) {
        Long count = teamMapper.selectCount(new QueryWrapper<TeamDO>().eq("team_name", teamName));
        if (count > 0) {
            throw new RTException(ErrorCodeEnum.TEAM_NAME_EXIST.getCode(), ErrorCodeEnum.TEAM_NAME_EXIST.getMsg());
        }
    }
}
