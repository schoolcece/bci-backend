package com.hcc.bcitask.feign;

import com.hcc.common.model.dto.ParadigmDTO;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Description: 远程调用赛事服务接口
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@FeignClient
public interface CompetitionFeign {
    ParadigmDTO getInfoByParadigmId(int paradigmId);
}
