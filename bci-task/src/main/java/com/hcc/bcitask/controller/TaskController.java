package com.hcc.bcitask.controller;

import com.hcc.bcitask.service.TaskService;
import com.hcc.common.annotation.Loggable;
import com.hcc.common.model.R;
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
    public R getTask(@RequestParam("paradigm") int paradigm, @RequestParam(value = "current", defaultValue = "1") int curPage) {
        return R.ok().put("data", taskService.getTask(paradigm, curPage));
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
        return R.ok();
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
        return R.ok();
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
