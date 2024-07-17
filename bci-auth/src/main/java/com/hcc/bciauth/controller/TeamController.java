package com.hcc.bciauth.controller;

import com.hcc.common.model.param.CreateTeamParam;
import com.hcc.bciauth.service.TeamService;
import com.hcc.common.annotation.Loggable;
import com.hcc.common.model.R;
import com.hcc.common.utils.UserUtils;
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

    /**
     * 获取队伍信息
     * @param event
     * @param curPage
     * @param teamName
     * @return
     */
    @GetMapping("/getAllTeamInfos")
    public R getAllTeamInfos(@RequestParam int event,
                             @RequestParam(value = "curPage", defaultValue = "1") int curPage,
                             @RequestParam(value = "teamName", defaultValue = "") String teamName) {
        return R.ok().put("data", teamService.getAllTeamInfos(event, curPage, teamName));
    }

    /**
     * 获取队伍成员信息
     * @param eventId
     * @return
     */
    @GetMapping("/getTeamMembers")
    public R getTeamMembers(@RequestParam int eventId) {
        return R.ok().put("data", teamService.getTeamMembers(eventId));
    }

    /**
     * 获取队员范式报名队伍信息
     * @param event
     * @param paradigm
     * @return
     */
    @GetMapping("/getTeamInfo")
    public R getTeamInfo(@RequestParam("event") int event, @RequestParam("paradigm") int paradigm) {
        return R.ok().put("data", teamService.getTeamInfo(event, paradigm));
    }

    /**
     * 根据id获取队伍成员信息
     * @param teamId
     * @return
     */
    @GetMapping("/getTeamMembersById")
    public R getTeamMembersById(@RequestParam int teamId) {
        return R.ok().put("data", teamService.getTeamMembersById(teamId));
    }

    /**
     * 审核队伍参赛申请接口 post请求
     * @param teamId
     * @param paradigm
     * @param status
     * @return
     */
    @PostMapping("/auditTeam")
    public R auditTeam(@RequestParam int teamId, @RequestParam int paradigm, @RequestParam int status){
        teamService.auditTeam(teamId, paradigm, status);
        return R.ok();
    }

    /**
     * 报名系统队伍成绩展示接口 get请求
     * @param event
     * @param paradigm
     * @return
     */
    @GetMapping("/listTeamByParadigm")
    public R listTeamByParadigm(@RequestParam int event, @RequestParam int paradigm){
        return R.ok().put("data", teamService.listTeamByParadigm(event, paradigm));
    }

    /**
     * 内部调用接口 根据队伍ID获取队名
     * @param teamId
     * @return
     */
    @GetMapping("/getTeamNameById")
    String getTeamName(@RequestParam("teamId")int teamId){
        return teamService.getTeamName(teamId);
    }
}
