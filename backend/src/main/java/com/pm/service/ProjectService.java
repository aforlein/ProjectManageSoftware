package com.pm.service;

import com.pm.common.PageResult;
import com.pm.dto.project.ProjectDTO;
import com.pm.dto.project.ProjectPageQuery;
import com.pm.vo.project.ProjectVO;

public interface ProjectService {

    PageResult<ProjectVO> page(ProjectPageQuery query);

    ProjectVO getById(Long id);

    Long create(ProjectDTO dto);

    void update(Long id, ProjectDTO dto);

    void delete(Long id);
}
