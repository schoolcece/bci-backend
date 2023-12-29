package com.hcc.bcicompetition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.entity.EventDO;
import com.hcc.common.model.entity.ParadigmDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: 范式信息mapper层
 *
 * @Author: hcc
 * @Date: 2023/12/28
 */
@Mapper
public interface ParadigmMapper extends BaseMapper<ParadigmDO> {
}
