package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("pm_communication")
public class PmCommunication {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private LocalDate communicationDate;
    private String type;
    private String participant;
    private String content;
    private String notes;
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
