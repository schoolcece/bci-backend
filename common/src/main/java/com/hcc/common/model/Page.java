package com.hcc.common.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Description: 分页查询Model
 *
 * @Author: hcc
 * @Date: 2024/3/12
 */
@Builder
@Data
public class Page<T> {

    public Long total;

    public List<T> record;
}
