package com.hcc.bcitask.feign;

import com.hcc.common.model.dto.ParadigmDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description: 远程调用赛事服务接口
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@FeignClient(name = "bci-competition", url = "http://127.0.0.1:8082")
public interface CompetitionFeign {
    @GetMapping("/paradigm/getInfoByParadigmId")
    ParadigmDTO getInfoByParadigmId( @RequestParam("paradigmId")int paradigmId);
}
