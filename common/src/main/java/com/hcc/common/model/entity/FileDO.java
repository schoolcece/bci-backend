package com.hcc.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("bci_file")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 范式id
     */
    private int paradigmId;

    /**
     * 文件路径
     */
    private String url;

    /**
     * 上传用户id
     */
    private int userid;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件描述
     */
    private String description;

    /**
     * 上传时间
     */
    private Date createTime;
}
