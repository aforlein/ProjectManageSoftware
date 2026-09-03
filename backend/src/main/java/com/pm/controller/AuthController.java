package com.pm.controller;

import com.pm.common.R;
import com.pm.dto.auth.LoginRequest;
import com.pm.service.AuthService;
import com.pm.vo.auth.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        LoginVO vo = authService.login(request);
        return R.success(vo);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public R<Map<String, String>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return R.success(Map.of("userId", auth.getName()));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public R<Void> logout() {
        return R.success();
    }
}
