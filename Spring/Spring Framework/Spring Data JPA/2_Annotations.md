## I. Entity Mapping

### @Entity
Đánh dấu class là một entity (bảng trong DB).

```java
@Entity
public class User {
    // Bắt buộc có constructor không tham số
}
```

### @Table
Cấu hình chi tiết cho bảng.

```java
@Table(
    name = "users",                          // Tên bảng
    uniqueConstraints = @UniqueConstraint(   // Unique constraint
        columnNames = {"email"}
    ),
    indexes = @Index(                        // Index
        name = "idx_email", 
        columnList = "email"
    )
)
```

### @Id + @GeneratedValue
Định nghĩa primary key và cách sinh giá trị.

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

**Các strategy:**
- `IDENTITY`: Auto-increment (MySQL, SQL Server)
- `SEQUENCE`: Dùng sequence (PostgreSQL, Oracle)
- `AUTO`: JPA tự chọn (mặc định)
- `TABLE`: Dùng bảng riêng để sinh ID (hiếm dùng)

### @Column
Cấu hình chi tiết cột.

```java
@Column(
    name = "user_name",        // Tên cột trong DB
    nullable = false,          // NOT NULL
    unique = true,             // UNIQUE
    length = 100,              // VARCHAR(100)
    columnDefinition = "TEXT"  // Custom SQL type
)
private String username;
```

### @Transient
Field không lưu vào DB (chỉ dùng trong code).

```java
@Transient
private String fullName;  // Không map vào DB
```

### @Enumerated
Lưu enum vào DB.

```java
@Enumerated(EnumType.STRING)  // Lưu tên enum (khuyên dùng)
private Role role;

@Enumerated(EnumType.ORDINAL) // Lưu số thứ tự (0,1,2...) - không nên dùng
private Status status;
```

### @Lob
Lưu dữ liệu lớn (text dài hoặc binary).

```java
@Lob
private String description;  // TEXT/CLOB

@Lob
private byte[] image;        // BLOB
```
## II. Relationships

### @ManyToOne (Quan hệ N-1)
Nhiều entity thuộc về 1 entity khác. Đây là phía "nhiều" - nơi có foreign key.

```java
@Entity
public class Order {
    @ManyToOne(fetch = FetchType.LAZY)  // LAZY: load khi cần
    @JoinColumn(name = "user_id")       // Tên cột FK
    private User user;
}
```

**Cascade options:**
```java
@ManyToOne(cascade = CascadeType.PERSIST)  // Lưu cả parent khi lưu child
@ManyToOne(cascade = CascadeType.ALL)      // Cascade tất cả operations
```

### @OneToMany (Quan hệ 1-N)
1 entity có nhiều entity con. Đây là phía "một" - không có FK.

```java
@Entity
public class User {
    @OneToMany(
        mappedBy = "user",              // Tên field ở phía ManyToOne
        cascade = CascadeType.ALL,      // Cascade operations
        orphanRemoval = true,           // Xóa child khi remove khỏi collection
        fetch = FetchType.LAZY          // LAZY loading (mặc định)
    )
    private List<Order> orders = new ArrayList<>();
}
```

**Lưu ý:** `mappedBy` chỉ định phía nào là owner của relationship (phía có FK).

### @OneToOne (Quan hệ 1-1)
Mỗi entity chỉ liên kết với 1 entity khác.

```java
// Phía owner (có FK)
@Entity
public class User {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", unique = true)
    private Profile profile;
}

// Phía inverse
@Entity
public class Profile {
    @OneToOne(mappedBy = "profile")
    private User user;
}
```

### @ManyToMany (Quan hệ N-N)
Nhiều entity liên kết với nhiều entity khác (cần bảng trung gian).

```java
@Entity
public class User {
    @ManyToMany
    @JoinTable(
        name = "user_role",                           // Tên bảng trung gian
        joinColumns = @JoinColumn(name = "user_id"),  // FK đến User
        inverseJoinColumns = @JoinColumn(name = "role_id") // FK đến Role
    )
    private Set<Role> roles = new HashSet<>();
}

// Phía inverse
@Entity
public class Role {
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
```

