#!/bin/bash

# IC Framework Spring AOT 测试脚本

set -e

echo "=== IC Framework Spring AOT 测试脚本 ==="

# 等待应用程序启动
wait_for_app() {
    echo "等待应用程序启动..."
    for i in {1..30}; do
        if curl -s http://localhost:8080/api/health > /dev/null; then
            echo "应用程序已启动！"
            return 0
        fi
        echo "等待中... ($i/30)"
        sleep 2
    done
    echo "应用程序启动超时！"
    return 1
}

# 测试 API 端点
test_endpoints() {
    echo "测试 API 端点..."
    
    # 测试健康检查
    echo "1. 测试健康检查端点..."
    curl -s http://localhost:8080/api/health | jq .
    
    # 测试问候端点
    echo "2. 测试问候端点..."
    curl -s http://localhost:8080/api/hello | jq .
    
    # 测试带参数的问候端点
    echo "3. 测试带参数的问候端点..."
    curl -s "http://localhost:8080/api/hello?name=IC%20Framework" | jq .
    
    # 测试系统信息端点
    echo "4. 测试系统信息端点..."
    curl -s http://localhost:8080/api/info | jq .
    
    # 测试 Actuator 端点
    echo "5. 测试 Actuator 端点..."
    curl -s http://localhost:8080/actuator/health | jq .
    
    echo "所有端点测试完成！"
}

# 性能测试
performance_test() {
    echo "性能测试..."
    
    # 使用 ab 进行简单的性能测试
    if command -v ab &> /dev/null; then
        echo "使用 Apache Bench 进行性能测试..."
        ab -n 1000 -c 10 http://localhost:8080/api/health
    else
        echo "Apache Bench 未安装，跳过性能测试"
    fi
}

# 主测试流程
main() {
    # 检查应用程序是否运行
    if ! curl -s http://localhost:8080/api/health > /dev/null; then
        echo "错误: 应用程序未运行，请先启动应用程序"
        echo "启动命令: java -jar target/ic-framework-spring-aot-1.0.0.jar"
        echo "或者: ./target/ic-framework-spring-aot (原生镜像)"
        exit 1
    fi
    
    # 等待应用程序完全启动
    wait_for_app
    
    # 测试端点
    test_endpoints
    
    # 性能测试
    performance_test
    
    echo "=== 测试完成 ==="
}

# 运行主测试流程
main 