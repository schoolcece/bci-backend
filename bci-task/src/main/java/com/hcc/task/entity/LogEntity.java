package com.hcc.task.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 日志表
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-10-06 12:53:42
 */
@Data
@TableName("bci_log")
public class LogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer logId;
	/**
	 * 日志内容
	 */
	private String content;
	/**
	 * 任务id
	 */
	private Integer taskId;

}
