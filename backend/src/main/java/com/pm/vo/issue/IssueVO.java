package com.pm.vo.issue;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IssueVO {
    private Long id;
    private Long projectId;
    private String issueTitle;
    private String issueType;
    private String issueDescription;
    private String priority;
    private String status;
    private String rootCause;
    private String solution;
    private Long ownerId;
    private LocalDateTime createTime;
}
