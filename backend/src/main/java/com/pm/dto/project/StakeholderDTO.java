package com.pm.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StakeholderDTO {
    @NotBlank(message = "姓名不能为空")
    private String name;
    private String role;
    private String department;
    private String phone;
    private String email;
}
