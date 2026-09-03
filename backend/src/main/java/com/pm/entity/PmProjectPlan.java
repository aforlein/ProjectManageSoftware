package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("pm_project_plan")
public class PmProjectPlan {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String planName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Integer progress;
    private Long parentId;
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
