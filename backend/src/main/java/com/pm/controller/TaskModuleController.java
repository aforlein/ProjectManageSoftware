package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.project.TaskModuleDTO;
import com.pm.entity.PmIterationTask;
import com.pm.entity.PmTaskModule;
import com.pm.exception.BusinessException;
import com.pm.mapper.PmIterationTaskMapper;
import com.pm.mapper.PmTaskModuleMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "任务模块分类")
@RestController
@RequestMapping("/api/project/{projectId}/task-module")
@RequiredArgsConstructor
public class TaskModuleController {
    private final PmTaskModuleMapper taskModuleMapper;
    private final PmIterationTaskMapper taskMapper;

    @GetMapping
    public R<List<PmTaskModule>> list(@PathVariable Long projectId) {
        return R.success(taskModuleMapper.selectList(new LambdaQueryWrapper<PmTaskModule>()
                .eq(PmTaskModule::getProjectId, projectId).orderByAsc(PmTaskModule::getCreateTime)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody TaskModuleDTO dto) {
        PmTaskModule entity = new PmTaskModule();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        taskModuleMapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody TaskModuleDTO dto) {
        PmTaskModule entity = new PmTaskModule();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        taskModuleMapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        Long count = taskMapper.selectCount(
                new LambdaQueryWrapper<PmIterationTask>().eq(PmIterationTask::getModuleId, id));
        if (count > 0) {
            throw new BusinessException("该模块下还有任务，无法删除");
        }
        taskModuleMapper.deleteById(id);
        return R.success();
    }
}
