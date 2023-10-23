package com.hcc.bciauthserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hcc.bciauthserver.entity.*;
import com.hcc.bciauthserver.service.TeamService;
import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.exception.RRException;
import com.hcc.common.utils.R;
import com.hcc.common.utils.UserUtils;
import com.hcc.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RefreshScope
@RestController
@RequestMapping("/auth")
public class TeamController {

    @Value("${pythonTeamOperate}")
    private boolean pythonTeamOperate;

    @Value("${matlabTeamOperate}")
    private boolean matlabTeamOperate;

    private TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

    /**
     * 创建赛队接口（只对普通用户（role=0）开放权限） post请求
     * @param teamInfoVo
     * @return R
     */
    @PostMapping("/buildTeam")
    public R buildTeam(@RequestBody TeamInfoVo teamInfoVo){
        if (teamInfoVo.getCompetition()==0&&!pythonTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        if (teamInfoVo.getCompetition()==1&&!matlabTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        teamService.buildTeam(teamInfoVo);
        return R.ok();
    }

    /**
     * 加入赛队接口 （只对普通用户（role=0）开放权限） post请求
     * @param teamId
     * @return R
     */
    @PostMapping("/joinTeam")
    public R joinTeam(@RequestParam int teamId){
        if (!pythonTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        teamService.joinTeam(teamId);
        return R.ok();
    }

    /**
     * 离开队伍接口 （只对队员（role=1）开放权限） post请求
     * @param teamId
     * @param competition
     * @return
     */
    @PostMapping("/leaveTeam")
    public R leaveTeam(@RequestParam int teamId, @RequestParam int competition){
        if (competition==0&&!pythonTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        if (competition==1&&!matlabTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        teamService.leaveTeam(teamId, competition);
        return R.ok();
    }

    /**
     * 审核入队申请接口 （只对队长（role=2）开放权限） post请求
     * @param userId
     * @param competition
     * @return
     */
    @PostMapping("/auditTeamMember")
    public R auditTeamMember(@RequestParam(value = "userId") int userId,
                             @RequestParam(value = "competition") int competition){
        if (competition==0&&!pythonTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        if (competition==1&&!matlabTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        teamService.auditTeamMeber(userId, competition);
        return R.ok();
    }

    /**
     * 移交队长权限接口 （只对队长（role=2）开放权限） post请求
     * @param userId
     * @param competition
     * @return
     */
    @PostMapping("/transferTeamLeader")
    public R transferTeamLeader(@RequestParam int userId, @RequestParam int competition){
        if (competition==0&&!pythonTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        if (competition==1&&!matlabTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        teamService.transferTeamLeader(userId, competition);
        return R.ok();
    }

    /**
     * 注销队伍接口 （只对队长（role=2）开放权限） post请求
     * @param competition
     * @return
     */
    @PostMapping("/deleteTeam")
    public R deleteTeam(@RequestParam int competition){
        if (competition==0&&!pythonTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        if (competition==1&&!matlabTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        teamService.deleteTeam(competition);
        return R.ok();
    }

    /**
     * 报名参赛接口 （只对队长（role=2）开放权限） post请求
     * @param type
     * @param competition
     * @return
     */
    @PostMapping("/participate")
    public R participate(@RequestParam int type, @RequestParam int competition){
        if (competition==0&&!pythonTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        if (competition==1&&!matlabTeamOperate){
            throw new RRException(BizCodeEnum.NOT_PERMITED.getCode(), BizCodeEnum.NOT_PERMITED.getMsg());
        }
        teamService.participate(type, competition);
        return R.ok();
    }

    /** todo: 分页查询 关键字查询
     * 获取所有队伍信息 get请求
     * @param competition
     * @return
     */
    @GetMapping("/getAllTeamInfos")
    public R getAllTeamInfos(@RequestParam Integer competition,
                             @RequestParam(value = "curPage", defaultValue = "1") int curPage,
                             @RequestParam(value = "teamName", defaultValue = "") String teamName){
//        List<TeamEntity> teamEntities = teamService.getBaseMapper()
//                .selectList(new QueryWrapper<TeamEntity>().eq("competition", competition));
        Page<TeamEntity> page = teamService.query().eq("competition", competition).like("team_name", teamName).page(new Page<>(curPage, 10));
        return R.ok().put("data", page.getRecords()).put("total", page.getTotal());
    }

    /**
     * 获取队伍所有成员信息 get请求
     * @param competition
     * @return
     */
    @GetMapping("/getTeamMembers")
    public R getTeamMembers(@RequestParam(defaultValue = "0") Integer competition){
        List<MemberInfoVo> memberInfoVos = teamService.getTeamMembers(competition);
        return R.ok().put("data", memberInfoVos);
    }

    /**
     * task调用接口， 获取队伍名 get请求
     * @param teamId
     * @return
     */
    @GetMapping("/getTeamName")
    public String getTeamName(@RequestParam("teamId") int teamId){
        return teamService.getTeamName(teamId);
    };


    ////////////////////////////////////////赛题负责人接口/////////////////////////////////////////////////////////////

    /**
     * 获取报名某一范式的所有队伍信息 get请求
     * @param competition
     * @param type
     * @return
     */
    @GetMapping("/getTeamInfo")
    public R getTeamInfo(@RequestParam("competition") int competition, @RequestParam("type") int type){
        //权限校验
        UserInfo userInfo = UserUtils.getUser();
        if(userInfo.getRole()[competition]<=2){
            throw new RRException(BizCodeEnum.NO_PERMISSION.getCode(), BizCodeEnum.NO_PERMISSION.getMsg());
        }
        List<TeamEntity> teamEntities = teamService.getTeamInfo(competition, type);
        return R.ok().put("data", teamEntities);
    }

    /**
     * 获取队伍所有成员信息 get请求
     * @param competition
     * @return
     */
    @GetMapping("/getTeamMembersByTeamId")
    public R getTeamMembersByTeamId(@RequestParam(defaultValue = "0") int competition, @RequestParam int teamId){
        //权限校验
        UserInfo userInfo = UserUtils.getUser();
        if(userInfo.getRole()[competition]<=2){
            throw new RRException(BizCodeEnum.NO_PERMISSION.getCode(), BizCodeEnum.NO_PERMISSION.getMsg());
        }
        List<MemberInfoVo> memberInfoVos = teamService.getTeamMembersByTeamId(competition, teamId);
        return R.ok().put("data", memberInfoVos);
    }

    /**
     * 审核队伍参赛申请接口 post请求
     * @param teamId
     * @param type
     * @param status
     * @return
     */
    @PostMapping("/auditTeam")
    public R auditTeam(@RequestParam int teamId, @RequestParam int type,@RequestParam int status){
        //权限校验
        UserInfo userInfo = UserUtils.getUser();
        if((type<=4&&userInfo.getRole()[0]<=2)||(type==5&&userInfo.getRole()[1]<=2)){
            throw new RRException(BizCodeEnum.NO_PERMISSION.getCode(), BizCodeEnum.NO_PERMISSION.getMsg());
        }
        teamService.auditTeam(teamId, type, status);
        return R.ok();
    }

}
