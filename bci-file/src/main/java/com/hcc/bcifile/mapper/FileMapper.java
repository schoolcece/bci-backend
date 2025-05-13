package com.hcc.bcifile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.entity.FileDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<FileDO> {
}
