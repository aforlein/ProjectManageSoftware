package com.pm.controller;

import com.pm.common.PageResult;
import com.pm.common.R;
import com.pm.dto.project.ProjectDTO;
import com.pm.dto.project.ProjectPageQuery;
import com.pm.service.ProjectService;
import com.pm.vo.project.ProjectVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "项目管理")
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "分页查询项目")
    @GetMapping("/page")
    public R<PageResult<ProjectVO>> page(ProjectPageQuery query) {
        return R.success(projectService.page(query));
    }

    @Operation(summary = "获取项目详情")
    @GetMapping("/{id}")
    public R<ProjectVO> getById(@PathVariable Long id) {
        return R.success(projectService.getById(id));
    }

    @Operation(summary = "新增项目")
    @PostMapping
    public R<Long> create(@Valid @RequestBody ProjectDTO dto) {
        return R.success(projectService.create(dto));
    }

    @Operation(summary = "编辑项目")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody ProjectDTO dto) {
        projectService.update(id, dto);
        return R.success();
    }

    @Operation(summary = "删除项目")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return R.success();
    }
}
