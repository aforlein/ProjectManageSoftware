package com.pm.dto.project;

import com.pm.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectPageQuery extends PageQuery {

    private String keyword;
    private String status;
}
