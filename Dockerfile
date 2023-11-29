FROM openjdk:17.0.2
MAINTAINER Adam<adam@adbyte.cn>

ARG JAR_FILE
ARG APP_VERSION

ENV TZ=Asia/Shanghai \
    APP_VERSION=${APP_VERSION}

COPY ${JAR_FILE} /app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]

EXPOSE 80
