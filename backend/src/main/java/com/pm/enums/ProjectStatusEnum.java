package com.pm.enums;

import lombok.Getter;

@Getter
public enum ProjectStatusEnum {

    PLANNING("planning", "规划中"),
    IN_PROGRESS("in_progress", "进行中"),
    COMPLETED("completed", "已完成"),
    CLOSED("closed", "已关闭"),
    ON_HOLD("on_hold", "已暂停");

    private final String code;
    private final String desc;

    ProjectStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
