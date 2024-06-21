package com.hcc.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 *
 * @Author: hcc
 * @Date: 2024/3/8
 */
@Data
public class RecordVo {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private float score;
}
