package com.hcc.common.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Description: 范式信息
 *
 * @Author: hcc
 * @Date: 2024/1/9
 */
@Data
@Builder
public class ParadigmDTO {

    /**
     * 范式名称
     */
    private String paradigmName;

    /**
     * 赛事id
     */
    private int eventId;

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
