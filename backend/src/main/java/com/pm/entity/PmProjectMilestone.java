package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("pm_project_milestone")
public class PmProjectMilestone {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String milestoneName;
    private String description;
    private LocalDate plannedDate;
    private LocalDate actualDate;
    private String status;
    private Integer sortOrder;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
