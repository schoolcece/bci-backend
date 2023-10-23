package com.hcc.log.mapper;

import com.hcc.log.entity.LogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.log.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日志表
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-10-06 12:53:42
 */
@Mapper
public interface LogMapper extends BaseMapper<LogEntity> {
	
}
