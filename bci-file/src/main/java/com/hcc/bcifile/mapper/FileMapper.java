package com.hcc.bcifile.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.entity.FileDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FileMapper extends BaseMapper<FileDO> {
    List<FileDO> selectPageByParadigm(@Param("paradigmId") int paradigmId, @Param("index")Integer index, @Param("pageNum")int pageNum);
}
