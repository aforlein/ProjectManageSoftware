package com.pm.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeployRecordDTO {
    @NotBlank(message = "部署版本不能为空")
    private String deployVersion;
    private String deployEnv;
    private LocalDateTime deployDate;
    private String status;
    private String deployNotes;
}
