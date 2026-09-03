package com.pm.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskModuleDTO {
    @NotBlank(message = "模块名称不能为空")
    private String moduleName;
}
