package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.project.ProjectMilestoneDTO;
import com.pm.entity.PmProjectMilestone;
import com.pm.mapper.PmProjectMilestoneMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "项目里程碑")
@RestController
@RequestMapping("/api/project/{projectId}/milestone")
@RequiredArgsConstructor
public class ProjectMilestoneController {
    private final PmProjectMilestoneMapper mapper;

    @GetMapping
    public R<List<PmProjectMilestone>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmProjectMilestone>()
                .eq(PmProjectMilestone::getProjectId, projectId).orderByAsc(PmProjectMilestone::getSortOrder)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody ProjectMilestoneDTO dto) {
        PmProjectMilestone entity = new PmProjectMilestone();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody ProjectMilestoneDTO dto) {
        PmProjectMilestone entity = new PmProjectMilestone();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
