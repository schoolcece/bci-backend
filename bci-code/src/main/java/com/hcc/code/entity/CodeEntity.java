package com.hcc.code.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 代码表
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
@Data
@TableName("bci_code")
public class CodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer codeId;
	/**
	 * 范式 0:ssvep 1:mi 2:erp 3:emotion 4:other 
	 */
	private Integer type;
	/**
	 * 路径
	 */
	private String url;
	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
	private Timestamp createTime;

	private String fileName;

	private Integer status;

	private Integer taskId;

	@TableField(insertStrategy = FieldStrategy.NEVER,updateStrategy = FieldStrategy.NEVER)
	@TableLogic(value = "1",delval = "0")
	private Integer showStatus;

}
