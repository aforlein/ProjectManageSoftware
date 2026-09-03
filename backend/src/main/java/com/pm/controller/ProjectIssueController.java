package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.issue.IssueDTO;
import com.pm.entity.PmIssueLog;
import com.pm.mapper.PmIssueLogMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "项目问题日志")
@RestController
@RequestMapping("/api/project/{projectId}/issue")
@RequiredArgsConstructor
public class ProjectIssueController {
    private final PmIssueLogMapper mapper;

    @GetMapping
    public R<List<PmIssueLog>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmIssueLog>()
                .eq(PmIssueLog::getProjectId, projectId).orderByDesc(PmIssueLog::getCreateTime)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody IssueDTO dto) {
        PmIssueLog entity = new PmIssueLog();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody IssueDTO dto) {
        PmIssueLog entity = new PmIssueLog();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
