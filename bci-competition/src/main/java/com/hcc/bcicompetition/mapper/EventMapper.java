package com.hcc.bcicompetition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.entity.EventDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: 赛事信息mapper层
 *
 * @Author: hcc
 * @Date: 2023/12/13
 */
@Mapper
public interface EventMapper extends BaseMapper<EventDO> {
}
