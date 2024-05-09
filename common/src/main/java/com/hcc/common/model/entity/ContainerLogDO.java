package com.hcc.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 容器日志
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContainerLogDO {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 任务id
     */
    private int taskId;

    /**
     * 日志内容
     */
    private String content;
}
