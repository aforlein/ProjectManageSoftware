package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.project.IterationTaskDTO;
import com.pm.entity.PmIterationTask;
import com.pm.mapper.PmIterationTaskMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "迭代任务")
@RestController
@RequestMapping("/api/project/{projectId}/task")
@RequiredArgsConstructor
public class IterationTaskController {
    private final PmIterationTaskMapper mapper;

    @GetMapping
    public R<List<PmIterationTask>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmIterationTask>()
                .eq(PmIterationTask::getProjectId, projectId).orderByDesc(PmIterationTask::getCreateTime)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody IterationTaskDTO dto) {
        PmIterationTask entity = new PmIterationTask();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody IterationTaskDTO dto) {
        PmIterationTask entity = new PmIterationTask();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
