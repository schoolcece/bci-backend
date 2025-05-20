package com.hcc.common.model.dto;

import com.hcc.common.model.vo.FileVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FileDTO {

    private List<FileVO> files;

    private long total;
}
