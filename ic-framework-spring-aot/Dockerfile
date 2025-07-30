# 使用 Ubuntu 作为基础镜像
FROM ubuntu:22.04

# 设置工作目录
WORKDIR /app

# 安装必要的运行时库
RUN apt-get update && apt-get install -y \
    libc6 \
    && rm -rf /var/lib/apt/lists/*

# 复制原生可执行文件
COPY target/ic-framework-spring-aot .

# 给可执行文件添加执行权限
RUN chmod +x ic-framework-spring-aot

# 暴露端口
EXPOSE 8080

# 设置启动命令
ENTRYPOINT ["./ic-framework-spring-aot"] 