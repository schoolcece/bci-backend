package com.hcc.common.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileVO {

    private Integer id;

    private String fileName;

    private String teamName;

    private String fileSize;
}
