-- ============================================
-- 项目管理系统 - 种子数据
-- 密码: admin123 (BCrypt加密)
-- ============================================

SET NAMES utf8mb4;
USE project_manage;

-- 默认角色
INSERT INTO sys_role (id, role_name, role_code, description) VALUES
(1, '管理员', 'ADMIN', '系统管理员，全部权限'),
(2, '项目经理', 'PROJECT_MANAGER', '项目管理权限'),
(3, '成员', 'MEMBER', '编辑权限'),
(4, '观察者', 'VIEWER', '只读权限');

-- 默认管理员用户 (密码: admin123)
INSERT INTO sys_user (id, username, password, real_name, email, phone, role_id, status) VALUES
(1, 'admin', '$2b$10$gV4aty1C9dFWA3ZXikMgN.QlejeAV/CMaMQeGXkNh.IpvxMBVAPpW', '系统管理员', 'admin@pm.com', '13800000000', 1, 1);
