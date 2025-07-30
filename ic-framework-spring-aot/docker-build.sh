#!/bin/bash

# IC Framework Spring AOT Docker 构建脚本

set -e

echo "=== IC Framework Spring AOT Docker 构建脚本 ==="

# 设置镜像名称和标签
IMAGE_NAME="ic-framework-spring-aot"
TAG="latest"

# 构建 Docker 镜像
echo "构建 Docker 镜像: $IMAGE_NAME:$TAG"
docker build -t $IMAGE_NAME:$TAG .

echo "Docker 镜像构建完成！"
echo "镜像名称: $IMAGE_NAME:$TAG"

# 显示镜像信息
echo "镜像信息:"
docker images $IMAGE_NAME:$TAG

echo "=== Docker 构建完成 ==="
echo ""
echo "运行容器命令:"
echo "docker run -p 8080:8080 $IMAGE_NAME:$TAG"
echo ""
echo "后台运行容器命令:"
echo "docker run -d -p 8080:8080 --name ic-aot-app $IMAGE_NAME:$TAG" 