package com.hcc.scheduler.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 *
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-10-25 21:56:14
 */
@Data
@TableName("bci_compute_resource")
public class ComputeNodeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer nodeId;
    /**
     * 节点ip
     */
    private String ip;
    /**
     * 现运行任务数
     */
    private Integer runningTasks;
    /**
     * 最大运行任务数
     */
    private Integer maxTasks;
    /**
     * 目前是否可用 0 不可用 1 可用
     */
    private Integer status;

}
