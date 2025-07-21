package com.hcc.common.model.dto;

import com.hcc.common.model.vo.TaskFinalVO;
import com.hcc.common.model.vo.TaskVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskFinalDTO {

    private List<TaskFinalVO> taskFinals;

    private long total;
}
