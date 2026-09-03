package com.pm.vo.dashboard;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DashboardVO {
    private long totalProjects;
    private long inProgressProjects;
    private long completedProjects;
    private long highRiskCount;
    private List<TaskItem> priorityTasks;
    private List<RiskItem> highRisks;

    @Data
    @Builder
    public static class TaskItem {
        private String taskName;
        private String projectName;
        private String endDate;
        private String assigneeName;
    }

    @Data
    @Builder
    public static class RiskItem {
        private String riskTitle;
        private String projectName;
        private String riskLevel;
    }
}
