# Docker 部署总结 V1.0

日期：2026-07-15

---

# 零、Docker 部署原理

## 0.1 核心概念

| 概念 | 比喻 | 说明 |
|---|---|---|
| **Dockerfile** | 菜谱 | 文本文件，描述如何构建镜像（用什么基础环境、安装什么依赖、复制哪些文件、执行什么命令） |
| **镜像（Image）** | 光盘 ISO | 根据 Dockerfile 生成的只读模板，包含运行应用所需的全部文件和环境 |
| **容器（Container）** | 运行中的程序 | 镜像的运行时实例，拥有独立的文件系统、网络、进程空间 |
| **Docker Engine** | 操作系统内核 | 管理镜像和容器的后台服务，负责实际的构建、运行、网络、存储 |
| **Registry** | 应用商店 | 镜像仓库，如 Docker Hub、阿里云容器镜像服务。Docker Engine 从 Registry 拉取（pull）基础镜像 |
| **docker-compose** | 乐团指挥 | 编排工具，一条命令同时启动多个关联的容器 |

## 0.2 代码工程 → 镜像 → 容器的完整过程

以下以本项目的后端服务为例：

```
                                       Docker Hub (Registry)
源码                         │         ┌─────────────────┐
F:\...\backend               │         │ maven:3.9-      │
├── pom.xml                  │         │ eclipse-        │
├── src/                     │         │ temurin-17      │
│   ├── main/java/...        │ ① pull  │ eclipse-        │
│   └── main/resources/      │←────────│ temurin-17-jre  │
│                            │         └─────────────────┘
      ↓ ② docker build
┌──────────────────────┐     │
│ Dockerfile.backend   │     │         Docker Engine 本地存储
│                      │     │         ┌─────────────────────┐
│  Stage 1: Build      │     │         │ 基础镜像缓存        │
│  FROM maven:3.9...   │──②──┤         │ ├── maven:3.9-...   │
│  COPY pom.xml .      │     │         │ ├── temurin:17-jre  │
│  RUN mvn package     │     │ ③ 生成  │                     │
│                      │     │←────────│ 项目镜像            │
│  Stage 2: Run        │     │         │ pm-backend:latest   │
│  FROM temurin:17-jre │     │         │ (约 200MB)          │
│  COPY --from=build   │     │         └─────────────────────┘
│  ENTRYPOINT java -jar│     │
└──────────────────────┘     │
                             │ ④ docker run / docker-compose up
      ↓                      │
┌──────────────────────┐     │         ┌─────────────────────┐
│  容器 pm-backend     │     │         │  容器 pm-backend    │
│                      │     │         │  ├── 独立文件系统   │
│  JRE 17 运行环境     │     │         │  ├── 独立网络栈     │
│  app.jar 应用代码    │     │         │  ├── CPU/内存限制   │
│  端口 8080           │←⑤──┤         │  └── PID 1: java    │
│                      │     │         │      -jar app.jar   │
└──────────────────────┘     │         └─────────────────────┘
```

### 步骤详解

**① pull（拉取基础镜像）**

首次构建时，Docker Engine 检查本地是否已有 `maven:3.9-eclipse-temurin-17` 和 `eclipse-temurin:17-jre-alpine` 镜像。如果没有，从 Docker Hub（或配置的镜像加速器）下载。首次较慢（几百 MB），之后缓存复用。

**② docker build（构建镜像）**

Docker Engine 逐行执行 Dockerfile 中的指令。本项目的后端 Dockerfile 采用**多阶段构建**：

- **Stage 1（编译阶段）**：用 `maven:3.9-eclipse-temurin-17` 为基础，将 `pom.xml` 和 `src/` 复制进容器 → 执行 `mvn package` → 生成 `target/*.jar`
- **Stage 2（运行阶段）**：换用轻量的 `eclipse-temurin:17-jre-alpine` 为基础（只有 JRE 没有 JDK，镜像更小），从 Stage 1 只复制 JAR 文件过来 → 设置启动命令

```dockerfile
# Stage 1: 编译（体积 ~600MB，仅构建时使用）
FROM maven:3.9-eclipse-temurin-17 AS build
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests -q -B

# Stage 2: 运行（体积 ~200MB，最终镜像）
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

多阶段构建的好处：最终镜像不包含 Maven、源码等编译工具，体积从 ~600MB 降到 ~200MB。

**③ 生成镜像并存储**

构建成功后，Docker Engine 将镜像（一个分层的只读文件系统）存储在本地的镜像仓库中。可以通过 `docker images` 查看。镜像名由 `docker-compose.yml` 中的 `build.context` 决定，本项目中为 `projectmanagesoftware-backend`。

**④ docker run / docker-compose up（启动容器）**

Docker Engine 基于镜像创建一个**可写层**，在上面运行容器。`docker-compose.yml` 定义了：

```yaml
backend:
  environment:          # 注入环境变量（数据库地址、密码等配置）
    DB_HOST: mysql
  ports:
    - "8080:8080"       # 宿主机:容器 端口映射
  depends_on:           # 启动顺序依赖
    mysql:
      condition: service_healthy  # 等 MySQL 健康检查通过后才启动
  networks:
    - pm-network        # 加入自定义网络，容器间用服务名互相访问
