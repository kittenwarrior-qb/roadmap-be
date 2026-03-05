# Spring Boot — Tổng quan

> **Spring Boot** giúp tạo ứng dụng Java **production-ready** một cách nhanh chóng,  
> không cần cấu hình phức tạp, không cần XML, không cần server riêng.

---

## 1. Spring Boot là gì?

**Spring** là framework Java phổ biến nhất để xây dựng ứng dụng enterprise.  
**Spring Boot** = Spring + **Auto-configuration** — giúp bạn bắt đầu trong vài phút thay vì vài giờ.

```
Trước Spring Boot:        Với Spring Boot:
- Cấu hình XML dài        - Gần như zero config
- Deploy lên Tomcat        - Chạy trực tiếp: java -jar app.jar
- Setup thủ công           - Auto-configure mọi thứ
```

**Mục tiêu chính:**
- 🚀 **Khởi động nhanh** — tạo app chạy được trong vài phút
- 🎯 **Opinionated defaults** — cấu hình sẵn, override khi cần
- 🔋 **Production-ready** — built-in: metrics, health check, security, logging
- ❌ **Không code generation, không XML**

---

## 2. Các lĩnh vực Spring Boot hỗ trợ

| Lĩnh vực | Công nghệ hỗ trợ |
|---------|-----------------|
| **Web** | Spring MVC (Servlet), Spring WebFlux (Reactive) |
| **Security** | Spring Security, OAuth2, SAML |
| **Database SQL** | JPA/Hibernate, JDBC, Connection Pool |
| **Database NoSQL** | Redis, MongoDB, Neo4j |
| **Messaging** | RabbitMQ (AMQP), Kafka, ActiveMQ (JMS) |
| **Caching** | EhCache, Hazelcast, Infinispan |
| **Scheduling** | Quartz, `@Scheduled` |
| **Container** | Docker, Cloud Native Buildpacks |
| **Monitoring** | Actuator — metrics, health, audit |

---

## 3. Build system & Khởi tạo project

Spring Boot hỗ trợ **3 build system**:

| | Maven | Gradle | Ant |
|-|-------|--------|-----|
| File config | `pom.xml` | `build.gradle` | `build.xml` |
| Phổ biến | ✅ Rất phổ biến | ✅ Linh hoạt hơn | Ít dùng |

**Khởi tạo nhanh nhất:** [start.spring.io](https://start.spring.io) — chọn dependencies, download, chạy ngay.

---

## 4. Cấu trúc ứng dụng Spring Boot

```java
@SpringBootApplication          // = @Configuration + @EnableAutoConfiguration + @ComponentScan
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);   // Khởi động embedded server
    }
}
```

**3 annotation cốt lõi:**

| Annotation | Mục đích |
|-----------|---------|
| `@Configuration` | Đánh dấu class chứa Bean definitions |
| `@EnableAutoConfiguration` | Tự động cấu hình Spring dựa trên dependencies có trong classpath |
| `@ComponentScan` | Quét và đăng ký các Bean trong package |

---

## 5. Các tính năng nổi bật

### External Configuration
Cấu hình qua `application.properties` / `application.yml` — không cần sửa code:

```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost/mydb
spring.profiles.active=dev
```

### Profiles
Cấu hình khác nhau cho từng môi trường:

```
application-dev.properties   → môi trường development
application-prod.properties  → môi trường production
```

### Actuator — Monitoring production

```
GET /actuator/health    → Trạng thái ứng dụng
GET /actuator/metrics   → CPU, memory, request count...
GET /actuator/loggers   → Cấu hình logging
```

---

## 6. Vòng đời phát triển

```
1. Khởi tạo     → start.spring.io / Spring Initializr
2. Phát triển   → IDE (IntelliJ, Eclipse, VS Code)
3. Chạy local   → mvn spring-boot:run  hoặc  java -jar app.jar
4. Deploy       → Production JAR (embedded Tomcat)  hoặc  Docker container
5. Monitor      → Spring Actuator + Metrics
```

---

## 7. Tóm tắt — Spring vs Spring Boot

| | Spring Framework | Spring Boot |
|-|-----------------|-------------|
| Cấu hình | Nhiều XML / Java config | Auto-config, gần như zero config |
| Server | Deploy war lên Tomcat ngoài | Embedded server (Tomcat/Netty) |
| Khởi động | Phức tạp | `java -jar app.jar` |
| Thích hợp | Kiểm soát chi tiết | ✅ Hầu hết dự án thực tế |