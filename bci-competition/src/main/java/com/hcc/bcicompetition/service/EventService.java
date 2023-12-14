package com.hcc.bcicompetition.service;

import java.util.Date;

/**
 * Description: service层 赛事信息服务层接口
 *
 * @Author: hcc
 * @Date: 2023/12/13
 */
public interface EventService {
    /**
     * 判断curTime是否处于赛事期间
     * @param curTime
     * @param event
     * @return
     */
    boolean inTime(Date curTime, int event);
}
