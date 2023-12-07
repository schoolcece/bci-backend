package com.hcc.bciauth.enums;

import lombok.AllArgsConstructor;

/**
 * Description: 角色枚举
 *
 * @Author: hcc
 * @Date: 2023/12/4
 */
@AllArgsConstructor
public enum RoleEnum {
    NORMAL_USER(0),
    ADMIN(1);

    private int role;

    public int getRole() {
        return role;
    }
}
