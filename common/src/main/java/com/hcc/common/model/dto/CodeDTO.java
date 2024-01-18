package com.hcc.common.model.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.hcc.common.model.vo.CodeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Description: 代码信息
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@Data
@Builder
public class CodeDTO {

    /**
     * 代码信息
     */
    private List<CodeVO> codes;

    /**
     * 代码总数量
     */
    private long total;

}