### @JoinColumn
Cấu hình foreign key column.

```java
@JoinColumn(
    name = "user_id",              // Tên cột FK
    nullable = false,              // NOT NULL
    foreignKey = @ForeignKey(      // Tên constraint
        name = "fk_order_user"
    )
)
```

## III. Repository Annotations

### @Repository
Đánh dấu class là Spring bean repository (tùy chọn với Spring Data JPA).

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
```

### @Query
Viết custom query bằng JPQL hoặc native SQL.

```java
// JPQL (khuyên dùng)
@Query("SELECT u FROM User u WHERE u.email = :email")
User findByEmail(@Param("email") String email);

// Native SQL
@Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
User findByEmailNative(String email);

// Join fetch (giải quyết N+1)
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findUserWithOrders(@Param("id") Long id);
```

### @Modifying
Bắt buộc cho UPDATE/DELETE queries.

```java
@Modifying
@Transactional  // Phải có @Transactional
@Query("UPDATE User u SET u.active = false WHERE u.lastLogin < :date")
int deactivateInactiveUsers(@Param("date") LocalDateTime date);

@Modifying
@Query("DELETE FROM User u WHERE u.active = false")
void deleteInactiveUsers();
```

**Lưu ý:** 
- Phải kết hợp với `@Transactional`
- Return type: `void` hoặc `int` (số dòng affected)
- Thêm `clearAutomatically = true` để clear persistence context sau khi execute

### @Param
Bind tên parameter trong query.

```java
@Query("SELECT u FROM User u WHERE u.name = :name AND u.age > :age")
List<User> findByNameAndAge(@Param("name") String name, @Param("age") int age);
```
## IV. Transaction Management

### @Transactional
Quản lý transaction tự động (commit/rollback).

```java
@Service
public class UserService {
    
    @Transactional  // Mặc định: rollback khi có RuntimeException
    public void createUser(User user) {
        userRepository.save(user);
        // Nếu có exception, tự động rollback
    }
    
    @Transactional(readOnly = true)  // Tối ưu cho read operations
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    @Transactional(
        propagation = Propagation.REQUIRES_NEW,  // Tạo transaction mới
        isolation = Isolation.READ_COMMITTED,    // Isolation level
        timeout = 30,                            // Timeout (seconds)
        rollbackFor = Exception.class            // Rollback cho checked exception
    )
    public void complexOperation() {
        // ...
    }
}
```

**Propagation types:**
- `REQUIRED` (mặc định): Dùng transaction hiện tại hoặc tạo mới
- `REQUIRES_NEW`: Luôn tạo transaction mới
- `NESTED`: Tạo nested transaction
- `SUPPORTS`: Dùng transaction nếu có, không thì không cần

**Best practices:**
- Đặt ở service layer, không đặt ở repository
- Dùng `readOnly = true` cho read operations (tối ưu performance)
- Giữ transaction scope nhỏ nhất có thể

## V. Fetch Strategy & Performance

### FetchType
Quyết định khi nào load dữ liệu từ relationships.

```java
// LAZY: Load khi truy cập (khuyên dùng)
@ManyToOne(fetch = FetchType.LAZY)
private User user;

// EAGER: Load ngay lập tức (cẩn thận N+1 problem)
@ManyToOne(fetch = FetchType.EAGER)
private Category category;
```

**Mặc định:**
- `@ManyToOne`, `@OneToOne`: EAGER
- `@OneToMany`, `@ManyToMany`: LAZY

**Best practice:** Luôn dùng LAZY, chỉ dùng EAGER khi thực sự cần.

### @EntityGraph
Giải quyết N+1 problem bằng cách fetch trước relationships.

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Fetch user cùng với roles trong 1 query
    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findById(Long id);
    
    // Fetch nhiều relationships
    @EntityGraph(attributePaths = {"roles", "orders", "orders.items"})
    List<User> findAll();
}
```

