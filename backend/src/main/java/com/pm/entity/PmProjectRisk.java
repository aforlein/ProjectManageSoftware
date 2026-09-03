package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("pm_project_risk")
public class PmProjectRisk {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String riskTitle;
    private String riskType;
    private String riskLevel;
    private String probabilityLevel;
    private String impactLevel;
    private String riskDescription;
    private String responseStrategy;
    private String contingencyPlan;
    private String status;
    private Long ownerId;
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
