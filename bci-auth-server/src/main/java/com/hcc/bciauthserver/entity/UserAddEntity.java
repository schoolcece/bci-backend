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
@TableName("bci_user_additional")
public class UserAddEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer uaddId;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 赛事
	 */
	private Integer competition;
	/**
	 * 队伍ID
	 */
	private Integer teamId;
	/**
	 * 角色  0：普通用户   1：队员 2：队长
	 */
	private Integer role;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
}
