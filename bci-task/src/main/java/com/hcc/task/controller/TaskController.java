package com.hcc.task.controller;

import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.utils.R;
import com.hcc.common.utils.UserUtils;
import com.hcc.common.vo.UserInfo;
import com.hcc.task.entity.TaskEntity;
import com.hcc.task.exception.RRException;
import com.hcc.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RefreshScope
@RequestMapping("/task")
public class TaskController {

    TaskService taskService;

    RedisTemplate<String, Long> redisTemplateForCount;

    StringRedisTemplate redisTemplate;

    @Autowired
    public TaskController(TaskService taskService, RedisTemplate<String, Long> redisTemplateForCount, StringRedisTemplate redisTemplate){
        this.taskService = taskService;
        this.redisTemplateForCount = redisTemplateForCount;
        this.redisTemplate = redisTemplate;
    }

    @Value("${commit.maxCount}")
    private int maxCount;
    @Value("${container.maxTime}")
    private int[] maxTime;

    /**
     * 提交任务接口
     * @param codeId
     * @param type
     * @param competition
     * @return
     */
    @PreAuthorize("@JER.hasJoin()")
    @PostMapping("/compute")
    public R compute(@RequestParam("type") int type, @RequestParam("codeId") int codeId
                     , @RequestParam("competition") int competition){
        UserInfo userInfo = UserUtils.getUser();
        //0. 检查是否有正在运行的任务
        String countKey = "teamId"+ userInfo.getTeamId()[competition] + "type" + type;
        String taskingKey = countKey + "on";
        if (!redisTemplate.opsForValue().setIfAbsent(taskingKey, "", maxTime[type]+100, TimeUnit.SECONDS)){
            throw new RRException(BizCodeEnum.HAS_TASK_RUNNING.getCode(), BizCodeEnum.HAS_TASK_RUNNING.getMsg());
        }
//        redisTemplate.opsForValue().set(taskingKey, "", maxTime[type], TimeUnit.SECONDS);
        //1. 提交次数检查&代码是否属于用户检查
        //1.1 检查当日提交次数限制
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.MILLISECOND,0);
        //1.2 当前时间与晚上十二点的秒差
        Long timeOut = (calendar.getTimeInMillis()-System.currentTimeMillis()) / 1000;
        redisTemplateForCount.opsForValue().setIfAbsent(countKey,0L, timeOut, TimeUnit.SECONDS);
        Long count = redisTemplateForCount.opsForValue().get(countKey);
        if(count >= maxCount){
            redisTemplate.delete(taskingKey);
            throw new RRException(BizCodeEnum.COMMIT_OVER_TIMES.getCode(), BizCodeEnum.COMMIT_OVER_TIMES.getMsg());
        }
        try{
            taskService.taskInitialization(codeId, type, competition);
        }catch (Exception e){
            redisTemplate.delete(taskingKey);
            throw e;
        }
        return R.ok();
    }

//    @RequestMapping("/computeOnline")
//    public R computeOnline(@RequestParam("codeId") int codeId,
//                     @RequestParam("type") int type,
//                     HttpServletRequest request){
//        String token = request.getHeader("token");
//        if(StringUtils.isEmpty(token)){
//            return R.error(BizCodeEnume.IDENTITY_LAPSE.getCode(), BizCodeEnume.IDENTITY_LAPSE.getMsg());
//        }
//        String userInfoStr = redisTemplate.opsForValue().get(token);
//        if(StringUtils.isEmpty(userInfoStr)){
//            return R.error(BizCodeEnume.IDENTITY_LAPSE.getCode(), BizCodeEnume.IDENTITY_LAPSE.getMsg());
//        }
//        UserInfo userInfo = JSON.parseObject(userInfoStr,new TypeReference<UserInfo>(){});
//        taskService.computeOnline(codeId,userInfo);
//        return R.ok();
//    }

    /**
     * 删除任务接口 post请求
     * @param codeId
     */
    @PostMapping("/deleteByCodeId")
    void deleteByCodeId(@RequestParam("codeId") int codeId){
        List<Integer> taskIds = taskService.getTaskIds(codeId);
        if(taskIds.size() > 0){
            taskService.removeByIds(taskIds);
        }
    }

//    @RequestMapping("/cancel")
//    public R cancel(@RequestParam int codeId, HttpServletRequest request){
//        String token = request.getHeader("token");
//        if(StringUtils.isEmpty(token)){
//            return R.error(BizCodeEnume.IDENTITY_LAPSE.getCode(), BizCodeEnume.IDENTITY_LAPSE.getMsg());
//        }
//        String userInfoStr = redisTemplate.opsForValue().get(token);
//        if(StringUtils.isEmpty(userInfoStr)){
//            return R.error(BizCodeEnume.IDENTITY_LAPSE.getCode(), BizCodeEnume.IDENTITY_LAPSE.getMsg());
//        }
//        UserInfo userInfo = JSON.parseObject(userInfoStr,new TypeReference<UserInfo>(){});
//        taskService.cancel(codeId, userInfo.getUserId());
//        return R.ok();
//    }

    /**
     * 更新任务成绩接口 post请求
     * @param taskId
     * @param score
     */
    @PostMapping("/updateScore")
    public R updateScore(@RequestParam int taskId,@RequestParam float score){
        TaskEntity task = new TaskEntity();
        task.setTaskId(taskId);
        task.setScore(score);
        taskService.updateById(task);
        return R.ok();
    }

//    @Cacheable(value = {"code"},key = "#root.methodName")
    @PreAuthorize("@JER.hasJoin()")
    @RequestMapping("/rank")
    public R getScoresRank(@RequestParam("type") int type, @RequestParam("dataset") int dataset,
                           @RequestParam(value = "curPage", defaultValue = "1") int curPage){
        return taskService.getScoresRank(type, dataset, curPage);
    }
    @RequestMapping("/record")
    public R getScoreRecord(@RequestParam("teamId") int teamId, @RequestParam("type") int type,
                            @RequestParam("dataset") int dataset, @RequestParam(value = "curPage", defaultValue = "1") int curPage){
//        List<RecordVo> record = taskService.getScoresRecord(teamId,type,dataset, curPage);
        return taskService.getScoresRecord(teamId,type,dataset, curPage);
    }

    /**
     * 离开队伍或注销队伍调用接口 post请求
     */
    @PostMapping("/deleteByUserId")
    public R deleteByUserId(@RequestParam("userId") int userId){
        taskService.deleteByUserId(userId);
        return R.ok();
    };
}
