package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.project.ProjectPlanDTO;
import com.pm.entity.PmProjectPlan;
import com.pm.mapper.PmProjectPlanMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "项目计划")
@RestController
@RequestMapping("/api/project/{projectId}/plan")
@RequiredArgsConstructor
public class ProjectPlanController {
    private final PmProjectPlanMapper mapper;

    @GetMapping
    public R<List<PmProjectPlan>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmProjectPlan>()
                .eq(PmProjectPlan::getProjectId, projectId).orderByAsc(PmProjectPlan::getSortOrder)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody ProjectPlanDTO dto) {
        PmProjectPlan entity = new PmProjectPlan();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody ProjectPlanDTO dto) {
        PmProjectPlan entity = new PmProjectPlan();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
