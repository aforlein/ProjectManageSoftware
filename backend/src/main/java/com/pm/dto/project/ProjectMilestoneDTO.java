package com.pm.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectMilestoneDTO {
    @NotBlank(message = "里程碑名称不能为空")
    private String milestoneName;
    private String description;
    private LocalDate plannedDate;
    private LocalDate actualDate;
    private String status;
    private Integer sortOrder;
}
