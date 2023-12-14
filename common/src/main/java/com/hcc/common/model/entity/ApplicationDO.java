package com.hcc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: 报名信息实体类
 *
 * @Author: hcc
 * @Date: 2023/12/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDO {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 范式id
     */
    private int paradigmId;

    /**
     * 队伍id
     */
    private int teamId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态
     */
    private int status;

    /**
     * 审核人
     */
    private int updateUser;

    /**
     * 备注
     */
    private String comment;
}
