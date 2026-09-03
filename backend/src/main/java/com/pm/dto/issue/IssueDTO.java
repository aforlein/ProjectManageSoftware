package com.pm.dto.issue;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IssueDTO {
    @NotBlank(message = "问题标题不能为空")
    private String issueTitle;
    private String issueType;
    private String issueDescription;
    private String priority;
    private String status;
    private String rootCause;
    private String solution;
    private Long ownerId;
}
