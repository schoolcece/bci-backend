package com.hcc.bcitask.controller;

import com.hcc.bcitask.mapper.CommonMapper;
import com.hcc.bcitask.service.TaskService;
import com.hcc.common.annotation.Loggable;
import com.hcc.common.component.RedisComponent;
import com.hcc.common.model.R;
import com.hcc.common.model.bo.UserInfoBO;
import com.hcc.common.utils.KeyConvertUtils;
import com.hcc.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description: controller层 任务相关操作
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private RedisComponent redisComponent;

    /**
     * 创建任务接口
     * @param paradigmId
     * @param codeId
     * @return
     */
    @Loggable("创建任务")
    @PostMapping("/createTask")
    public R createTask(@RequestParam("paradigmId") int paradigmId, @RequestParam("codeId") int codeId,
                        @RequestParam("taskName") String taskName, @RequestParam("taskType") int taskType) {
        taskService.createTask(paradigmId, codeId, taskName, taskType);
        return R.ok();
    }

    /**
     * 执行任务接口
     * @param taskId
     * @return
     */
    @Loggable("执行任务")
    @PostMapping("/execTask")
    public R execTask(@RequestParam("taskId") int taskId) {
        taskService.execTask(taskId);
        return R.ok();
    }

    /**
     * 获取任务接口
     * @param paradigm
     * @return
     */
    @GetMapping("/getTask")
    public R getTask(@RequestParam("paradigm") int paradigm, @RequestParam(value = "curPage", defaultValue = "1") int curPage) {
        UserInfoBO user = UserUtils.getUser();
        return R.ok().put("data", taskService.getTask(paradigm, curPage)).put("running", redisComponent.hasKey(KeyConvertUtils.taskingKeyConvert(user.getTeamInfoMap().get(paradigm<=5?1:2).getTeamId(), paradigm)));
    }

    /**
     * 根据任务id逻辑删除任务接口
     * @param taskId
     * @return
     */
    @Loggable("删除任务")
    @PostMapping("/deleteTaskById")
    public R deleteTaskById(@RequestParam("taskId") int taskId) {
        taskService.deleteTaskById(taskId);
        return R.ok();
    }

    /**
     * 获取成绩排名接口
     * @param paradigmId
     * @param dataset
     * @param curPage
     * @return
     */
    @GetMapping("/rank")
    public R getScoreRank(@RequestParam("paradigmId") int paradigmId, @RequestParam("dataset") int dataset,
                          @RequestParam(value = "curPage", defaultValue = "1") int curPage) {
        return taskService.rank(paradigmId, dataset, curPage);
    }

    /**
     * 获取指定队伍成绩记录接口
     * @param teamId
     * @param paradigm
     * @param dataset
     * @param curPage
     * @return
     */
    @GetMapping("/record")
    public R getScoreRecord(@RequestParam("teamId") int teamId, @RequestParam("paradigm") int paradigm,
                            @RequestParam("dataset") int dataset, @RequestParam(value = "curPage", defaultValue = "1") int curPage) {
        return taskService.record(teamId, paradigm, dataset, curPage);
    }

    /**
     * 获取任务对应日志
     * @param taskId
     * @return
     */
    @GetMapping("/getLog")
    public R getLog(@RequestParam("taskId") int taskId) {
        return R.ok().put("content", taskService.getLog(taskId));
    }

    /**
     * 创建决赛任务接口
     * @param
     * @return
     */
    @PostMapping("/createTaskForFinals")
    public R createTaskForFinals(@RequestParam("paradigmId") int paradigmId, @RequestParam("codeId") int codeId,
                                 @RequestParam("taskName") String taskName, @RequestParam("taskType") int taskType) {
        taskService.createTaskForFinals(paradigmId, codeId, taskName, taskType);
        return R.ok();
    }

    /**
     * 确认代码版本接口
     * @param
     * @return
     */
    @PostMapping("/confirmTask")
    public R confirmTask(@RequestParam("taskId") int taskId) {
        taskService.confirmTask(taskId);
        return R.ok();
    }

    @PostMapping("/cancelConfirm")
    public R cancelConfirm(@RequestParam("taskId") int taskId) {
        taskService.cancelConfirm(taskId);
        return R.ok();
    }

    /**
     * 决赛任务启动接口
     * @param
     * @return
     */
    @PostMapping("/execTaskForFinals")
    public R execTaskForFinals(@RequestParam("taskId") int taskId) {
        taskService.execTaskForFinals(taskId);
        return R.ok();
    }

    /**
     * 决赛所有任务启动接口
     * @param
     * @return
     */
    @PostMapping("/execAllTaskForFinals")
    public R execAllTaskForFinals(@RequestParam("paradigmId") int paradigmId) {
        taskService.execAllTaskForFinals(paradigmId);
        return R.ok();
    }

    @PostMapping("/stopAllTaskForFinals")
    public R stopAllTaskForFinals(@RequestParam("paradigmId") int paradigmId) {
        taskService.stopAllTaskForFinals(paradigmId);
        return R.ok();
    }

    @GetMapping("/getTaskForFinals")
    public R getTaskForFinals(@RequestParam("paradigm") int paradigm, @RequestParam(value = "curPage", defaultValue = "1") int curPage) {
        return R.ok().put("data", taskService.getTaskForFinals(paradigm, curPage));
    }


//======================================以下为内部调用接口==========================================================================================

    /**
     * 更新任务成绩接口 post请求
     * @param taskId
     * @param score
     */
    @PostMapping("/updateScore")
    public R updateScore(@RequestParam int taskId,@RequestParam float score){
        taskService.updateScoreById(taskId, score);
        return R.ok();
    }

    /**
     * 用户离队或注销队伍调用接口 逻辑删除用户提交任务
     * @param userId
     * @return
     */
    @PostMapping("/deleteTaskByUserId")
    public R deleteTaskByUserId(@RequestParam("userId") int userId) {
        return R.ok();
    }
}
