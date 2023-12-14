package com.hcc.bcicompetition.service.impl;

import com.hcc.bcicompetition.mapper.EventMapper;
import com.hcc.bcicompetition.service.EventService;
import com.hcc.common.model.entity.EventDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * Description: 赛事信息服务层实现
 *
 * @Author: hcc
 * @Date: 2023/12/13
 */
@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventMapper eventMapper;

    @Override
    public boolean inTime(Date curTime, int event) {
        EventDO eventDO = eventMapper.selectById(event);
        if (Objects.isNull(eventDO)) {
            return false;
        }
        return curTime.before(eventDO.getEndTime())&&curTime.after(eventDO.getStartTime());
    }
}
