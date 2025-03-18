# 使用官方的 OpenJDK 21 基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 将打包好的 JAR 文件复制到容器中
COPY target/transactiondemo-0.0.1-SNAPSHOT.jar app.jar

# 暴露应用程序端口
EXPOSE 8080

# 运行应用程序
CMD ["java", "-jar", "app.jar"]    