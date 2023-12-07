package com.hcc.common.model.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Description: 创建队伍传递参数
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@Data
public class CreateTeamParam {

    /**
     * 队伍所属赛事id
     */
    @NotNull
    private int eventId;

    /**
     * 队伍名称
     */
    @NotBlank
    private String teamName;

    /**
     * 队伍导师
     */
    @NotBlank
    private String instructor;

    /**
     * 学校、单位
     */
    @NotBlank
    private String university;
}
