package com.hcc.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: 任务实体类
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDO {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 队伍id
     */
    private int teamId;

    /**
     * 代码id
     */
    private int codeId;

    /**
     * 范式id
     */
    private int paradigmId;

    /**
     * 任务类型
     */
    private int taskType;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 数据集
     */
    private int dataset;

    /**
     * 容器id
     */
    private String containerId;

    /**
     * 状态： 0:待运行 1:运行中 2:运行成功 3:运行失败
     */
    private int status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 得分
     */
    private Float score;

    /**
     * 计算节点ip
     */
    private String computeNodeIp;

    /**
     * 逻辑删除 0 代表删除 1 代表展示
     */
    private Integer showStatus;
}
