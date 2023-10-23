package com.hcc.bciauthserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户表
 * 
 * @author hcc
 * @email 1301646502@qq.com
 * @date 2022-09-30 15:10:44
 */
@Data
public class TeamInfoVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 队伍名
	 */
	private String teamName;
	/**
	 * 赛事
	 */
	private Integer competition;
	/**
	 * 指导老师
	 */
	private String instructor;
	/**
	 * 学校、单位
	 */
	private String university;
}
