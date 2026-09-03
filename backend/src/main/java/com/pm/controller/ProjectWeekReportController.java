package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.project.WeekReportDTO;
import com.pm.entity.PmWeekReport;
import com.pm.mapper.PmWeekReportMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "周报记录")
@RestController
@RequestMapping("/api/project/{projectId}/week-report")
@RequiredArgsConstructor
public class ProjectWeekReportController {
    private final PmWeekReportMapper mapper;

    @GetMapping
    public R<List<PmWeekReport>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmWeekReport>()
                .eq(PmWeekReport::getProjectId, projectId).orderByDesc(PmWeekReport::getWeekStart)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody WeekReportDTO dto) {
        PmWeekReport entity = new PmWeekReport();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody WeekReportDTO dto) {
        PmWeekReport entity = new PmWeekReport();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
