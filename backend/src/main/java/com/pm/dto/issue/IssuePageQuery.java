package com.pm.dto.issue;

import com.pm.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IssuePageQuery extends PageQuery {
    private Long projectId;
    private String priority;
    private String status;
    private String keyword;
}
