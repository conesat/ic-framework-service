# MinIO 对象存储部署指南

## 📋 概述

本目录包含 MinIO 对象存储服务的完整部署配置，包括 Docker Compose 配置、部署脚本和环境配置。

## 📁 文件结构

```
doc/部署/
├── docker-compose-minio.yml    # MinIO Docker Compose 配置
├── minio-deploy.sh            # 部署脚本
├── env.template               # 环境配置模板
└── README.md                  # 本说明文档
```

## 🚀 快速开始

### 1. 环境准备

确保系统已安装：
- Docker (版本 20.10+)
- Docker Compose (版本 2.0+)
- curl (用于健康检查)

### 2. 配置环境

```bash
# 复制环境配置模板
cp env.template .env

# 根据需要修改 .env 文件中的配置
vim .env
```

### 3. 部署服务

```bash
# 给部署脚本添加执行权限
chmod +x minio-deploy.sh

# 执行部署
./minio-deploy.sh
```

## 🔧 配置说明

### Docker Compose 配置特性

#### 服务配置
- **镜像**: `minio/minio:latest` - 最新稳定版本
- **重启策略**: `unless-stopped` - 除非手动停止，否则自动重启
- **端口映射**:
  - `9000:9000` - API 端口
  - `9001:9001` - Web 控制台端口

#### 环境变量
- `MINIO_ROOT_USER` - 根用户名
- `MINIO_ROOT_PASSWORD` - 根密码
- `TZ` - 时区设置
- `MINIO_LOG_LEVEL` - 日志级别
- `MINIO_BROWSER_REDIRECT_URL` - 浏览器重定向 URL

#### 数据持久化
- **数据目录**: `/Volumes/data/minio/data` - 文件存储
- **配置目录**: `/Volumes/data/minio/config` - 配置文件
- **客户端目录**: `/Volumes/data/minio/mc` - MinIO 客户端配置

#### 健康检查
- **检查命令**: `curl -f http://localhost:9000/minio/health/live`
- **检查间隔**: 30秒
- **超时时间**: 20秒
- **重试次数**: 3次
- **启动等待**: 40秒

#### 资源限制
- **内存限制**: 1GB
- **CPU 限制**: 0.5 核
- **内存预留**: 512MB
- **CPU 预留**: 0.25 核

#### 网络配置
- **网络类型**: bridge
- **网络名称**: minio-network
- **隔离性**: 独立的网络环境

### 可选服务

#### MinIO 客户端工具 (mc)
- **镜像**: `minio/mc:latest`
- **用途**: 命令行管理工具
- **启动方式**: `docker-compose --profile tools up mc`
- **配置**: 自动连接到 MinIO 服务

## 📖 使用说明

### 部署脚本命令

```bash
# 完整部署（默认）
./minio-deploy.sh deploy

# 启动服务
./minio-deploy.sh start

# 停止服务
./minio-deploy.sh stop

# 重启服务
./minio-deploy.sh restart

# 查看日志
./minio-deploy.sh logs

# 查看状态
./minio-deploy.sh status

# 清理数据
./minio-deploy.sh clean
```

### Docker Compose 命令

```bash
# 启动服务
docker-compose -f docker-compose-minio.yml up -d

# 查看服务状态
docker-compose -f docker-compose-minio.yml ps

# 查看服务日志
docker-compose -f docker-compose-minio.yml logs -f minio

# 停止服务
docker-compose -f docker-compose-minio.yml down

# 重启服务
docker-compose -f docker-compose-minio.yml restart

# 启动客户端工具
docker-compose -f docker-compose-minio.yml --profile tools up mc
```

## 🌐 访问地址

### 服务端点
- **API 端点**: http://localhost:9000
- **Web 控制台**: http://localhost:9001

### 登录凭据
- **用户名**: minioadmin
- **密码**: minioadmin

## 🔒 安全配置

### 默认安全设置
1. **存储桶策略**: 自动配置为公开读取
2. **用户权限**: 根用户具有完全权限
3. **网络隔离**: 使用独立网络

### 生产环境建议
1. **修改默认密码**: 更改 `MINIO_ROOT_PASSWORD`
2. **启用 HTTPS**: 配置 SSL 证书
3. **限制网络访问**: 配置防火墙规则
4. **定期备份**: 备份数据目录
5. **监控告警**: 配置健康检查告警

## 📊 监控和维护

### 健康检查
```bash
# 检查服务健康状态
curl -f http://localhost:9000/minio/health/live

# 查看容器状态
docker ps | grep minio
```

### 日志查看
```bash
# 实时查看日志
docker-compose -f docker-compose-minio.yml logs -f minio

# 查看最近日志
docker-compose -f docker-compose-minio.yml logs --tail=100 minio
```

### 数据备份
```bash
# 备份数据目录
tar -czf minio-backup-$(date +%Y%m%d).tar.gz /Volumes/data/minio/data

# 恢复数据
tar -xzf minio-backup-20241219.tar.gz -C /
```

## 🛠️ 故障排除

### 常见问题

#### 1. 服务启动失败
```bash
# 检查端口占用
lsof -i :9000
lsof -i :9001

# 检查目录权限
ls -la /Volumes/data/minio/

# 查看详细日志
docker-compose -f docker-compose-minio.yml logs minio
```

#### 2. 无法访问 Web 控制台
```bash
# 检查服务状态
docker-compose -f docker-compose-minio.yml ps

# 检查网络连接
curl -v http://localhost:9001

# 重启服务
docker-compose -f docker-compose-minio.yml restart
```

#### 3. 文件上传失败
```bash
# 检查存储桶策略
docker run --rm --network minio-network \
  -e MC_HOST_local=http://minioadmin:minioadmin@minio:9000 \
  minio/mc:latest policy list local/ic-framework

# 重新配置策略
./minio-deploy.sh deploy
```

### 性能优化

#### 1. 调整资源限制
修改 `docker-compose-minio.yml` 中的资源限制：
```yaml
deploy:
  resources:
    limits:
      memory: 2G    # 增加内存限制
      cpus: '1.0'   # 增加 CPU 限制
```

#### 2. 优化存储
- 使用 SSD 存储
- 配置 RAID 阵列
- 启用压缩

#### 3. 网络优化
- 使用专用网络
- 配置负载均衡
- 启用缓存

## 📞 支持

如果遇到问题，请检查：
1. Docker 和 Docker Compose 版本
2. 系统资源使用情况
3. 网络连接状态
4. 日志文件内容

更多信息请参考：
- [MinIO 官方文档](https://docs.min.io/)
- [Docker Compose 文档](https://docs.docker.com/compose/)
- [项目文档](../README.md) 