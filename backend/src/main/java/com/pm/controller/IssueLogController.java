package com.pm.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pm.common.PageResult;
import com.pm.common.R;
import com.pm.dto.issue.IssueDTO;
import com.pm.dto.issue.IssuePageQuery;
import com.pm.entity.PmIssueLog;
import com.pm.mapper.PmIssueLogMapper;
import com.pm.vo.issue.IssueVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "问题日志（全局）")
@RestController
@RequestMapping("/api/issue")
@RequiredArgsConstructor
public class IssueLogController {
    private final PmIssueLogMapper mapper;

    @Operation(summary = "分页查询问题")
    @GetMapping("/page")
    public R<PageResult<IssueVO>> page(IssuePageQuery query) {
        Page<PmIssueLog> page = new Page<>(query.getPage(), query.getSize());
        LambdaQueryWrapper<PmIssueLog> wrapper = new LambdaQueryWrapper<>();
        if (query.getProjectId() != null) wrapper.eq(PmIssueLog::getProjectId, query.getProjectId());
        if (StrUtil.isNotBlank(query.getPriority())) wrapper.eq(PmIssueLog::getPriority, query.getPriority());
        if (StrUtil.isNotBlank(query.getStatus())) wrapper.eq(PmIssueLog::getStatus, query.getStatus());
        if (StrUtil.isNotBlank(query.getKeyword())) wrapper.like(PmIssueLog::getIssueTitle, query.getKeyword());
        wrapper.orderByDesc(PmIssueLog::getCreateTime);
        Page<PmIssueLog> result = mapper.selectPage(page, wrapper);
        List<IssueVO> voList = result.getRecords().stream().map(r -> { IssueVO vo = new IssueVO(); BeanUtils.copyProperties(r, vo); return vo; }).collect(Collectors.toList());
        return R.success(PageResult.of(result, voList));
    }

    @GetMapping("/{id}")
    public R<PmIssueLog> getById(@PathVariable Long id) { return R.success(mapper.selectById(id)); }

    @PostMapping
    public R<Long> create(@Valid @RequestBody IssueDTO dto) { PmIssueLog e = new PmIssueLog(); BeanUtils.copyProperties(dto, e); mapper.insert(e); return R.success(e.getId()); }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody IssueDTO dto) { PmIssueLog e = new PmIssueLog(); e.setId(id); BeanUtils.copyProperties(dto, e); mapper.updateById(e); return R.success(); }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) { mapper.deleteById(id); return R.success(); }
}
