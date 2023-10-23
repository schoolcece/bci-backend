package com.hcc.task.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("bci_collection_task")
public class CollectionTaskEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer collectionTaskId;

    private String type;

    private String config;

    private String builder;

    private Integer status;

    private Date createTime;

    @TableField(insertStrategy = FieldStrategy.NEVER,updateStrategy = FieldStrategy.NEVER)
    @TableLogic(value = "1",delval = "0")
    private Integer showStatus;

    private String comment;
}
