package com.pm.vo.system;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private Long roleId;
    private String roleName;
    private Integer status;
    private LocalDateTime createTime;
}
