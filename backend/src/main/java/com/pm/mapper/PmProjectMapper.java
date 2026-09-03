package com.pm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.dto.project.ProjectPageQuery;
import com.pm.entity.PmProject;
import com.pm.vo.project.ProjectVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PmProjectMapper extends BaseMapper<PmProject> {

    Page<ProjectVO> selectProjectPage(Page<?> page, @Param("query") ProjectPageQuery query);
}
