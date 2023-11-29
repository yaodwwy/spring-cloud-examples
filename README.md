
### 技术选型：

- JDK => `OpenJDK-21.0.1+`
- spring-boot.version => `3.2.0-M3+`
- spring-cloud.version => `2023.0.0-M2+`
- spring-authorization-server.version => `1.1.2`

以上边做边更新版本；


以下SQL表从 `spring-authorization-server`组件中获取

    oauth2-authorization-schema.sql
    oauth2-registered-client-schema.sql
    oauth2-authorization-consent-schema.sql
    // userdetails 的默认sql
    org/springframework/security/core/userdetails/jdbc/users.ddl

### 项目说明

内容与spring-authorization-server\samples\demo-authorizationserver 一致

justAuth 参考：https://github.com/justauth/JustAuth-demo

    计划扩展自定义用户源与客户端源

1. [x] demo-pm 是以前的旧项目
2. [x] 正在尝试通过 justAuth 集成到 iam
3. [x] 已完成集成pm justAuth iam
4. [x] 正在集成justAuth aliyun 登录
