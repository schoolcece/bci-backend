package com.hcc.common.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * Description:
 *
 * @Author: hcc
 * @Date: 2023/12/28
 */
@Data
@Builder
public class ParadigmVO {

    /**
     * 主键id
     */
    private Integer id;


    /**
     * 范式名称
     */
    private String paradigmName;
}