```

**⑤ 容器运行中**

容器内的 Java 进程（PID 1）监听 8080 端口。Docker Engine 将宿主机的 8080 端口流量转发到容器内的 8080 端口。容器通过 `pm-network` 网络用 `mysql:3306`（服务名即 DNS）访问数据库容器，无需知道 MySQL 的 IP 地址。

## 0.3 镜像如何进入 Docker Engine

镜像的传递有三种路径：

### 路径 A：本地构建（本项目采用）

```
Dockerfile → docker build → Docker Engine 本地镜像存储
```

镜像只存在于构建它的那台机器上，不经过网络传输。

### 路径 B：Registry 发布

```
本地构建 → docker push → Docker Hub/私有 Registry → docker pull → 服务器 Docker Engine
```

适用于多台服务器部署。在 CI/CD 流水线中，构建机 build 完 push 到 Registry，生产服务器 pull 下来运行。

### 路径 C：docker save / load

```
docker save -o image.tar image:tag    # 导出为文件
→ 传输 tar 文件到目标机器 →
docker load -i image.tar              # 导入到目标 Docker Engine
```

适用于离线环境或内网部署。

### 镜像在 Windows 上的实际存储位置

Docker Desktop for Windows（WSL2 模式）的镜像并非直接存放在 Windows 文件系统中，而是封装在 WSL2 的虚拟磁盘内：

| 层级 | 路径 | 说明 |
|---|---|---|
| Windows 宿主机 | `C:\Users\69113\AppData\Local\Docker\wsl\disk\` | 存放 `.vhdx` 虚拟磁盘文件 |
| WSL2 内部 | `/var/lib/docker/` | 虚拟磁盘内的 Linux 标准路径 |

`.vhdx` 文件是一个完整的 Linux 文件系统，Docker 的所有数据（镜像层、容器可写层、数据卷、网络配置）都在里面。

**查看方式**：

```bash
# 进入 WSL2 查看 Docker 存储详情
wsl -d docker-desktop
ls /var/lib/docker/
# 目录结构:
#   containers/    ← 容器元数据
#   image/         ← 镜像元数据（layer ID、tag 映射等）
#   overlay2/      ← 镜像分层文件系统（实际占用空间最大的地方）
#   volumes/       ← docker-compose 中 volumes 定义的数据卷
#   network/       ← 自定义网络配置
```

**关键理解**：每次 `docker build` 生成的镜像层就存储在 `overlay2/` 下，每个 layer 对应一个由 SHA256 命名的目录。多个镜像共享相同的基础层（如 `eclipse-temurin:17-jre-alpine`），不会重复占用空间。

## 0.5 Docker Compose 如何编排多容器

```
docker-compose.yml
    │
    ├── service: mysql      → 容器 pm-mysql
    │     image: mysql:8.0     pull 官方镜像
    │     volumes:             挂载 ./sql/ → 自动建库建表
    │     healthcheck:         mysqladmin ping → 确认就绪
    │
    ├── service: backend    → 容器 pm-backend
    │     build: ./backend    执行 Dockerfile.backend
    │     depends_on:         mysql healthy → 等数据库就绪
    │     environment:        DB_HOST=mysql → 服务名即地址
    │
    └── service: frontend   → 容器 pm-frontend
          build: ./frontend   执行 Dockerfile.frontend
          depends_on:         backend → 等后端就绪
```

三个容器共享 `pm-network` 桥接网络，彼此通过服务名（`mysql`、`backend`、`frontend`）互相访问。宿主机访问前端只需 `http://localhost:80`，nginx 将 `/api/` 请求反向代理到 `backend:8080`。

---

# 一、部署依赖的环境和组件

## 1.1 宿主机要求

| 组件 | 版本 | 用途 |
|---|---|---|
| Docker Desktop | 29.x+ | 容器运行时 |
| WSL2 | 随 Docker Desktop 安装 | Windows 上的 Linux 内核支持 |
| 操作系统 | Windows 10/11, macOS, Linux | — |
| 内存 | ≥ 4GB（3个容器运行） | — |
| 磁盘 | ≥ 2GB（镜像 + 数据卷） | — |

## 1.2 容器架构

