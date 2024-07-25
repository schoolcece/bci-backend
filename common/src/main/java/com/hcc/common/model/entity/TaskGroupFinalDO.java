package com.hcc.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskGroupFinalDO {

    private Integer id;

    private int taskId;

    private int groupId;

    private String containerId;

    private String containerName;

    private int status;
}
