package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("pm_deploy_record")
public class PmDeployRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String deployVersion;
    private String deployEnv;
    private LocalDateTime deployDate;
    private String status;
    private String deployNotes;
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
