package com.hcc.bcicompetition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hcc.bcicompetition.mapper.ParadigmMapper;
import com.hcc.bcicompetition.service.ParadigmService;
import com.hcc.common.model.dto.ParadigmDTO;
import com.hcc.common.model.entity.ParadigmDO;
import com.hcc.common.model.vo.ParadigmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 范式信息服务实现
 *
 * @Author: hcc
 * @Date: 2023/12/28
 */
@Service
public class ParadigmServiceImpl implements ParadigmService {

    @Autowired
    private ParadigmMapper paradigmMapper;

    @Override
    public List<ParadigmVO> listParadigm(int eventId) {
        List<ParadigmDO> paradigmDOS = paradigmMapper.selectList(new QueryWrapper<ParadigmDO>().eq("event_id", eventId));
        return paradigmDOS.stream().map(item -> ParadigmVO.builder()
                .id(item.getId())
                .paradigmName(item.getParadigmName())
                .build()).collect(Collectors.toList());
    }

    @Override
    public ParadigmDTO getInfoByParadigmId(int paradigmId) {
        return paradigmMapper.selectByParadigmId(paradigmId);
    }
}
