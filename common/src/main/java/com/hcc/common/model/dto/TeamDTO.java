package com.hcc.common.model.dto;

import com.hcc.common.model.vo.TeamVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamDTO {
    private List<TeamVO> teams;

    private long total;
}
