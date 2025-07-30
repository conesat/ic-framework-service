# IC Framework Spring AOT

这是一个基于 Spring Boot 3.5.3 的 AOT (Ahead-of-Time) 编译示例项目，展示了如何使用 Spring Boot 的原生镜像功能。

## 项目特性

- ✅ Spring Boot 3.5.3
- ✅ Java 21
- ✅ AOT (Ahead-of-Time) 编译
- ✅ GraalVM 原生镜像
- ✅ Spring Boot Actuator
- ✅ RESTful API
- ✅ 单元测试
- ✅ Docker 支持

## 系统要求

- Java 21 或更高版本
- Maven 3.8+
- GraalVM 22.3+ (用于原生镜像构建)
- Docker (可选，用于容器化)

## 快速开始

### 1. 克隆项目

```bash
cd ic-framework-service/ic-framework-spring-aot
```

### 2. 构建项目

#### JVM 模式构建

```bash
# 编译和测试
mvn clean compile test

# 构建 JAR 包
mvn package
```

#### 原生镜像构建

```bash
# 使用构建脚本
./build.sh native

# 或直接使用 Maven
mvn -Pnative native:compile
```

### 3. 运行应用程序

#### JVM 模式运行

```bash
# 运行 JAR 包
java -jar target/ic-framework-spring-aot-1.0.0.jar
```

#### 原生镜像运行

```bash
# 运行原生可执行文件
./target/ic-framework-spring-aot
```

### 4. 测试应用程序

```bash
# 运行测试脚本
./test.sh
```

## API 端点

### 健康检查
```
GET /api/health
```

### 问候接口
```
GET /api/hello?name=World
```

### 系统信息
```
GET /api/info
```

### Actuator 端点
```
GET /actuator/health
GET /actuator/info
GET /actuator/metrics
```

## Docker 支持

### 构建 Docker 镜像

```bash
# 使用 Docker 构建脚本
./docker-build.sh

# 或直接使用 Docker
docker build -t ic-framework-spring-aot:latest .
```

### 运行 Docker 容器

```bash
# 前台运行
docker run -p 8080:8080 ic-framework-spring-aot:latest

# 后台运行
docker run -d -p 8080:8080 --name ic-aot-app ic-framework-spring-aot:latest
```

## 性能对比

| 指标 | JVM 模式 | 原生镜像 |
|------|----------|----------|
| 启动时间 | ~2-3 秒 | ~0.1-0.2 秒 |
| 内存使用 | ~100-200MB | ~50-80MB |
| 构建时间 | ~30 秒 | ~2-3 分钟 |

## 项目结构

```
ic-framework-spring-aot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ic/framework/aot/
│   │   │       ├── IcFrameworkAotApplication.java
│   │   │       ├── controller/
│   │   │       │   └── HelloController.java
│   │   │       └── config/
│   │   │           └── AppConfig.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/
│           └── com/ic/framework/aot/
│               ├── IcFrameworkAotApplicationTests.java
│               └── controller/
│                   └── HelloControllerTest.java
├── pom.xml
├── Dockerfile
├── build.sh
├── docker-build.sh
├── test.sh
└── README.md
```

## 配置说明

### application.yml

主要配置项：

- `server.port`: 服务器端口 (默认: 8080)
- `spring.aot.enabled`: 启用 AOT 编译 (默认: true)
- `management.endpoints.web.exposure.include`: 暴露的 Actuator 端点

### Maven 配置

- `native` profile: 用于构建原生镜像
- GraalVM Native Maven Plugin: 处理原生镜像构建
- Spring Boot Maven Plugin: 支持原生镜像构建

## 故障排除

### 常见问题

1. **GraalVM 未安装**
   ```bash
   # 下载并安装 GraalVM
   # 参考: https://www.graalvm.org/downloads/
   ```

2. **原生镜像构建失败**
   ```bash
   # 检查系统依赖
   # Ubuntu/Debian: sudo apt-get install build-essential libz-dev zlib1g-dev
   # macOS: xcode-select --install
   ```

3. **内存不足**
   ```bash
   # 增加 Maven 内存
   export MAVEN_OPTS="-Xmx4g -XX:MaxPermSize=512m"
   ```

## 开发指南

### 添加新的 API 端点

1. 在 `controller` 包中创建新的控制器
2. 添加相应的测试类
3. 更新文档

### 修改配置

1. 编辑 `application.yml` 文件
2. 在 `AppConfig` 类中添加新的配置 Bean
3. 重新构建和测试

## 许可证

本项目采用 MIT 许可证。

## 贡献

欢迎提交 Issue 和 Pull Request！

## 联系方式

- 项目地址: [GitHub Repository]
- 问题反馈: [Issues] 