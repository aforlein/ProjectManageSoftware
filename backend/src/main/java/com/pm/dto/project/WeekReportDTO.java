package com.pm.dto.project;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WeekReportDTO {
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private String content;
    private String planNext;
    private String issues;
}
