package com.hcc.bcicompetition.service.impl;

import com.hcc.bcicompetition.mapper.EventMapper;
import com.hcc.bcicompetition.service.EventService;
import com.hcc.common.model.entity.EventDO;
import com.hcc.common.model.vo.EventVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public List<EventVO> listEvent() {
        List<EventDO> eventDOS = eventMapper.selectList(null);
        return eventDOS.stream().map(item -> EventVO.builder()
                .id(item.getId())
                .eventName(item.getEventName())
                .eventLeader(item.getEventLeader())
                .eventDesc(item.getEventDesc())
                .logoUrl(item.getLogoUrl())
                .startTime(item.getStartTime())
                .endTime(item.getEndTime())
                .build()).collect(Collectors.toList());
    }
}
