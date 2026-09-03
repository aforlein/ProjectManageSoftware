package com.pm.controller;

import com.pm.common.R;
import com.pm.service.DashboardService;
import com.pm.vo.dashboard.DashboardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "获取Dashboard统计数据")
    @GetMapping("/stats")
    public R<DashboardVO> stats() {
        return R.success(dashboardService.getStats());
    }
}
