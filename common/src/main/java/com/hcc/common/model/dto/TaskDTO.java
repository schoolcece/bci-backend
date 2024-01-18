package com.hcc.common.model.dto;

import com.hcc.common.model.vo.CodeVO;
import com.hcc.common.model.vo.TaskVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @Author: hcc
 * @Date: 2024/1/18
 */
@Data
@Builder
public class TaskDTO {

    /**
     * 任务信息
     */
    private List<TaskVO> tasks;

    /**
     * 代码总数量
     */
    private long total;
}
