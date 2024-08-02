package com.hcc.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskFinalVO {

    private int id;

    private String taskName;

    private String md5;

    private int status;
}
