package com.hcc.task.entity;

import lombok.Data;

@Data
public class CollectionVo {
    private static final long serialVersionUID = 1L;

    private String device;
    private String datasourceIP;
    private String datasourcePORT;
    private String kafkaIP;
    private String kafkaPORT;
    private String kafkaTOPIC;
    private String comment;
}
