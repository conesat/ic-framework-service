# IC Framework Spring AOT 项目结构

## 目录结构

```
ic-framework-spring-aot/
├── .gitignore                    # Git 忽略文件配置
├── pom.xml                       # Maven 项目配置
├── Dockerfile                    # Docker 容器化配置
├── README.md                     # 项目说明文档
├── SUMMARY.md                    # 项目总结报告
├── PROJECT_STRUCTURE.md          # 项目结构文档（本文件）
├── build.sh                      # 构建脚本
├── docker-build.sh               # Docker 构建脚本
├── test.sh                       # 测试脚本
├── performance-test.sh           # 性能测试脚本
├── src/                          # 源代码目录
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ic/framework/aot/
│   │   │       ├── IcFrameworkAotApplication.java    # 主应用程序类
│   │   │       ├── controller/
│   │   │       │   └── HelloController.java          # REST 控制器
│   │   │       └── config/
│   │   │           └── AppConfig.java                # 应用配置类
│   │   └── resources/
│   │       └── application.yml                       # 应用配置文件
│   └── test/
│       └── java/
│           └── com/ic/framework/aot/
│               ├── IcFrameworkAotApplicationTests.java    # 应用测试类
│               └── controller/
│                   └── HelloControllerTest.java           # 控制器测试类
└── target/                       # 构建输出目录
    ├── ic-framework-spring-aot-1.0.0.jar              # JAR 包
    ├── ic-framework-spring-aot-1.0.0.jar.original     # 原始 JAR 包
    └── ic-framework-spring-aot                        # 原生可执行文件
```

## 文件说明

### 配置文件
- **`.gitignore`**: Git 版本控制忽略文件配置
- **`pom.xml`**: Maven 项目配置文件，包含依赖和构建配置
- **`Dockerfile`**: Docker 容器化配置文件
- **`application.yml`**: Spring Boot 应用配置文件

### 文档文件
- **`README.md`**: 项目详细说明和使用指南
- **`SUMMARY.md`**: 项目完成总结和性能测试报告
- **`PROJECT_STRUCTURE.md`**: 项目结构说明（本文件）

### 脚本文件
- **`build.sh`**: 项目构建脚本，支持 JVM 和原生镜像构建
- **`docker-build.sh`**: Docker 镜像构建脚本
- **`test.sh`**: 应用测试脚本
- **`performance-test.sh`**: 性能对比测试脚本

### 源代码文件
- **`IcFrameworkAotApplication.java`**: Spring Boot 主应用程序类
- **`HelloController.java`**: RESTful API 控制器
- **`AppConfig.java`**: 应用配置类
- **`*Tests.java`**: 单元测试类

### 构建产物
- **`ic-framework-spring-aot-1.0.0.jar`**: Spring Boot 可执行 JAR 包
- **`ic-framework-spring-aot`**: GraalVM 原生可执行文件

## 清理说明

项目已经过清理，移除了以下不必要的文件：

### 已移除的文件
- ❌ `app.log`, `jvm-app.log`, `native-app.log` - 运行时日志文件
- ❌ `Dockerfile.simple` - 重复的 Docker 配置文件（已合并到 `Dockerfile`）
- ❌ `target/classes/` - 编译后的类文件
- ❌ `target/generated-sources/` - 生成的源代码
- ❌ `target/generated-test-sources/` - 生成的测试源代码
- ❌ `target/graalvm-reachability-metadata/` - GraalVM 元数据
- ❌ `target/maven-archiver/` - Maven 归档文件
- ❌ `target/maven-status/` - Maven 状态文件
- ❌ `target/spring-aot/` - Spring AOT 生成文件
- ❌ `target/surefire-reports/` - 测试报告
- ❌ `target/test-classes/` - 测试类文件
- ❌ `target/test-ids/` - 测试 ID 文件

### 保留的文件
- ✅ 所有源代码文件
- ✅ 所有配置文件
- ✅ 所有脚本文件
- ✅ 所有文档文件
- ✅ 最终构建产物（JAR 包和原生可执行文件）

## 使用建议

1. **开发阶段**: 使用 `mvn clean compile test` 进行开发和测试
2. **构建阶段**: 使用 `./build.sh` 构建 JVM 版本，使用 `./build.sh native` 构建原生版本
3. **测试阶段**: 使用 `./test.sh` 进行功能测试，使用 `./performance-test.sh` 进行性能测试
4. **部署阶段**: 使用 `./docker-build.sh` 构建 Docker 镜像

## 注意事项

- `target/` 目录包含构建产物，通常不需要提交到版本控制
- 运行时生成的日志文件会被 `.gitignore` 自动忽略
- 原生镜像构建需要 GraalVM 环境支持
- Docker 构建需要 Docker 环境支持 