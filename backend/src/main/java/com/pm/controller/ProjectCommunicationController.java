package com.pm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.common.R;
import com.pm.dto.project.CommunicationDTO;
import com.pm.entity.PmCommunication;
import com.pm.mapper.PmCommunicationMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "沟通记录")
@RestController
@RequestMapping("/api/project/{projectId}/communication")
@RequiredArgsConstructor
public class ProjectCommunicationController {
    private final PmCommunicationMapper mapper;

    @GetMapping
    public R<List<PmCommunication>> list(@PathVariable Long projectId) {
        return R.success(mapper.selectList(new LambdaQueryWrapper<PmCommunication>()
                .eq(PmCommunication::getProjectId, projectId).orderByDesc(PmCommunication::getCommunicationDate)));
    }

    @PostMapping
    public R<Long> create(@PathVariable Long projectId, @Valid @RequestBody CommunicationDTO dto) {
        PmCommunication entity = new PmCommunication();
        BeanUtils.copyProperties(dto, entity);
        entity.setProjectId(projectId);
        mapper.insert(entity);
        return R.success(entity.getId());
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody CommunicationDTO dto) {
        PmCommunication entity = new PmCommunication();
        entity.setId(id);
        BeanUtils.copyProperties(dto, entity);
        mapper.updateById(entity);
        return R.success();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
