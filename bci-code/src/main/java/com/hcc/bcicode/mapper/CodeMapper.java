package com.hcc.bcicode.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hcc.common.model.entity.CodeDO;
import com.hcc.common.model.vo.CodeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: mapper层 代码管理
 *
 * @Author: hcc
 * @Date: 2023/12/11
 */
@Mapper
public interface CodeMapper extends BaseMapper<CodeDO> {
    List<CodeVO> selectPageByUserId(@Param("paradigmId")int paradigmId, @Param("userId")int userId, @Param("index")Integer index, @Param("pageNum")int pageNum);

    String getCodeUrlById(@Param("codeId")int codeId);

    String getFileNameById(@Param("codeId")int codeId);
}
