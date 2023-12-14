package com.hcc.bcicode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.entity.CodeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: mapper层 代码管理
 *
 * @Author: hcc
 * @Date: 2023/12/11
 */
@Mapper
public interface CodeMapper extends BaseMapper<CodeDO> {
}
