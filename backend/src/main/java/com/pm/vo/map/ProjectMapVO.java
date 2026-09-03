package com.pm.vo.map;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProjectMapVO {
    private Long id;
    private String projectName;
    private String customerName;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String status;
}
