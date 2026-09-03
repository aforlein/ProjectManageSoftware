package com.pm.vo.project;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectVO {

    private Long id;
    private String projectName;
    private String customerName;
    private String location;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String projectScope;
    private String projectTarget;
    private BigDecimal contractAmount;
    private BigDecimal estimatedCost;
    private Long projectManagerId;
    private String projectManagerName;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createTime;
}
