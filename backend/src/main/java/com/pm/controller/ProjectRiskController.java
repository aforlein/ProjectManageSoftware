package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.risk.RiskDTO;
import com.pm.entity.PmProjectRisk;
import com.pm.mapper.PmProjectRiskMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "项目风险管理")
@RestController
@RequestMapping("/api/project/{projectId}/risk")
@RequiredArgsConstructor
public class ProjectRiskController {
    private final PmProjectRiskMapper mapper;

    @GetMapping
    public R<List<PmProjectRisk>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmProjectRisk>()
                .eq(PmProjectRisk::getProjectId, projectId).orderByDesc(PmProjectRisk::getCreateTime)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody RiskDTO dto) {
        PmProjectRisk entity = new PmProjectRisk();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody RiskDTO dto) {
        PmProjectRisk entity = new PmProjectRisk();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
