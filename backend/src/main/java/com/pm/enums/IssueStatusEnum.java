package com.pm.enums;

import lombok.Getter;

@Getter
public enum IssueStatusEnum {

    OPEN("open", "未开始"),
    IN_PROGRESS("in_progress", "处理中"),
    RESOLVED("resolved", "已解决"),
    CLOSED("closed", "已关闭"),
    REOPENED("reopened", "已重新打开");

    private final String code;
    private final String desc;

    IssueStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
