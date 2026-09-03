package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("pm_iteration_task")
public class PmIterationTask {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private Long planId;
    private String taskName;
    private String description;
    private Long assigneeId;
    private String priority;
    private String status;
    private String taskType;
    private Long moduleId;
    private Integer storyPoints;
    private LocalDate startDate;
    private LocalDate endDate;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
