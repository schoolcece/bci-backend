package com.hcc.task.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Data;

/**
 * 任务表
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
@Data
@TableName("bci_task")
public class TaskEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer taskId;
	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 队伍id
	 */
	private Integer teamId;
	/**
	 * 镜像
	 */
	private String image;
	/**
	 * 范式 0:ssvep 1:mi 2:erp 3:emotion 4:other 
	 */
	private Integer type;
	/**
	 * 数据集 0 A榜 1 B榜
	 */
	private Integer dataset;
	/**
	 * 代码id
	 */
	private Integer codeId;
	/**
	 * 容器id
	 */
	private String containerId;
	/**
	 * 状态： 0:未提交 1:初始化 2:初始化失败 3:运行中 4:运行失败 5:已完成
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	private Float score;

	private Integer computeNodeId;

	@TableField(insertStrategy = FieldStrategy.NEVER,updateStrategy = FieldStrategy.NEVER)
	@TableLogic(value = "1",delval = "0")
	private Integer showStatus;
}