```
┌─────────────────────────────────────────────┐
│                  Docker                      │
│                                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │  MySQL   │  │  Backend │  │ Frontend │  │
│  │  8.0     │  │  JRE 17  │  │ nginx    │  │
│  │  :3306   │  │  :8080   │  │  :80     │  │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘  │
│       │             │             │         │
│       └─────────────┴─────────────┘         │
│               pm-network                    │
└─────────────────────────────────────────────┘
         ↓              ↓            ↓
     3307:3306      8080:8080     80:80
    (宿主机端口映射)
```

## 1.3 项目文件结构

```
project-manage-system/
├── docker-compose.yml              # 编排文件
├── docker/
│   ├── Dockerfile.backend          # 后端镜像（Maven编译 → JRE运行）
│   ├── Dockerfile.frontend         # 前端镜像（Node构建 → nginx托管）
│   └── nginx.conf                  # nginx配置（SPA路由 + /api反向代理）
├── frontend/
│   └── nginx.conf                  # 前端构建上下文用（与docker/nginx.conf相同）
├── backend/
│   └── src/main/java/com/pm/controller/HealthController.java  # 健康检查端点
├── sql/
│   ├── 01_ddl.sql                  # 建表脚本
│   └── 02_seed.sql                 # 种子数据
└── ...
```

## 1.4 关键配置

| 配置项 | 来源 | 默认值 |
|---|---|---|
| `DB_HOST` | 环境变量 | `localhost`（宿主机）/ `mysql`（Docker内） |
| `DB_PORT` | 环境变量 | `3306` |
| `DB_NAME` | 环境变量 | `project_manage` |
| `DB_USER` | 环境变量 | `root` |
| `DB_PASSWORD` | 环境变量 | `root` |
| `JWT_SECRET` | 环境变量 | Base64 编码的长密钥 |

---

# 二、部署和运维指令

## 2.1 首次启动

```bash
# 项目根目录下
docker-compose up -d
```

首次执行会：
1. 拉取基础镜像（mysql:8.0, maven:3.9-eclipse-temurin-17, node:22-alpine, nginx:alpine）
2. 编译后端 JAR（Maven）
3. 构建前端静态文件（Vite）
4. 启动 MySQL → 等待健康 → 启动后端 → 启动前端

## 2.2 日常运维

```bash
# 查看运行状态
docker ps

# 查看日志
docker logs pm-backend
docker logs pm-frontend
docker logs pm-mysql

# 停止所有服务
docker-compose down

# 停止并清除数据卷（数据库重置）
docker-compose down -v

# 重启单个服务
docker-compose restart backend
docker-compose restart frontend

# 重新构建并启动（代码变更后）
docker-compose up -d --build
```

## 2.3 访问地址

| 服务 | URL |
|---|---|
| 前端 | `http://localhost:80` |
| 后端 API | `http://localhost:8080` |
| API 文档 | `http://localhost:8080/doc.html` |
| MySQL（外部连接） | `localhost:3307` |

## 2.4 端口说明

| 宿主机端口 | 容器内部端口 | 服务 |
|---|---|---|
| 80 | 80 | 前端 nginx |
| 8080 | 8080 | 后端 Spring Boot |
| 3307 | 3306 | MySQL（3306已由宿主机MySQL占用） |

---

# 三、部署中产生的问题和解决方案

## 问题 1：Docker Hub 无法拉取镜像

**现象**：
```
Error: dial tcp 69.63.184.142:443: connectex: A connection attempt failed
```

**原因**：国内网络环境无法直接访问 Docker Hub。

**解决**：配置国内镜像加速器。

Docker Desktop → 设置 → Docker Engine → 修改配置：

```json
{
  "registry-mirrors": [
    "https://docker.1ms.run",
    "https://docker.xuanyuan.me"
  ]
}
```

点击 **Apply & Restart** 生效。

---

## 问题 2：前端构建失败 — Node 版本不兼容

**现象**：
```
npm warn EBADENGINE required: { node: '^20.19.0 || >=22.12.0' }
npm run build did not complete successfully: exit code: 1
```

**原因**：`Dockerfile.frontend` 使用 `node:18-alpine`，但 Vite 8.x 要求 Node ≥ 20。

**解决**：将基础镜像改为 `node:22-alpine`。

```dockerfile
# 修改前
FROM node:18-alpine AS build

# 修改后
FROM node:22-alpine AS build
```

---

## 问题 3：前端构建失败 — nginx.conf 路径找不到

**现象**：
```
"/docker/nginx.conf": not found
```

**原因**：`Dockerfile.frontend` 中 `COPY docker/nginx.conf` 在构建上下文 `./frontend` 下找不到 `docker/` 目录。

**解决**：
1. 将 `docker/nginx.conf` 复制一份到 `frontend/nginx.conf`
2. Dockerfile 中改为 `COPY nginx.conf /etc/nginx/conf.d/default.conf`

```dockerfile
# 修改前
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf

# 修改后
COPY nginx.conf /etc/nginx/conf.d/default.conf
```

