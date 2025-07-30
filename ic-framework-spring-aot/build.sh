#!/bin/bash

# IC Framework Spring AOT 构建脚本
# 支持 JVM 和原生镜像构建

set -e

echo "=== IC Framework Spring AOT 构建脚本 ==="

# 检查 Java 版本
echo "检查 Java 版本..."
java -version

# 检查 Maven 版本
echo "检查 Maven 版本..."
mvn -version

# 清理之前的构建
echo "清理之前的构建..."
mvn clean

# 编译和测试
echo "编译和测试..."
mvn compile test

# 构建 JAR 包
echo "构建 JAR 包..."
mvn package -DskipTests

# 检查是否构建原生镜像
if [ "$1" = "native" ]; then
    echo "构建原生镜像..."
    
    # 检查 GraalVM 是否可用
    if ! command -v native-image &> /dev/null; then
        echo "错误: 未找到 native-image 命令，请确保已安装 GraalVM"
        exit 1
    fi
    
    # 构建原生镜像
    mvn -Pnative native:compile
    
    echo "原生镜像构建完成！"
    echo "可执行文件位置: target/ic-framework-spring-aot"
else
    echo "跳过原生镜像构建"
    echo "如需构建原生镜像，请运行: ./build.sh native"
fi

echo "=== 构建完成 ===" 