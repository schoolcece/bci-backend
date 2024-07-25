package com.hcc.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskFinalDO {

    private Integer id;

    private int userId;

    private int teamId;

    private int codeId;

    private int paradigmId;

    private String taskName;

    private int taskType;

    private String computeNodeIp;
}
