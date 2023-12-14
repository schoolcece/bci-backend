package com.hcc.common.model.param;

import lombok.Data;

import java.util.Date;

/**
 * Description: 发布赛事 请求参数
 *
 * @Author: hcc
 * @Date: 2023/12/12
 */
@Data
public class EventParam {

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
    private String logo;

    /**
     * 赛事开启时间
     */
    private Date startTime;

    /**
     * 赛事结束时间
     */
    private Date endTime;
}
