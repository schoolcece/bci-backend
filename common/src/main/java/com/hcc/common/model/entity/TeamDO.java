package com.hcc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 队伍实体类
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("bci_team")
public class TeamDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    private Integer id;

    /**
     * 队伍所属赛事id
     */
    private int eventId;

    /**
     * 队长id
     */
    private int leaderId;

    /**
     * 队伍名称
     */
    private String teamName;

    /**
     * 队伍导师
     */
    private String instructor;

    /**
     * 学校、单位
     */
    private String university;

    /**
     * 队伍状态： 0 代表正常可用， 1 代表禁用， 2 代表队伍解散注销
     */
    private int status;

    /**
     * 队伍创建时间
     */
    private Date createTime;
}
