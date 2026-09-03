package com.pm.vo.risk;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RiskVO {
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
    private LocalDateTime createTime;
}
