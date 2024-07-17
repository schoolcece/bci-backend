package com.hcc.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamVO {

    private String teamName;

    private String university;

    private String instructor;

    private int memberNumbers;

    private List<String> members;

    private String leaderName;

    private String mobile;
}
