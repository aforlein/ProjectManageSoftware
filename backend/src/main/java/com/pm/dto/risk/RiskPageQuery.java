package com.pm.dto.risk;

import com.pm.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RiskPageQuery extends PageQuery {
    private Long projectId;
    private String riskLevel;
    private String status;
    private String keyword;
}
