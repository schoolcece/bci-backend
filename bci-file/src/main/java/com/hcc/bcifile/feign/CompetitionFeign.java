package com.hcc.bcifile.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@FeignClient(name = "bci-competition")
public interface CompetitionFeign {
    @GetMapping("/paradigm/getEventByParadigm")
    int getEventByParadigm(@RequestParam("paradigmId") int paradigmId);
}
