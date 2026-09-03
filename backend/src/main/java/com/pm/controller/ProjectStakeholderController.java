package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.project.StakeholderDTO;
import com.pm.entity.PmStakeholder;
import com.pm.mapper.PmStakeholderMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "关系人")
@RestController
@RequestMapping("/api/project/{projectId}/stakeholder")
@RequiredArgsConstructor
public class ProjectStakeholderController {
    private final PmStakeholderMapper mapper;

    @GetMapping
    public R<List<PmStakeholder>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmStakeholder>()
                .eq(PmStakeholder::getProjectId, projectId).orderByDesc(PmStakeholder::getCreateTime)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody StakeholderDTO dto) {
        PmStakeholder entity = new PmStakeholder();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody StakeholderDTO dto) {
        PmStakeholder entity = new PmStakeholder();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
