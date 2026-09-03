package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("pm_issue_log")
public class PmIssueLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String issueTitle;
    private String issueType;
    private String issueDescription;
    private String priority;
    private String status;
    private String rootCause;
    private String solution;
    private Long ownerId;
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
