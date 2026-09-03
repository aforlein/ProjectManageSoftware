package com.pm.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {

    ADMIN("ADMIN", "管理员"),
    PROJECT_MANAGER("PROJECT_MANAGER", "项目经理"),
    MEMBER("MEMBER", "成员"),
    VIEWER("VIEWER", "观察者");

    private final String code;
    private final String desc;

    UserRoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
