package com.pm.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.PageResult;
import com.pm.common.R;
import com.pm.dto.risk.RiskDTO;
import com.pm.dto.risk.RiskPageQuery;
import com.pm.entity.PmProjectRisk;
import com.pm.mapper.PmProjectRiskMapper;
import com.pm.vo.risk.RiskVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "风险管理（全局）")
@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
public class RiskController {
    private final PmProjectRiskMapper mapper;

    @Operation(summary = "分页查询风险")
    @GetMapping("/page")
    public R<PageResult<RiskVO>> page(RiskPageQuery query) {
        Page<PmProjectRisk> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<PmProjectRisk> wrapper = new LambdaQueryWrapper<>();
        if (query.getProjectId() != null) wrapper.eq(PmProjectRisk::getProjectId, query.getProjectId());
        if (StrUtil.isNotBlank(query.getRiskLevel())) wrapper.eq(PmProjectRisk::getRiskLevel, query.getRiskLevel());
        if (StrUtil.isNotBlank(query.getStatus())) wrapper.eq(PmProjectRisk::getStatus, query.getStatus());
        if (StrUtil.isNotBlank(query.getKeyword())) wrapper.like(PmProjectRisk::getRiskTitle, query.getKeyword());
        wrapper.orderByDesc(PmProjectRisk::getCreateTime);
        Page<PmProjectRisk> result = mapper.selectPage(page, wrapper);
        List<RiskVO> voList = result.getRecords().stream().map(r -> { RiskVO vo = new RiskVO(); BeanUtils.copyProperties(r, vo); return vo; }).collect(Collectors.toList());
        return R.success(PageResult.of(result, voList));
    }

    @GetMapping("/{id}")
    public R<PmProjectRisk> getById(@PathVariable Long id) { return R.success(mapper.selectById(id)); }

    @PostMapping
    public R<Long> create(@Valid @RequestBody RiskDTO dto) { PmProjectRisk e = new PmProjectRisk(); BeanUtils.copyProperties(dto, e); mapper.insert(e); return R.success(e.getId()); }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody RiskDTO dto) { PmProjectRisk e = new PmProjectRisk(); e.setId(id); BeanUtils.copyProperties(dto, e); mapper.updateById(e); return R.success(); }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