---

## 问题 4：后端启动崩溃 — JWT 密钥过短

**现象**：
```
WeakKeyException: The specified key byte array is 48 bits which is not secure
enough for any JWT HMAC-SHA algorithm. Consider using HS256.key() builder.
```

**原因**：`docker-compose.yml` 中 `JWT_SECRET` 默认值为 `changeit`，仅 48 bit，而 JWT HMAC-SHA 算法要求密钥 ≥ 256 bit。

**解决**：将默认值替换为 Base64 编码的长密钥（与 `application.yml` 一致）。

```yaml
# 修改前
JWT_SECRET: ${JWT_SECRET:-changeit}

# 修改后
JWT_SECRET: ${JWT_SECRET:-cHJvamVjdC1tYW5hZ2Utc3lzdGVtLWp3dC1zZWNyZXQta2V5LTIwMjQtdmVyeS1sb25nLXNlY3VyZS1rZXk=}
```

---

## 问题 5：Dashboard 接口报500 — 表结构不匹配

**现象**：
```
Unknown column 'task_type' in 'field list'
```

**原因**：`sql/01_ddl.sql` 中的表结构是初版设计，不包含后续优化阶段新增的列（`task_type`、`module_id`、`location`）和表（`pm_task_module`）。

**解决**：在 `sql/01_ddl.sql` 末尾补充 ALTER TABLE 和 CREATE TABLE 语句。

```sql
-- 优化阶段新增结构
ALTER TABLE pm_project ADD COLUMN location VARCHAR(200);
ALTER TABLE pm_iteration_task ADD COLUMN task_type VARCHAR(20) DEFAULT 'requirement';
ALTER TABLE pm_iteration_task ADD COLUMN module_id BIGINT;
CREATE TABLE IF NOT EXISTS pm_task_module ( ... );
```

---

## 问题 6：中文出现乱码

**现象**：
- 登录 API 返回 `realName` 为乱码
- 前端 Navbar 用户信息显示乱码

**原因**：MySQL 容器默认字符集为 `latin1`，`sql/` 目录下的中文内容被错误编码存储。

**解决方案（两处修改）**：

**a. MySQL 容器强制字符集**（`docker-compose.yml`）：

```yaml
mysql:
  environment:
    ...
  command: --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --init-connect='SET NAMES utf8mb4'
```

**b. SQL 文件开头声明编码**（`01_ddl.sql` 和 `02_seed.sql`）：

```sql
SET NAMES utf8mb4;
```

**c. 清理浏览器旧缓存**：

修复后，浏览器中残留的旧 localStorage 数据仍可能是乱码。清除 localStorage 后重新登录即可。

---

## 问题 7：地图 Marker 图标不显示

**现象**：项目分布地图上项目标记的小图钉图标不显示。

**原因**：Leaflet 的默认 Marker 图标 PNG 文件在 Vite 构建后路径解析失败（经典 Webpack/Vite + Leaflet 兼容问题）。

**解决**：在 `map/index.vue` 和 `MapPicker.vue` 中显式导入图标文件并设为默认。

```js
import markerIcon from 'leaflet/dist/images/marker-icon.png'
import markerIcon2x from 'leaflet/dist/images/marker-icon-2x.png'
import markerShadow from 'leaflet/dist/images/marker-shadow.png'

delete L.Icon.Default.prototype._getIconUrl
L.Icon.Default.mergeOptions({
  iconRetinaUrl: markerIcon2x,
  iconUrl: markerIcon,
  shadowUrl: markerShadow
})
```

---

# 四、后续重新更新部署的完整步骤

当代码有变更需要重新部署时，按以下步骤操作：

```bash
# 1. 确保 SQL DDL 文件包含所有表结构变更（检查是否有新增列/表）

# 2. 如果表结构有变更，需清除 MySQL 数据卷（否则旧表结构仍然存在）
docker-compose down -v

# 3. 重新构建并启动
docker-compose up -d --build

# 4. 检查所有容器状态
docker ps
# 确认三个容器均为 Up 且 backend/mysql 为 healthy

# 5. 验证关键 API
curl http://localhost:8080/api/health
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 6. 浏览器访问验证
# http://localhost:80
# 登录后检查中文显示是否正常、地图 Marker 是否显示
```

### 各场景的部署策略

| 变更类型 | 是否需要 `down -v` | 命令 |
|---|---|---|
| 仅前端代码 | 否 | `docker-compose up -d --build frontend` |
| 仅后端代码 | 否 | `docker-compose up -d --build backend` |
| SQL DDL 变更 | **是** | `docker-compose down -v && docker-compose up -d --build` |
| docker-compose.yml 变更 | 否 | `docker-compose up -d` |
| 新增环境变量 | 否 | `docker-compose up -d` |
