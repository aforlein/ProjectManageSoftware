package com.pm.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectPlanDTO {
    @NotBlank(message = "计划名称不能为空")
    private String planName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Integer progress;
    private Long parentId;
    private Integer sortOrder;
}
