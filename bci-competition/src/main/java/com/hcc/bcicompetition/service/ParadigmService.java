package com.hcc.bcicompetition.service;

import com.hcc.common.model.dto.ParadigmDTO;
import com.hcc.common.model.vo.ParadigmVO;

import java.util.List;

/**
 * Description: service层 范式信息服务层接口
 *
 * @Author: hcc
 * @Date: 2023/12/28
 */
public interface ParadigmService {
    /**
     * 获取范式信息
     * @param eventId
     * @return
     */
    List<ParadigmVO> listParadigm(int eventId);

    ParadigmDTO getInfoByParadigmId(int paradigmId);
}
