# IC Framework Service

一个基于 [Ic Framework](https://github.com/conesat/ic-framework)的服务框架，提供完整的前后端解决方案，包括system模块提供了权限校验、岗位部门等模块基础模块。

在线文档 [http://icframework.chinahg.top](http://icframework.chinahg.top)

hotel是作者另外提供了酒店管理模块，包括酒店、房间、设施、房态等模块。

> 预览地址：[http://hotel.chinahg.top/](http://hotel.chinahg.top/) 
> 账号 test 密码 Aa23456

## 📋 项目概述

IC Framework Service 是一个现代化的企业级应用开发框架，采用微服务架构设计，提供了完整的前后端分离解决方案。项目包含多个业务模块和多端应用支持。

### 🏗️ 项目架构

```
ic-framework-service/
├── ic-framework-system/     # 系统核心模块
├── ic-framework-project/    # 项目模块
├── ic-framework-hotel/      # 酒店管理模块
├── app/                     # Flutter 移动端应用
├── _web/                    # Web 前端应用
│   ├── admin/              # 管理后台 (Vue 3 + TDesign)
│   └── uni/                # UniApp 跨平台应用
└── doc/                     # 项目文档

```

## 🚀 技术栈

### 后端技术栈
- **Java 21** - 最新 LTS 版本
- **Spring Boot 3.x** - 微服务框架
- **MyBatis** - 数据持久层框架
- **MySQL** - 关系型数据库
- **Redis** - 缓存和会话存储
- **Caffeine** - 本地缓存
- **Lettuce** - Redis 客户端
- **Maven** - 项目构建工具
- **Docker** - 容器化部署

### 前端技术栈
- **Vue 3** + **TypeScript** - 管理后台
- **TDesign Vue Next** - UI 组件库
- **Vite** - 构建工具
- **Pinia** - 状态管理
- **Vue Router** - 路由管理
- **Axios** - HTTP 客户端

### 移动端技术栈
- **Flutter** - 跨平台移动应用开发
- **Dart** - 编程语言
- **UniApp** - 跨平台小程序开发

## 📦 模块说明

### ic-framework-system
系统核心模块，提供基础功能：
- 用户认证与授权
- 组织架构管理（部门、职位）
- 用户管理
- 菜单权限管理
- 系统设置
- 字典管理
- 文件上传（阿里云 OSS）
- 在线用户管理

### ic-framework-project
最基本的项目模块，业务代码可以在这里完善

### ic-framework-hotel
酒店管理模块，提供酒店业务功能：
- 酒店信息管理
- 房间管理
- 楼栋管理
- 配套设施管理
- 房态管理
- 酒店用户管理

### Web 前端应用
- **admin**: 基于 Vue 3 + TDesign 的管理后台
- **uni**: 基于 UniApp 的跨平台应用

### 移动端应用
- **app**: 基于 Flutter 的原生移动应用

## 🛠️ 开发环境要求

### 后端环境
- **JDK 21** 或更高版本
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **Docker** (可选，用于容器化部署)

### 前端环境
- **Node.js 18.0+**
- **pnpm** (推荐) 或 **npm**

### 移动端环境
- **Flutter SDK 3.3.3+**
- **Dart SDK**
- **Android Studio** (Android 开发)
- **Xcode** (iOS 开发，仅 macOS)

## 🚀 快速开始

### 1. 克隆项目
```bash
git clone <repository-url>
cd ic-framework-service
```

### 2. 后端启动

#### 环境准备
1. 安装 JDK 21
2. 安装 MySQL 8.0+ 并创建数据库
3. 安装 Redis 6.0+
4. 配置数据库连接信息

#### 编译和运行
```bash
# 编译整个项目
mvn clean compile

# 运行系统模块 (端口: 9999)
cd ic-framework-project
mvn spring-boot:run

# 或运行酒店模块
cd ic-framework-hotel
mvn spring-boot:run
```

### 3. 前端启动

#### 管理后台
```bash
cd _web/admin
pnpm install
pnpm dev
```
访问地址: http://localhost:3002

#### UniApp 应用
```bash
cd _web/uni
# 使用 HBuilderX 打开项目并运行
```

### 4. 移动端启动

#### Flutter 应用
```bash
cd app
flutter pub get
flutter run
```

## 🐳 Docker 部署

项目支持 Docker 容器化部署：

### 构建镜像
```bash
# 构建项目模块
cd ic-framework-project
mvn clean package
docker build -t ic-framework-project .

# 构建酒店模块
cd ic-framework-hotel
mvn clean package
docker build -t ic-framework-hotel .
```

### 运行容器
```bash
# 运行项目模块
docker run -p 9999:9999 ic-framework-project

# 运行酒店模块
docker run -p 9999:9999 ic-framework-hotel
```

## 📝 配置说明

### 数据库配置
在 `application-dev.yml` 中配置数据库连接：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
```

### Redis 配置
```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: your_password
```

### 文件上传配置
支持阿里云 OSS 文件上传，需要配置相关参数。

## 🔧 开发指南

### 代码生成
项目集成了代码生成功能，可以快速生成 CRUD 代码：
- 实体类
- Mapper 接口
- Service 层
- Controller 层
- 前端页面


### 权限控制
使用基于角色的权限控制（RBAC）：
- 用户类型：系统用户、普通用户
- 菜单权限控制
- 接口权限验证

## 📱 功能特性

### 系统管理
- ✅ 用户管理
- ✅ 角色权限
- ✅ 组织架构
- ✅ 菜单管理
- ✅ 字典管理
- ✅ 系统设置
- ✅ 在线用户
- ✅ 文件管理

### 项目管理
- ✅ 项目创建
- ✅ 任务管理
- ✅ 进度跟踪
- ✅ 团队协作

### 酒店管理
- ✅ 酒店信息
- ✅ 房间管理
- ✅ 房态监控
- ✅ 配套设施
- ✅ 用户管理

### 多端支持
- ✅ Web 管理后台
- ✅ 移动端 App
- ✅ 小程序支持
- ✅ 响应式设计

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 [MIT License](LICENSE) 许可证。

## 📞 联系我们

如有问题或建议，请通过以下方式联系：
- 提交 Issue
- 发送邮件至: [1092501244@qq.com]
- 项目主页: [https://github.com/conesat/ic-framework-service]

---

⭐ 如果这个项目对您有帮助，请给我们一个 Star！