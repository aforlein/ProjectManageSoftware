package com.pm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("pm_knowledge")
public class PmKnowledge {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long projectId;
    private String title;
    private String content;
    private String type;
    private String tags;
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
