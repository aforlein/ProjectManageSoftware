package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("pm_project")
public class PmProject {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String projectName;

    private String customerName;

    private String location;

    private java.math.BigDecimal longitude;

    private java.math.BigDecimal latitude;

    private String projectScope;

    private String projectTarget;

    private BigDecimal contractAmount;

    private BigDecimal estimatedCost;

    private Long projectManagerId;

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
