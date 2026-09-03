package com.pm.controller;

import com.pm.common.R;
import com.pm.service.MapService;
import com.pm.vo.map.ProjectMapVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "项目地图")
@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @Operation(summary = "获取有坐标的项目")
    @GetMapping("/projects")
    public R<List<ProjectMapVO>> getProjects() {
        return R.success(mapService.getProjects());
    }
}
