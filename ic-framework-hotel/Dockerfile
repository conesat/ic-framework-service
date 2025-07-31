# 使用 Amazon Corretto JDK 镜像
FROM amazoncorretto:21-alpine-jdk

# 设置工作目录
WORKDIR /app

# 拷贝 jar 包到容器
COPY target/*.jar app.jar

# 拷贝 resources 目录到容器的 /app/resources 路径下
COPY src/main/resources /app/resources

# 删除不需要的 dev 配置文件 idea直接部署 .dockerignore不生效，下面直接移除dev配置
#RUN rm -f /app/resources/application-dev.yml && \
#    rm -f /app/resources/mybatis-config-dev.xml \

# 暴露端口
EXPOSE 9999

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=file:/app/resources/"]