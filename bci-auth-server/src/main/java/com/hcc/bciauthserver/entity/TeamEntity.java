package com.hcc.bciauthserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("bci_team")
public class TeamEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 队伍id
	 */
	@TableId
	private Integer teamId;
	/**
	 * 队伍名
	 */
	private String teamName;
	/**
	 * 指导老师
	 */
	private String instructor;
	/**
	 * 学校、单位
	 */
	private String university;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
	private Timestamp createTime;
	/**
	 * 队长id
	 */
	private Integer userId;
	/**
	 * 赛事
	 */
	private Integer competition;
}
