# Spring Data Redis - RedisRepository

## I. Dependency cần có

Nếu dùng Spring Boot:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```

## II. Cấu hình Redis (application.properties)

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=
spring.data.redis.database=0
```

## III. Annotation quan trọng trong Redis Entity

### 1. @RedisHash (QUAN TRỌNG NHẤT)

Dùng thay cho @Entity của JPA.

```java
@RedisHash("users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
}
```

**Ý nghĩa:**
- "users" = prefix key trong Redis
- Redis sẽ lưu dạng: `users:{id}`
- Ví dụ: `users:1`

### 2. @Id

Giống JPA.

```java
@Id
private String id;
```

Nếu không set id → phải tự generate.

### 3. @Indexed (rất quan trọng)

Cho phép query theo field đó.

```java
@Indexed
private String email;
```

Nếu không có @Indexed → không thể query theo field đó bằng repository.

### 4. @TimeToLive (TTL - tự xoá key)

```java
@TimeToLive
private Long expiration;
```

Hoặc:

```java
@TimeToLive
private Long expiration = 60L; // 60 seconds
```

Sau 60s key sẽ tự xoá.

### 5. @PersistenceConstructor

Dùng khi có constructor nhiều tham số.

```java
@PersistenceConstructor
public User(String id, String name) {
    this.id = id;
    this.name = name;
}
```

## IV. Redis Repository Interface

### 1. Basic Syntax

```java
@Repository
public interface UserRedisRepository 
        extends CrudRepository<User, String> {
}
```

### 2. Query Method (Quan trọng)

Giống JPA nhưng phải có @Indexed.

```java
Optional<User> findByEmail(String email);

List<User> findByName(String name);

boolean existsByEmail(String email);

void deleteByEmail(String email);
```

### 3. Paging (Không mạnh như JPA)

Redis Repository không hỗ trợ full paging như JPA.

## V. Các method có sẵn của CrudRepository

- `save(entity)`
- `findById(id)`
- `findAll()`
- `deleteById(id)`
- `existsById(id)`
- `count()`

## VI. Redis Key Structure Thực Tế

Nếu bạn có:

```java
@RedisHash("users")
public class User {
    @Id
    private String id;

    @Indexed
    private String email;
}
```

Redis sẽ tạo:

```
users:1                     (Hash chính)
users:email:test@gmail.com  (Index key)
```

## VII. Khác gì JPA?

| JPA | Redis |
|-----|-------|
| @Entity | @RedisHash |
| @Table | Không có |
| @Column | Không có |
| Quan hệ @OneToMany | Không hỗ trợ |
| SQL query | Không có |
| Transaction mạnh | Yếu hơn |

## VIII. Khi nào dùng Redis Repository?

**Dùng khi:**
- Cache user session
- OTP
- Token
- Temporary data
- Microservice cần speed cao

**Không dùng khi:**
- Quan hệ phức tạp
- Join nhiều bảng
- Hệ thống kế toán

## IX. Ví dụ Full Code

```java
@RedisHash(value = "users", timeToLive = 60)
public class User {

    @Id
    private String id;

    private String name;

    @Indexed
    private String email;
}
```

Repository:

```java
public interface UserRedisRepository 
        extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);
}
```

## X. So sánh nhanh với JPA để bạn dễ nhớ

Vì bạn đang học Spring Security + Clean Architecture + JPA, thì nhớ:

- **JPA** = Database chính
- **Redis** = cache layer

Thường flow thực tế:

```
Controller
    ↓
Service
    ↓
Check Redis trước
    ↓
Nếu không có → Query DB
    ↓
Save lại vào Redis
```

## XI. Nếu dùng RedisTemplate (Advanced hơn)

```java
@Autowired
private RedisTemplate<String, Object> redisTemplate;
```

**Set:**

```java
redisTemplate.opsForValue().set("key", value);
```

**Get:**

```java
redisTemplate.opsForValue().get("key");
```

**Hash:**

```java
redisTemplate.opsForHash().put("user:1", "name", "Quoc");
```

## XII. Quan trọng nhất bạn phải nhớ

- @RedisHash
- @Indexed
- @TimeToLive
- CrudRepository
- RedisTemplate (nếu custom)