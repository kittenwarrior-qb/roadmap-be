## I. Document Mapping

### @Document
Đánh dấu class là MongoDB document (collection).

```java
@Document(collection = "users")  // Tên collection
public class User {
    @Id
    private String id;  // MongoDB dùng String cho ObjectId
    
    private String username;
}
```

**Cấu hình collection:**
```java
@Document(
    collection = "users",
    collation = @Collation(locale = "vi")  // Collation cho sort/compare
)
```

### @Id
Primary key trong MongoDB (mặc định là ObjectId).

```java
@Id
private String id;  // MongoDB tự sinh ObjectId

// Hoặc custom ID
@Id
private Long customId;
```

### @Field
Đặt tên field khác với property name.

```java
@Field("user_name")  // Lưu trong MongoDB là "user_name"
private String username;

@Field(name = "email", order = 1)  // Thứ tự field
private String email;
```

### @Indexed
Tạo index cho field (tăng tốc query).

```java
@Indexed(unique = true)  // Unique index
private String email;

@Indexed(name = "username_idx", background = true)
private String username;

@Indexed(expireAfterSeconds = 3600)  // TTL index (tự xóa sau 1h)
private LocalDateTime createdAt;
```

**Compound index:**
```java
@Document
@CompoundIndex(
    name = "name_age_idx",
    def = "{'name': 1, 'age': -1}"  // 1: ascending, -1: descending
)
public class User { }
```

### @Transient
Field không lưu vào MongoDB.

```java
@Transient
private String tempData;
```

### @TextIndexed
Full-text search index.

```java
@TextIndexed
private String description;

// Search
List<User> findByDescriptionContaining(String keyword);
```

---

## II. Repository Queries

### @Repository
Đánh dấu MongoDB repository.

```java
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Query methods
}
```

### @Query
Viết MongoDB query bằng JSON.

```java
// Basic query
@Query("{ 'email': ?0 }")
Optional<User> findByEmail(String email);

// Multiple parameters
@Query("{ 'name': ?0, 'age': { $gte: ?1 } }")
List<User> findByNameAndMinAge(String name, int minAge);

// Projection (chỉ lấy một số fields)
@Query(value = "{ 'email': ?0 }", fields = "{ 'name': 1, 'email': 1 }")
User findByEmailWithProjection(String email);

// Regex
@Query("{ 'name': { $regex: ?0, $options: 'i' } }")  // i: case-insensitive
List<User> searchByName(String pattern);

// $in operator
@Query("{ 'role': { $in: ?0 } }")
List<User> findByRoles(List<String> roles);
```

### @Aggregation
Dùng MongoDB Aggregation Pipeline (powerful queries).

```java
// Group by và count
@Aggregation(pipeline = {
    "{ '$match': { 'age': { '$gte': ?0 } } }",
    "{ '$group': { '_id': '$role', 'count': { '$sum': 1 } } }"
})
List<RoleCount> countUsersByRole(int minAge);

// Lookup (join)
@Aggregation(pipeline = {
    "{ '$lookup': { 'from': 'orders', 'localField': '_id', 'foreignField': 'userId', 'as': 'orders' } }",
    "{ '$match': { 'orders': { '$ne': [] } } }"
})
List<User> findUsersWithOrders();

// Sort và limit
@Aggregation(pipeline = {
    "{ '$match': { 'active': true } }",
    "{ '$sort': { 'createdAt': -1 } }",
    "{ '$limit': 10 }"
})
List<User> findTop10ActiveUsers();
```

### @Meta
Thêm metadata cho query (timeout, hint, comment...).

```java
@Meta(
    maxExecutionTimeMs = 5000,  // Timeout 5s
    comment = "Find active users"
)
@Query("{ 'active': true }")
List<User> findActiveUsers();

@Meta(cursorBatchSize = 100)  // Batch size
List<User> findAll();
```

---

## III. Auditing

### Enable Auditing
```java
@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // Tự động track created/modified time & user
}
```

### Auditing Annotations

```java
@Document
public class User {
    @Id
    private String id;
    
    @CreatedDate
    private LocalDateTime createdAt;  // Tự động set khi insert
    
    @LastModifiedDate
    private LocalDateTime updatedAt;  // Tự động update khi modify
    
    @CreatedBy
    private String createdBy;  // User tạo
    
    @LastModifiedBy
    private String modifiedBy;  // User sửa cuối
}
```

**Cấu hình CreatedBy/ModifiedBy:**
```java
@Bean
public AuditorAware<String> auditorProvider() {
    return () -> Optional.of(SecurityContextHolder.getContext()
        .getAuthentication().getName());
}
```

---

## IV. References (Relationships)

### @DBRef (Legacy)
Tham chiếu document khác (tạo reference, không embed).

```java
@Document
public class Order {
    @Id
    private String id;
    
    @DBRef
    private User user;  // Lưu reference đến User document
}
```

**Lưu ý:** 
- MongoDB không có JOIN → mỗi @DBRef = 1 query riêng (N+1 problem)
- Chậm hơn embedded documents
- Chỉ dùng khi document được reference nhiều nơi

### @DocumentReference (Khuyên dùng)
Thay thế @DBRef, linh hoạt hơn.

```java
@Document
public class Order {
    @DocumentReference
    private User user;  // Lazy loading mặc định
    
    @DocumentReference(lazy = false)  // Eager loading
    private Product product;
}
```

**So sánh với @DBRef:**
- Hỗ trợ lazy loading tốt hơn
- Có thể custom lookup logic
- Performance tốt hơn

### Embedded Documents (Khuyên dùng nhất)
Nhúng document con vào document cha (không cần annotation đặc biệt).

```java
@Document
public class Order {
    @Id
    private String id;
    
    private Address shippingAddress;  // Embedded
    private List<OrderItem> items;    // Embedded list
}

// Không cần @Document cho embedded class
public class Address {
    private String street;
    private String city;
}
```

**Best practice:** Ưu tiên embedded nếu data không cần tái sử dụng.

---

## V. Validation

Dùng Bean Validation như JPA.

```java
@Document
public class User {
    @NotNull
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
    
    @Email
    @Indexed(unique = true)
    private String email;
    
    @Min(18)
    @Max(100)
    private Integer age;
}
```

---

## 🎯 Top Annotations Dùng Nhiều Nhất

```java
@Document              // 1. Định nghĩa collection
@Id                    // 2. Primary key
@Field                 // 3. Custom field name
@Indexed               // 4. Tạo index
@Query                 // 5. Custom query
@Aggregation           // 6. Aggregation pipeline
@CreatedDate           // 7. Auto timestamp
@LastModifiedDate      // 8. Auto update time
@Transient             // 9. Không lưu DB
@DocumentReference     // 10. Reference document
```

**Ví dụ document hoàn chỉnh:**
```java
@Document(collection = "users")
@CompoundIndex(def = "{'email': 1, 'active': 1}")
public class User {
    @Id
    private String id;
    
    @Indexed(unique = true)
    @Field("user_email")
    private String email;
    
    @TextIndexed
    private String bio;
    
    @DocumentReference(lazy = true)
    private List<Order> orders;
    
    private Address address;  // Embedded
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    @Transient
    private String tempToken;
}
```