package com.hcc.bciauthserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 用户表
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
@Data
@TableName("bci_team_right_relation")
public class TeamRightEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer trrId;
	/**
	 * 队伍id
	 */
	private Integer teamId;
	/**
	 * 权限
	 */
	@TableField("`right`")
	private Integer right;
	/**
	 * 状态 0待认证 1已通过
	 */
	private Integer status;
	/**
	 * 状态  0：禁用   1：正常
	 */
	private Timestamp createTime;
}
