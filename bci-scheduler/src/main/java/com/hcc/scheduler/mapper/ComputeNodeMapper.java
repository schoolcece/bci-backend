package com.hcc.scheduler.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.scheduler.entity.ComputeNodeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-10-25 21:56:14
 */
@Mapper
public interface ComputeNodeMapper extends BaseMapper<ComputeNodeEntity> {

    void addRunningTasksById(int nodeId);

    void reduceRunningTasksById(int nodeId);
}
