package com.hcc.common.utils;

public enum RoleEnum {
    TEAM_LEADER(2),
    TEAM_MEMBER(1),
    NORMAL_USER(0);
    private int role;

    RoleEnum(int role) {
        this.role = role;
    }
    public int getRole() {
        return this.role;
    }
}
