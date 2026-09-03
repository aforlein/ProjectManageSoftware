package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("pm_week_report")
public class PmWeekReport {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private String content;
    private String planNext;
    private String issues;
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
