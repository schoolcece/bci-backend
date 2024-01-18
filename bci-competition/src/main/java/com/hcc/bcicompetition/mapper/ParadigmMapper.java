package com.hcc.bcicompetition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.dto.ParadigmDTO;
import com.hcc.common.model.entity.EventDO;
import com.hcc.common.model.entity.ParadigmDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Description: 范式信息mapper层
 *
 * @Author: hcc
 * @Date: 2023/12/28
 */
@Mapper
public interface ParadigmMapper extends BaseMapper<ParadigmDO> {
    ParadigmDTO selectByParadigmId(@Param("paradigmId") int paradigmId);
}
