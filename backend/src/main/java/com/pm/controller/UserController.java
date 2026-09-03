package com.pm.controller;

import com.pm.common.PageResult;
import com.pm.common.R;
import com.pm.dto.system.PasswordChangeDTO;
import com.pm.dto.system.UserDTO;
import com.pm.dto.system.UserPageQuery;
import com.pm.service.UserService;
import com.pm.vo.system.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/system/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @Operation(summary = "分页查询用户")
    @GetMapping("/page")
    public R<PageResult<UserVO>> page(UserPageQuery query) {
        return R.success(userService.page(query));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public R<Long> create(@Valid @RequestBody UserDTO dto) {
        return R.success(userService.create(dto));
    }

    @Operation(summary = "编辑用户")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        userService.update(id, dto);
        return R.success();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return R.success();
    }

    @Operation(summary = "启用/禁用用户")
    @PutMapping("/{id}/status")
    public R<Void> toggleStatus(@PathVariable Long id) {
        userService.toggleStatus(id);
        return R.success();
    }

    @Operation(summary = "修改密码")
    @PutMapping("/{id}/password")
    public R<Void> changePassword(@PathVariable Long id, @Valid @RequestBody PasswordChangeDTO dto) {
        userService.changePassword(id, dto);
        return R.success();
    }
}
