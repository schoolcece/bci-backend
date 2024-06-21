package com.hcc.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description:
 *
 * @Author: hcc
 * @Date: 2024/1/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskVO {

    /**
     * id
     */
    private int id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 状态： 0:待运行 1:运行中 2:运行成功 3:运行失败
     */
    private int status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss" , timezone = "GMT+8")
    private Date createTime;

}
