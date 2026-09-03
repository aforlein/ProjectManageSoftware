package com.pm.dto.system;

import com.pm.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPageQuery extends PageQuery {

    private String keyword;
    private Long roleId;
    private Integer status;
}
