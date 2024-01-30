
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

需要 host 

          'example.com': '127.0.0.1'
          'demo.example.com': '127.0.0.1'
          'iam.example.com': '127.0.0.1'


[demo-zookeeper-kafka](demo-zookeeper-kafka) 项目需要如下host:

          'bitnami-zookeeper.kube-public.svc.cluster.local': '127.0.0.1'
          'bitnami-kafka-dev.kube-public.svc.cluster.local': '127.0.0.1'





参考资源：
justAuth：https://github.com/justauth/JustAuth-demo

    计划扩展自定义用户源与客户端源

1. [x] demo-pm 是以前的旧项目
2. [x] eureka
3. [x] iam
4. [x] aliyun 登录
5. [ ] SkyWalking 集成
6. [ ] Sentinel 集成
7. [ ] seata
8. [ ] xxl job
9. [ ] flowable7
10. [ ] jasper report
