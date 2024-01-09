package com.hcc.common.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    private Date createTime;

}
