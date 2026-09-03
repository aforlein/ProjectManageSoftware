package com.pm.dto.project;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KnowledgeDTO {
    @NotBlank(message = "标题不能为空")
    private String title;
    private String content;
    private String type;
    private String tags;
}