**Hoặc định nghĩa trong Entity:**
```java
@Entity
@NamedEntityGraph(
    name = "User.detail",
    attributeNodes = {
        @NamedAttributeNode("roles"),
        @NamedAttributeNode("orders")
    }
)
public class User { }

// Sử dụng
@EntityGraph("User.detail")
Optional<User> findById(Long id);
```
## VI. Composite Key

### Cách 1: @EmbeddedId + @Embeddable (khuyên dùng)

```java
// Class chứa composite key
@Embeddable
public class OrderItemId implements Serializable {
    private Long orderId;
    private Long productId;
    
    // Constructor, equals, hashCode
}

// Entity sử dụng
@Entity
public class OrderItem {
    @EmbeddedId
    private OrderItemId id;
    
    private Integer quantity;
}
```

### Cách 2: @IdClass

```java
// Class chứa composite key
public class OrderItemId implements Serializable {
    private Long orderId;
    private Long productId;
    
    // Constructor, equals, hashCode
}

// Entity sử dụng
@Entity
@IdClass(OrderItemId.class)
public class OrderItem {
    @Id
    private Long orderId;
    
    @Id
    private Long productId;
    
    private Integer quantity;
}
```
## VII. Hibernate-Specific (không phải JPA chuẩn)

### @CreationTimestamp + @UpdateTimestamp
Tự động set timestamp khi tạo/update.

```java
@Entity
public class User {
    @CreationTimestamp
    private LocalDateTime createdAt;  // Tự động set khi insert
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;  // Tự động update khi modify
}
```

### @DynamicInsert + @DynamicUpdate
Chỉ generate SQL cho các field không null.

```java
@Entity
@DynamicInsert  // INSERT chỉ các field có giá trị
@DynamicUpdate  // UPDATE chỉ các field thay đổi
public class User {
    // Tối ưu performance khi có nhiều columns
}
```
## VIII. Locking

### @Version (Optimistic Locking)
Tránh conflict khi nhiều user cùng update 1 record.

```java
@Entity
public class Product {
    @Id
    private Long id;
    
    @Version
    private Long version;  // Hibernate tự động tăng mỗi lần update
    
    private String name;
    private BigDecimal price;
}
```

**Cách hoạt động:**
1. User A đọc Product (version = 1)
2. User B đọc Product (version = 1)
3. User A update → version = 2 ✅
4. User B update → Exception (version mismatch) ❌

**Exception:** `OptimisticLockException` - cần handle và retry.
## IX. Validation (Bean Validation - không phải JPA)

Thường dùng kèm với JPA entities.

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Username không được null")
    @NotBlank(message = "Username không được rỗng")
    @Size(min = 3, max = 50, message = "Username từ 3-50 ký tự")
    private String username;
    
    @Email(message = "Email không hợp lệ")
    @Column(unique = true)
    private String email;
    
    @Min(value = 18, message = "Tuổi tối thiểu 18")
    @Max(value = 100, message = "Tuổi tối đa 100")
    private Integer age;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "SĐT phải 10 chữ số")
    private String phone;
}
```

**Trigger validation:**
```java
@PostMapping("/users")
public User create(@Valid @RequestBody User user) {  // @Valid trigger validation
    return userService.save(user);
}
```

---

## 🎯 Top 10 Annotations Dùng Nhiều Nhất

```java
@Entity                    // 1. Định nghĩa entity
@Table                     // 2. Cấu hình bảng
@Id                        // 3. Primary key
@GeneratedValue            // 4. Auto-generate ID
@Column                    // 5. Cấu hình cột
@ManyToOne                 // 6. Quan hệ N-1 (có FK)
@OneToMany                 // 7. Quan hệ 1-N
@JoinColumn                // 8. Cấu hình FK
@Transactional             // 9. Quản lý transaction
@Query                     // 10. Custom query
```

**Ví dụ entity hoàn chỉnh:**
```java
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String orderNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Version
    private Long version;
}
```