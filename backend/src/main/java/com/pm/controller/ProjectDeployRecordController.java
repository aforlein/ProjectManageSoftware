package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.project.DeployRecordDTO;
import com.pm.entity.PmDeployRecord;
import com.pm.mapper.PmDeployRecordMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "部署记录")
@RestController
@RequestMapping("/api/project/{projectId}/deploy-record")
@RequiredArgsConstructor
public class ProjectDeployRecordController {
    private final PmDeployRecordMapper mapper;

    @GetMapping
    public R<List<PmDeployRecord>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmDeployRecord>()
                .eq(PmDeployRecord::getProjectId, projectId).orderByDesc(PmDeployRecord::getDeployDate)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody DeployRecordDTO dto) {
        PmDeployRecord entity = new PmDeployRecord();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody DeployRecordDTO dto) {
        PmDeployRecord entity = new PmDeployRecord();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
