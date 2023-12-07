package com.hcc.common.model.dto;

import lombok.Data;

/**
 * Description: 范式权限信息
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@Data
public class PermissionInfoDTO {
    /**
     * 范式id
     */
    private int paradigmId;

    /**
     * 权限： 0 代表待审核， 1 代表审核已通过， 2 代表审核未通过， 3代表范式负责人
     */
    private int status;
}
