package com.hcc.bcicompetition.controller;

import com.hcc.bcicompetition.service.ParadigmService;
import com.hcc.common.model.R;
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
}
