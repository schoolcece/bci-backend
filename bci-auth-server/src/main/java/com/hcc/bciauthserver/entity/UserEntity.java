package com.hcc.bciauthserver.entity;

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
//@Data
//@TableName("bci_user")
//public class UserEntity implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 */
//	@TableId
//	private Integer userId;
//	/**
//	 * 用户名
//	 */
//	private String username;
//	/**
//	 * 密码
//	 */
//	private String password;
//	/**
//	 * 手机号
//	 */
//	private String mobile;
//	/**
//	 * 状态  0：禁用   1：正常
//	 */
//	private Integer status;
//	/**
//	 * 创建时间
//	 */
//	private Timestamp createTime;
//}

@Data
@TableName("bci_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer userId;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 电子邮箱
	 */
	private String email;
	/**
	 * 学校
	 */
	private String university;
	/**
	 * 专业
	 */
	private String profession;
	/**
	 * uid
	 */
	private String uid;
	/**
	 * 年龄（身份证认证后的补充信息）
	 */
	private Integer age;
	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
}