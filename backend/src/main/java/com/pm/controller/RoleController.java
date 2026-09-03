package com.pm.controller;

import com.pm.common.R;
import com.pm.dto.system.RoleDTO;
import com.pm.service.RoleService;
import com.pm.vo.system.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/system/role")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "获取所有角色（下拉列表）")
    @GetMapping("/list")
    public R<List<RoleVO>> list() {
        return R.success(roleService.listAll());
    }

    @Operation(summary = "角色列表")
    @GetMapping("/page")
    public R<List<RoleVO>> page() {
        return R.success(roleService.page());
    }

    @Operation(summary = "新增角色")
    @PostMapping
    public R<Long> create(@Valid @RequestBody RoleDTO dto) {
        return R.success(roleService.create(dto));
    }

    @Operation(summary = "编辑角色")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody RoleDTO dto) {
        roleService.update(id, dto);
        return R.success();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return R.success();
    }
}
