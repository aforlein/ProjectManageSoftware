package com.pm.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.PageResult;
import com.pm.dto.project.ProjectDTO;
import com.pm.dto.project.ProjectPageQuery;
import com.pm.entity.PmProject;
import com.pm.entity.SysUser;
import com.pm.exception.BusinessException;
import com.pm.mapper.PmProjectMapper;
import com.pm.mapper.SysUserMapper;
import com.pm.service.ProjectService;
import com.pm.vo.project.ProjectVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final PmProjectMapper pmProjectMapper;
    private final SysUserMapper sysUserMapper;

    @Override
    public PageResult<ProjectVO> page(ProjectPageQuery query) {
        Page<?> page = new Page<>(query.getPage(), query.getSize());
        Page<ProjectVO> result = pmProjectMapper.selectProjectPage(page, query);
        return PageResult.of(result, result.getRecords());
    }

    @Override
    public ProjectVO getById(Long id) {
        PmProject project = pmProjectMapper.selectById(id);
        if (project == null) {
            throw new BusinessException("项目不存在");
        }

        ProjectVO vo = new ProjectVO();
        BeanUtils.copyProperties(project, vo);

        if (project.getProjectManagerId() != null) {
            SysUser manager = sysUserMapper.selectById(project.getProjectManagerId());
            if (manager != null) {
                vo.setProjectManagerName(manager.getRealName());
            }
        }

        return vo;
    }

    @Override
    @Transactional
    public Long create(ProjectDTO dto) {
        PmProject project = new PmProject();
        BeanUtils.copyProperties(dto, project);

        project.setCreateBy(getCurrentUserId());
        pmProjectMapper.insert(project);
        return project.getId();
    }

    @Override
    @Transactional
    public void update(Long id, ProjectDTO dto) {
        PmProject project = pmProjectMapper.selectById(id);
        if (project == null) {
            throw new BusinessException("项目不存在");
        }

        PmProject updateProject = new PmProject();
        updateProject.setId(id);
        BeanUtils.copyProperties(dto, updateProject);
        pmProjectMapper.updateById(updateProject);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PmProject project = pmProjectMapper.selectById(id);
        if (project == null) {
            throw new BusinessException("项目不存在");
        }
        pmProjectMapper.deleteById(id);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return Long.parseLong(auth.getName());
        }
        return null;
    }
}
