package com.pm.controller;

import com.pm.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public R<String> health() {
        return R.success("OK");
    }
}
