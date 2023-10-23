package com.hcc.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RecordVo {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
    private Date createTime;
    private Float score;

    public RecordVo(Date createTime,Float score){
        this.createTime = createTime;
        this.score =score;
    }
}
