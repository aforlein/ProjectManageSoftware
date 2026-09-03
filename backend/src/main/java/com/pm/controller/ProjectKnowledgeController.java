package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.project.KnowledgeDTO;
import com.pm.entity.PmKnowledge;
import com.pm.mapper.PmKnowledgeMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "知识库")
@RestController
@RequestMapping("/api/project/{projectId}/knowledge")
@RequiredArgsConstructor
public class ProjectKnowledgeController {
    private final PmKnowledgeMapper mapper;

    @GetMapping
    public R<List<PmKnowledge>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmKnowledge>()
                .eq(PmKnowledge::getProjectId, projectId).orderByDesc(PmKnowledge::getCreateTime)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody KnowledgeDTO dto) {
        PmKnowledge entity = new PmKnowledge();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody KnowledgeDTO dto) {
        PmKnowledge entity = new PmKnowledge();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
