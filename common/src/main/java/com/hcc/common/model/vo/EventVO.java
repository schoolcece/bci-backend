package com.hcc.common.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Description: 赛事信息
 *
 * @Author: hcc
 * @Date: 2023/12/27
 */
@Data
@Builder
public class EventVO {
    /**
     * 主键id
     */
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
