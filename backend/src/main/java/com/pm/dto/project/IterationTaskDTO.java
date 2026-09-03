package com.pm.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class IterationTaskDTO {
    @NotBlank(message = "任务名称不能为空")
    private String taskName;
    private String description;
    private Long planId;
    private Long assigneeId;
    private String priority;
    private String status;
    private String taskType;
    private Long moduleId;
    private Integer storyPoints;
    private LocalDate startDate;
    private LocalDate endDate;
}
