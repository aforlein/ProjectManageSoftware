-- ============================================
-- 项目管理系统 - 数据库建表脚本
-- ============================================

SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS project_manage
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE project_manage;

-- ============================================
-- 系统表
-- ============================================

-- 系统角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    role_name   VARCHAR(50)  NOT NULL COMMENT '角色名称',
    role_code   VARCHAR(50)  NOT NULL COMMENT '角色编码(ADMIN/PROJECT_MANAGER/MEMBER/VIEWER)',
    description VARCHAR(255) COMMENT '角色描述',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 系统用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    real_name   VARCHAR(50)  COMMENT '真实姓名',
    email       VARCHAR(100) COMMENT '邮箱',
    phone       VARCHAR(20)  COMMENT '手机号',
    avatar      VARCHAR(500) COMMENT '头像URL',
    role_id     BIGINT       COMMENT '角色ID',
    status      TINYINT      DEFAULT 1 COMMENT '状态(0禁用 1启用)',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      DEFAULT 0 COMMENT '逻辑删除(0未删除 1已删除)',
    UNIQUE KEY uk_username (username),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ============================================
-- 项目管理表
-- ============================================

-- 项目主表
CREATE TABLE IF NOT EXISTS pm_project (
    id                 BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_name       VARCHAR(100) NOT NULL COMMENT '项目名称',
    customer_name      VARCHAR(100) COMMENT '客户名称',
    longitude          DECIMAL(10,6) COMMENT '经度',
    latitude           DECIMAL(10,6) COMMENT '纬度',
    project_scope      TEXT         COMMENT '项目范围',
    project_target     TEXT         COMMENT '项目目标',
    contract_amount    DECIMAL(12,2) DEFAULT 0.00 COMMENT '合同金额',
    estimated_cost     DECIMAL(12,2) DEFAULT 0.00 COMMENT '预估成本',
    project_manager_id BIGINT       COMMENT '项目经理ID',
    status             VARCHAR(20)  DEFAULT 'planning' COMMENT '状态(planning/in_progress/completed/closed/on_hold)',
    start_date         DATE         COMMENT '开始日期',
    end_date           DATE         COMMENT '结束日期',
    create_by          BIGINT       COMMENT '创建人ID',
    create_time        DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time        DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted            TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_status (status),
    INDEX idx_project_manager (project_manager_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目主表';

-- 项目里程碑表
CREATE TABLE IF NOT EXISTS pm_project_milestone (
    id              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id      BIGINT       NOT NULL COMMENT '项目ID',
    milestone_name  VARCHAR(100) NOT NULL COMMENT '里程碑名称',
    description     TEXT         COMMENT '描述',
    planned_date    DATE         COMMENT '计划完成日期',
    actual_date     DATE         COMMENT '实际完成日期',
    status          VARCHAR(20)  DEFAULT 'pending' COMMENT '状态(pending/completed/delayed)',
    sort_order      INT          DEFAULT 0 COMMENT '排序号',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目里程碑表';

-- 项目详细计划表
CREATE TABLE IF NOT EXISTS pm_project_plan (
    id              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id      BIGINT       NOT NULL COMMENT '项目ID',
    plan_name       VARCHAR(100) NOT NULL COMMENT '计划名称',
    description     TEXT         COMMENT '描述',
    start_date      DATE         COMMENT '开始日期',
    end_date        DATE         COMMENT '结束日期',
    status          VARCHAR(20)  DEFAULT 'pending' COMMENT '状态(pending/in_progress/completed)',
    progress        INT          DEFAULT 0 COMMENT '进度百分比(0-100)',
    parent_id       BIGINT       COMMENT '父计划ID(支持层级)',
    sort_order      INT          DEFAULT 0 COMMENT '排序号',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id),
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目详细计划表';

-- 迭代任务表
CREATE TABLE IF NOT EXISTS pm_iteration_task (
    id              BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id      BIGINT       NOT NULL COMMENT '项目ID',
    plan_id         BIGINT       COMMENT '关联计划ID',
    task_name       VARCHAR(100) NOT NULL COMMENT '任务名称',
    description     TEXT         COMMENT '任务描述',
    assignee_id     BIGINT       COMMENT '负责人ID(关联sys_user)',
    priority        VARCHAR(20)  DEFAULT 'medium' COMMENT '优先级(high/medium/low)',
    status          VARCHAR(20)  DEFAULT 'todo' COMMENT '状态(todo/in_progress/done)',
    story_points    INT          DEFAULT 0 COMMENT '故事点/工时预估',
    start_date      DATE         COMMENT '开始日期',
    end_date        DATE         COMMENT '截止日期',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id),
    INDEX idx_plan_id (plan_id),
    INDEX idx_assignee_id (assignee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='迭代任务表';

-- 问题日志表
CREATE TABLE IF NOT EXISTS pm_issue_log (
    id                BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id        BIGINT       NOT NULL COMMENT '项目ID',
    issue_title       VARCHAR(200) NOT NULL COMMENT '问题标题',
    issue_type        VARCHAR(50)  COMMENT '问题类型(bug/requirement/improvement/other)',
    issue_description TEXT         COMMENT '问题描述',
    priority          VARCHAR(20)  DEFAULT 'medium' COMMENT '优先级(urgent/high/medium/low)',
    status            VARCHAR(20)  DEFAULT 'open' COMMENT '状态(open/in_progress/resolved/closed/reopened)',
    root_cause        TEXT         COMMENT '根因分析',
    solution          TEXT         COMMENT '解决方案',
    owner_id          BIGINT       COMMENT '负责人ID',
    create_by         BIGINT       COMMENT '创建人ID',
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted           TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id),
    INDEX idx_owner_id (owner_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='问题日志表';

-- 项目风险表
CREATE TABLE IF NOT EXISTS pm_project_risk (
    id                BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id        BIGINT       NOT NULL COMMENT '项目ID',
    risk_title        VARCHAR(200) NOT NULL COMMENT '风险标题',
    risk_type         VARCHAR(50)  COMMENT '风险类型(tech/resource/schedule/quality/other)',
    risk_level        VARCHAR(20)  DEFAULT 'medium' COMMENT '风险等级(high/medium/low)',
    probability_level VARCHAR(20)  COMMENT '概率等级(high/medium/low)',
    impact_level      VARCHAR(20)  COMMENT '影响等级(high/medium/low)',
    risk_description  TEXT         COMMENT '风险描述',
    response_strategy TEXT         COMMENT '应对策略',
    contingency_plan  TEXT         COMMENT '应急计划',
    status            VARCHAR(20)  DEFAULT 'identified' COMMENT '状态(identified/assessed/mitigated/monitoring/closed)',
    owner_id          BIGINT       COMMENT '负责人ID',
    create_by         BIGINT       COMMENT '创建人ID',
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted           TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目风险表';

-- 知识库表
CREATE TABLE IF NOT EXISTS pm_knowledge (
    id          BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id  BIGINT       NOT NULL COMMENT '项目ID',
    title       VARCHAR(200) NOT NULL COMMENT '知识标题',
    content     MEDIUMTEXT   COMMENT '知识内容',
    type        VARCHAR(50)  COMMENT '类型(tech/business/process/experience/other)',
    tags        VARCHAR(500) COMMENT '标签(逗号分隔)',
    create_by   BIGINT       COMMENT '创建人ID',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='知识库表';

-- 沟通记录表
CREATE TABLE IF NOT EXISTS pm_communication (
    id                 BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id         BIGINT       NOT NULL COMMENT '项目ID',
    communication_date DATE         COMMENT '沟通日期',
    type               VARCHAR(50)  COMMENT '沟通类型(meeting/phone/email/wechat/other)',
    participant        VARCHAR(200) COMMENT '参与人',
    content            TEXT         COMMENT '沟通内容',
    notes              TEXT         COMMENT '备注',
    create_by          BIGINT       COMMENT '创建人ID',
    create_time        DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time        DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted            TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='沟通记录表';

-- 周报表
CREATE TABLE IF NOT EXISTS pm_week_report (
    id          BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id  BIGINT       NOT NULL COMMENT '项目ID',
    week_start  DATE         NOT NULL COMMENT '周开始日期(周一)',
    week_end    DATE         NOT NULL COMMENT '周结束日期(周日)',
    content     MEDIUMTEXT   COMMENT '本周工作内容',
    plan_next   MEDIUMTEXT   COMMENT '下周计划',
    issues      MEDIUMTEXT   COMMENT '问题和风险',
    create_by   BIGINT       COMMENT '创建人ID',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id),
    UNIQUE KEY uk_project_week (project_id, week_start)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='周报表';

-- 部署记录表
CREATE TABLE IF NOT EXISTS pm_deploy_record (
    id             BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id     BIGINT       NOT NULL COMMENT '项目ID',
    deploy_version VARCHAR(50)  NOT NULL COMMENT '部署版本',
    deploy_env     VARCHAR(50)  COMMENT '部署环境(dev/test/staging/prod)',
    deploy_date    DATETIME     COMMENT '部署时间',
    status         VARCHAR(20)  DEFAULT 'pending' COMMENT '状态(pending/success/failed/rollback)',
    deploy_notes   TEXT         COMMENT '部署说明',
    create_by      BIGINT       COMMENT '创建人ID',
    create_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted        TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部署记录表';

-- 关系人表
CREATE TABLE IF NOT EXISTS pm_stakeholder (
    id          BIGINT       PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id  BIGINT       NOT NULL COMMENT '项目ID',
    name        VARCHAR(50)  NOT NULL COMMENT '姓名',
    role        VARCHAR(50)  COMMENT '角色(product_owner/developer/tester/designer/customer/other)',
    department  VARCHAR(100) COMMENT '部门',
    phone       VARCHAR(20)  COMMENT '联系电话',
    email       VARCHAR(100) COMMENT '邮箱',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT      DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关系人表';

-- ============================================
-- 第二轮优化新增结构
-- ============================================

-- 项目新增字段：项目地点地址
ALTER TABLE pm_project ADD COLUMN location VARCHAR(200) COMMENT '项目地点地址';

-- 迭代任务新增字段：任务类型
ALTER TABLE pm_iteration_task ADD COLUMN task_type VARCHAR(20) DEFAULT 'requirement' COMMENT '任务类型(optimization/requirement/defect/dependency/other)';

-- 迭代任务新增字段：模块分类ID
ALTER TABLE pm_iteration_task ADD COLUMN module_id BIGINT COMMENT '模块分类ID';

-- 任务模块分类表
CREATE TABLE IF NOT EXISTS pm_task_module (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    project_id  BIGINT NOT NULL COMMENT '项目ID',
    module_name VARCHAR(100) NOT NULL COMMENT '模块名称',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务模块分类表';
