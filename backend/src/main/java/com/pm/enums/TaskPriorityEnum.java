package com.pm.enums;

import lombok.Getter;

@Getter
public enum TaskPriorityEnum {

    HIGH("high", "高"),
    MEDIUM("medium", "中"),
    LOW("low", "低");

    private final String code;
    private final String desc;

    TaskPriorityEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
