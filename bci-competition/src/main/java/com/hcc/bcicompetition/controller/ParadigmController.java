package com.hcc.bcicompetition.controller;

import com.hcc.bcicompetition.service.ParadigmService;
import com.hcc.common.model.R;
import com.hcc.common.model.dto.ParadigmDTO;
import com.hcc.common.model.vo.ParadigmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: controller层 范式管理
 *
 * @Author: hcc
 * @Date: 2023/12/11
 */
@RestController
@RequestMapping("/paradigm")
public class ParadigmController {

    @Autowired
    private ParadigmService paradigmService;

    /**
     * 获取指定赛事包含所有范式接口
     * @param eventId
     * @return
     */
    @GetMapping("/listParadigm")
    public R listParadigm(@RequestParam("eventId") int eventId) {
        return R.ok().put("data", paradigmService.listParadigm(eventId));
    }

    /**
     * 过去范式信息 内部调用接口
     * @param paradigmId
     * @return
     */
    @GetMapping("/getInfoByParadigmId")
    ParadigmDTO getInfoByParadigmId(@RequestParam("paradigmId")int paradigmId) {
        return paradigmService.getInfoByParadigmId(paradigmId);
    }
}
