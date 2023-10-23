package com.hcc.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.code.entity.CodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 代码表
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
@Mapper
public interface CodeMapper extends BaseMapper<CodeEntity> {

    void deleteByUserId(@Param("userId") int userId);
}
