package com.hcc.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 计算节点实体类
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComputeNodeDO {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 节点ip
     */
    private String ip;

    /**
     * 运行任务数
     */
    private int runningTasks;

    /**
     * 最大运行任务数
     */
    private int maxTasks;

    /**
     * 节点状态 0 代表禁用， 1 代表可用
     */
    private int status;

}
