package com.hcc.scheduler.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hcc.common.exception.BizCodeEnum;
import com.hcc.common.exception.RRException;
import com.hcc.common.vo.NodeVo;
import com.hcc.scheduler.entity.ComputeNodeEntity;
import com.hcc.scheduler.mapper.ComputeNodeMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ComputeNode")
public class ComputeNodeController {

    ComputeNodeMapper computeNodeMapper;

    RedissonClient redissonClient;

    @Autowired
    public ComputeNodeController(ComputeNodeMapper computeNodeMapper, RedissonClient redissonClient){
        this.computeNodeMapper = computeNodeMapper;
        this.redissonClient = redissonClient;
    }

    /**
     * 获取一个计算资源池中当前最空闲的计算节点
     * @return
     */
    @GetMapping("/getOne")
    public NodeVo getOne(){
        RLock lock = redissonClient.getLock("scheduler");
        lock.lock();
        try{
            List<ComputeNodeEntity> nodes = computeNodeMapper.selectList(new QueryWrapper<ComputeNodeEntity>().eq("status", 1))
                    .stream().filter(item -> item.getRunningTasks() < item.getMaxTasks())
                    .sorted((a, b) -> (b.getMaxTasks()-b.getRunningTasks()) - (a.getMaxTasks()-a.getRunningTasks())).collect(Collectors.toList());

            if(nodes.size() == 0){
                return null;
            }else{
                ComputeNodeEntity nodeSelected = nodes.get(0);
                computeNodeMapper.addRunningTasksById(nodeSelected.getNodeId());
                NodeVo nodeVo = new NodeVo();
                nodeVo.setNodeId(nodeSelected.getNodeId());
                nodeVo.setNodeIp(nodeSelected.getIp());
                return nodeVo;
            }
        }finally {
            lock.unlock();
        }
    }

    /**
     * 更新计算节点状态
     * @param nodeId
     */
    @PostMapping("/updateNode")
    public void updateNode(@RequestParam int nodeId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String info = (String) authentication.getPrincipal();
        if (!info.equals("innerVisit")){
            throw new RRException(BizCodeEnum.INNER_REJECT.getCode(), BizCodeEnum.INNER_REJECT.getMsg());
        }
        computeNodeMapper.reduceRunningTasksById(nodeId);
    }

}
