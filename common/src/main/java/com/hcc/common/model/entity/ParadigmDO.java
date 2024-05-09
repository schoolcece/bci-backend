package com.hcc.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: 范式信息实体类
 *
 * @Author: hcc
 * @Date: 2023/12/28
 */
@Data
@TableName("bci_paradigm")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParadigmDO {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 赛事id
     */
    private int eventId;

    /**
     * 范式名称
     */
    private String paradigmName;

    /**
     * 范式描述
     */
    private String paradigmDesc;

    /**
     * A榜数据集
     */
    private String aData;

    /**
     * B榜数据集
     */
    private String bData;

    /**
     * 切榜时间
     */
    private Date changeTime;

    /**
     * 镜像
     */
    private String image;
}
