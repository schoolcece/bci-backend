package com.hcc.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.task.entity.CollectionTaskEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务表
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
@Mapper
public interface CollectionTaskMapper extends BaseMapper<CollectionTaskEntity> {
	
}
