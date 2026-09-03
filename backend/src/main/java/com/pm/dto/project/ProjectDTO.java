package com.pm.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjectDTO {

    @NotBlank(message = "项目名称不能为空")
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

    private String status;

    private LocalDate startDate;

    private LocalDate endDate;
}
