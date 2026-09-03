package com.pm.vo.system;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleVO {

    private Long id;
    private String roleName;
    private String roleCode;
    private String description;
    private LocalDateTime createTime;
}
