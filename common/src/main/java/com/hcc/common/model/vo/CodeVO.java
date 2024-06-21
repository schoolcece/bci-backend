package com.hcc.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Description:
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@Data
@Builder
public class CodeVO {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 文件名
     */
    private String fileName;


    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

}
