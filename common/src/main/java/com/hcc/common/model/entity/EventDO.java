package com.hcc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: 赛事信息实体类
 *
 * @Author: hcc
 * @Date: 2023/12/13
 */
@Data
@TableName("bci_event")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDO {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 赛事名称
     */
    private String eventName;

    /**
     * 赛事负责人
     */
    private int eventLeader;

    /**
     * 赛事描述
     */
    private String eventDesc;

    /**
     * 赛事logo
     */
    private String logoUrl;

    /**
     * 赛事开始时间
     */
    private Date startTime;

    /**
     * 赛事结束时间
     */
    private Date endTime;
}
