package com.hcc.bciauth.controller;

import com.hcc.common.model.param.CreateTeamParam;
import com.hcc.bciauth.service.TeamService;
import com.hcc.common.annotation.Loggable;
import com.hcc.common.model.R;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description: controller层 队伍相关请求
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * 创建队伍接口
     * @param createTeamParam
     * @return
     */
    @Loggable("创建队伍")
    @PostMapping("/createTeam")
    public R createTeam(@RequestBody @Valid CreateTeamParam createTeamParam) {
        teamService.createTeam(createTeamParam);
        return R.ok();
    }

    /**
     * 申请加入队伍接口
     * @param teamId
     * @return
     */
    @Loggable("申请加入队伍")
    @PostMapping("/joinTeam")
    public R joinTeam(@RequestParam("teamId") int teamId) {
        teamService.joinTeam(teamId);
        return R.ok();
    }

    /**
     * 审核入队申请
     * @param userId
     * @param event
     * @return
     */
    @Loggable("审核入队申请")
    @PostMapping("/captainReview")
    public R captainReview(@RequestParam("userId") int userId,
                             @RequestParam("event") int event) {
        teamService.captainReview(userId, event);
        return R.ok();
    }

    /**
     * 移交队长
     * @param userId
     * @param event
     * @return
     */
    @Loggable("移交队长")
    @PostMapping("/transferCaptain")
    public R transferCaptain(@RequestParam("userId") int userId,
                             @RequestParam("event") int event) {
        teamService.transferCaptain(userId, event);
        return R.ok();
    }

    /**
     * 离队
     * @param event
     * @return
     */
    @Loggable("离开队伍")
    @PostMapping("/leaveTeam")
    public R leaveTeam(@RequestParam("event") int event) {
        teamService.leaveTeam(event);
        return R.ok();
    }

    /**
     * 注销队伍
     * @param event
     * @return
     */
    @Loggable("注销队伍")
    @PostMapping("/disbandTeam")
    public R disbandTeam(@RequestParam("event") int event) {
        teamService.disbandTeam(event);
        return R.ok();
    }

    /**
     * 报名参赛
     * @param event
     * @param paradigm
     * @return
     */
    @Loggable("报名参赛")
    @PostMapping("/registerForCompetition")
    public R registerForCompetition(@RequestParam("event") int event,
                                    @RequestParam("paradigm") int paradigm) {
        teamService.registerForCompetition(event, paradigm);
        return R.ok();
    }
}
