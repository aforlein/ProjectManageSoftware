package com.pm.dto.risk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RiskDTO {
    @NotBlank(message = "风险标题不能为空")
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
}
