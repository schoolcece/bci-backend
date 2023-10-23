package com.hcc.common.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserInfo {

    private String username;

    private Integer userId;

    private List<Integer> permissions;

    private List<Integer> undoPermissions;

    private Integer[] teamId;

    private Integer[] role;
}
