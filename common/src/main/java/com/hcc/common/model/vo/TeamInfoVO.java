package com.hcc.common.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 *
 * @Author: hcc
 * @Date: 2024/1/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfoVO {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 队伍名称
     */
    private String teamName;


    /**
     * 学校、单位
     */
    private String university;

    /**
     * 指导老师
     */
    private String instructor;
}
