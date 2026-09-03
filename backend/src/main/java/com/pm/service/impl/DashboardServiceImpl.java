package com.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pm.entity.*;
import com.pm.mapper.*;
import com.pm.service.DashboardService;
import com.pm.vo.dashboard.DashboardVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PmProjectMapper projectMapper;
    private final PmIterationTaskMapper taskMapper;
    private final PmProjectRiskMapper riskMapper;
    private final SysUserMapper userMapper;

    @Override
    public DashboardVO getStats() {
        long totalProjects = projectMapper.selectCount(null);
        long inProgress = projectMapper.selectCount(new LambdaQueryWrapper<PmProject>().eq(PmProject::getStatus, "in_progress"));
        long completed = projectMapper.selectCount(new LambdaQueryWrapper<PmProject>().eq(PmProject::getStatus, "completed"));
        long highRisk = riskMapper.selectCount(new LambdaQueryWrapper<PmProjectRisk>()
                .eq(PmProjectRisk::getRiskLevel, "high").ne(PmProjectRisk::getStatus, "closed"));

        List<PmIterationTask> tasks = taskMapper.selectList(new LambdaQueryWrapper<PmIterationTask>()
                .eq(PmIterationTask::getPriority, "high")
                .ne(PmIterationTask::getStatus, "done")
                .orderByAsc(PmIterationTask::getEndDate)
                .last("LIMIT 5"));
        List<DashboardVO.TaskItem> taskItems = tasks.stream().map(t -> {
            PmProject p = projectMapper.selectById(t.getProjectId());
            SysUser u = t.getAssigneeId() != null ? userMapper.selectById(t.getAssigneeId()) : null;
            return DashboardVO.TaskItem.builder()
                    .taskName(t.getTaskName())
                    .projectName(p != null ? p.getProjectName() : "")
                    .endDate(t.getEndDate() != null ? t.getEndDate().toString() : "")
                    .assigneeName(u != null ? u.getRealName() : "")
                    .build();
        }).collect(Collectors.toList());

        List<PmProjectRisk> risks = riskMapper.selectList(new LambdaQueryWrapper<PmProjectRisk>()
                .eq(PmProjectRisk::getRiskLevel, "high")
                .ne(PmProjectRisk::getStatus, "closed")
                .orderByDesc(PmProjectRisk::getCreateTime)
                .last("LIMIT 5"));
        List<DashboardVO.RiskItem> riskItems = risks.stream().map(r -> {
            PmProject p = projectMapper.selectById(r.getProjectId());
            return DashboardVO.RiskItem.builder()
                    .riskTitle(r.getRiskTitle())
                    .projectName(p != null ? p.getProjectName() : "")
                    .riskLevel(r.getRiskLevel())
                    .build();
        }).collect(Collectors.toList());

        return DashboardVO.builder()
                .totalProjects(totalProjects)
                .inProgressProjects(inProgress)
                .completedProjects(completed)
                .highRiskCount(highRisk)
                .priorityTasks(taskItems)
                .highRisks(riskItems)
                .build();
    }
}
