#!/bin/bash

# MinIO 部署脚本
# 用于快速部署和配置 MinIO 对象存储服务

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置变量
MINIO_DATA_DIR="/Volumes/data/minio"
MINIO_CONFIG_DIR="/Volumes/data/minio/config"
MINIO_MC_DIR="/Volumes/data/minio/mc"
COMPOSE_FILE="docker-compose-minio.yml"

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 Docker 是否安装
check_docker() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker 未安装，请先安装 Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose 未安装，请先安装 Docker Compose"
        exit 1
    fi
    
    log_success "Docker 环境检查通过"
}

# 创建必要的目录
create_directories() {
    log_info "创建 MinIO 数据目录..."
    
    mkdir -p "$MINIO_DATA_DIR/data"
    mkdir -p "$MINIO_CONFIG_DIR"
    mkdir -p "$MINIO_MC_DIR"
    
    # 设置目录权限
    chmod 755 "$MINIO_DATA_DIR"
    chmod 755 "$MINIO_CONFIG_DIR"
    chmod 755 "$MINIO_MC_DIR"
    
    log_success "目录创建完成"
}

# 启动 MinIO 服务
start_minio() {
    log_info "启动 MinIO 服务..."
    
    if [ -f "$COMPOSE_FILE" ]; then
        docker-compose -f "$COMPOSE_FILE" up -d
        log_success "MinIO 服务启动成功"
    else
        log_error "找不到 $COMPOSE_FILE 文件"
        exit 1
    fi
}

# 等待服务启动
wait_for_service() {
    log_info "等待 MinIO 服务启动..."
    
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -f http://localhost:9000/minio/health/live &> /dev/null; then
            log_success "MinIO 服务已就绪"
            return 0
        fi
        
        log_info "等待中... ($attempt/$max_attempts)"
        sleep 2
        ((attempt++))
    done
    
    log_error "MinIO 服务启动超时"
    return 1
}

# 配置存储桶策略
configure_bucket_policy() {
    log_info "配置存储桶策略..."
    
    # 使用 MinIO 客户端工具配置策略
    docker run --rm --network minio-network \
        -e MC_HOST_local=http://minioadmin:minioadmin@minio:9000 \
        minio/mc:latest \
        admin policy set local readwrite user=minioadmin
    
    # 设置存储桶为公开访问
    docker run --rm --network minio-network \
        -e MC_HOST_local=http://minioadmin:minioadmin@minio:9000 \
        minio/mc:latest \
        anonymous set download local/ic-framework
    
    log_success "存储桶策略配置完成"
}

# 显示服务信息
show_service_info() {
    echo ""
    echo "=========================================="
    echo "           MinIO 服务部署完成"
    echo "=========================================="
    echo ""
    echo "服务地址:"
    echo "  - API 端点: http://localhost:9000"
    echo "  - Web 控制台: http://localhost:9001"
    echo ""
    echo "登录凭据:"
    echo "  - 用户名: minioadmin"
    echo "  - 密码: minioadmin"
    echo ""
    echo "数据目录: $MINIO_DATA_DIR"
    echo ""
    echo "常用命令:"
    echo "  - 查看服务状态: docker-compose -f $COMPOSE_FILE ps"
    echo "  - 查看服务日志: docker-compose -f $COMPOSE_FILE logs -f minio"
    echo "  - 停止服务: docker-compose -f $COMPOSE_FILE down"
    echo "  - 重启服务: docker-compose -f $COMPOSE_FILE restart"
    echo ""
    echo "=========================================="
}

# 主函数
main() {
    echo "开始部署 MinIO 对象存储服务..."
    echo ""
    
    check_docker
    create_directories
    start_minio
    
    if wait_for_service; then
        configure_bucket_policy
        show_service_info
        log_success "MinIO 部署完成！"
    else
        log_error "MinIO 部署失败"
        exit 1
    fi
}

# 脚本入口
case "${1:-deploy}" in
    "deploy")
        main
        ;;
    "start")
        start_minio
        ;;
    "stop")
        log_info "停止 MinIO 服务..."
        docker-compose -f "$COMPOSE_FILE" down
        log_success "MinIO 服务已停止"
        ;;
    "restart")
        log_info "重启 MinIO 服务..."
        docker-compose -f "$COMPOSE_FILE" restart
        log_success "MinIO 服务已重启"
        ;;
    "logs")
        docker-compose -f "$COMPOSE_FILE" logs -f minio
        ;;
    "status")
        docker-compose -f "$COMPOSE_FILE" ps
        ;;
    "clean")
        log_warning "这将删除所有 MinIO 数据，确定继续吗？(y/N)"
        read -r response
        if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
            log_info "清理 MinIO 数据..."
            docker-compose -f "$COMPOSE_FILE" down -v
            rm -rf "$MINIO_DATA_DIR"
            log_success "清理完成"
        else
            log_info "取消清理操作"
        fi
        ;;
    *)
        echo "用法: $0 {deploy|start|stop|restart|logs|status|clean}"
        echo ""
        echo "命令说明:"
        echo "  deploy  - 完整部署 MinIO 服务（默认）"
        echo "  start   - 启动 MinIO 服务"
        echo "  stop    - 停止 MinIO 服务"
        echo "  restart - 重启 MinIO 服务"
        echo "  logs    - 查看服务日志"
        echo "  status  - 查看服务状态"
        echo "  clean   - 清理所有数据"
        exit 1
        ;;
esac 