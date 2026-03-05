# JpaRepository - Spring Data JPA

## 1. Các loại Repository chính

Spring Data JPA cung cấp các interface theo thứ bậc:

```
Repository (base interface)
    ↓
CrudRepository (CRUD operations)
    ↓
PagingAndSortingRepository (+ Pagination & Sorting)
    ↓
JpaRepository (+ JPA specific methods)
```

**Trong thực tế:**

```java
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository là đầy đủ nhất, bao gồm tất cả methods từ các interface cha
}
```

---

## 2. CRUD Methods (từ CrudRepository)

### CREATE / UPDATE

```java
<S extends T> S save(S entity);
<S extends T> List<S> saveAll(Iterable<S> entities);
```

**Cách hoạt động của save():**
- Nếu `id == null` hoặc `id` chưa tồn tại → **INSERT**
- Nếu `id` đã tồn tại → **UPDATE** (merge)

**Ví dụ:**

```java
// INSERT
User newUser = new User();
newUser.setName("John");
userRepository.save(newUser); // id = null → INSERT

// UPDATE
User existingUser = userRepository.findById(1L).get();
existingUser.setName("Jane");
userRepository.save(existingUser); // id = 1 → UPDATE
```

### READ

```java
Optional<T> findById(ID id);
boolean existsById(ID id);
List<T> findAll();
List<T> findAllById(Iterable<ID> ids);
long count();
```

**Ví dụ:**

```java
// Tìm theo ID
Optional<User> user = userRepository.findById(1L);

// Kiểm tra tồn tại
boolean exists = userRepository.existsById(1L);

// Lấy tất cả
List<User> users = userRepository.findAll();

// Đếm số lượng
long total = userRepository.count();
```

### DELETE

```java
void deleteById(ID id);
void delete(T entity);
void deleteAll();
void deleteAllById(Iterable<? extends ID> ids);
```

**Ví dụ:**

```java
// Xóa theo ID
userRepository.deleteById(1L);

// Xóa theo entity
User user = userRepository.findById(1L).get();
userRepository.delete(user);

// Xóa nhiều
userRepository.deleteAllById(Arrays.asList(1L, 2L, 3L));
```

---

## 3. Paging & Sorting (từ PagingAndSortingRepository)

### Pagination

```java
Page<T> findAll(Pageable pageable);
```

**Ví dụ:**

```java
// Trang 0, size 10, sắp xếp theo name
Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
Page<User> page = userRepository.findAll(pageable);

// Thông tin pagination
int totalPages = page.getTotalPages();
long totalElements = page.getTotalElements();
List<User> users = page.getContent();
boolean hasNext = page.hasNext();
```

### Sorting

```java
List<T> findAll(Sort sort);
```

**Ví dụ:**

```java
// Sắp xếp đơn giản
List<User> users = userRepository.findAll(Sort.by("name"));

// Sắp xếp giảm dần
List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

// Sắp xếp nhiều trường
List<User> users = userRepository.findAll(
    Sort.by("status").ascending()
        .and(Sort.by("createdAt").descending())
);
```

---

## 4. Các method bổ sung trong JpaRepository

### Flush Operations

```java
void flush();
<S extends T> S saveAndFlush(S entity);
<S extends T> List<S> saveAllAndFlush(Iterable<S> entities);
```

**flush()** = đẩy các thay đổi từ Persistence Context xuống database ngay lập tức (không đợi commit).

**Ví dụ:**

```java
User user = new User();
user.setName("John");
userRepository.save(user); // Chưa chắc đã INSERT vào DB

userRepository.flush(); // Bắt buộc thực thi SQL ngay
```

### Batch Delete

```java
void deleteAllInBatch();
void deleteAllByIdInBatch(Iterable<ID> ids);
void deleteAllInBatch(Iterable<T> entities);
```

**Khác biệt với delete() thường:**
- `delete()`: SELECT trước, sau đó DELETE từng entity (chậm)
- `deleteAllInBatch()`: DELETE trực tiếp bằng 1 câu SQL (nhanh hơn)

**Ví dụ:**

```java
// Xóa nhiều ID bằng 1 câu SQL
userRepository.deleteAllByIdInBatch(Arrays.asList(1L, 2L, 3L));
// SQL: DELETE FROM users WHERE id IN (1, 2, 3)
```

### Get Reference

```java
T getReferenceById(ID id);
```

**Khác với findById():**
- `findById()`: Load entity ngay lập tức
- `getReferenceById()`: Trả về proxy, chỉ load khi cần (lazy loading)

---

## 5. Query Method (Derived Query Methods)

Spring Data JPA tự động parse tên method và generate SQL.

**Cú pháp:**

```
findBy + PropertyName + Keyword + OrderBy + Property + Direction
```

**Ví dụ:**

```java
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Tìm theo email
    List<User> findByEmail(String email);
    
    // Tìm theo tuổi lớn hơn
    List<User> findByAgeGreaterThan(int age);
    
    // Tìm theo tên chứa keyword
    List<User> findByNameContaining(String keyword);
    
    // Tìm theo tuổi trong khoảng
    List<User> findByAgeBetween(int start, int end);
    
    // Tìm theo status và sắp xếp
    List<User> findByStatusOrderByCreatedAtDesc(String status);
    
    // Kết hợp nhiều điều kiện
    List<User> findByNameAndAgeGreaterThan(String name, int age);
    
    // Với Pagination
    Page<User> findByStatus(String status, Pageable pageable);
}
```

---

## 6. Custom Query với @Query

### JPQL (Java Persistence Query Language)

```java
@Query("SELECT u FROM User u WHERE u.email = :email")
Optional<User> findByEmail(@Param("email") String email);

@Query("SELECT u FROM User u WHERE u.age > :age AND u.status = :status")
List<User> findActiveUsers(@Param("age") int age, @Param("status") String status);
```

**Chi tiết về @Query:**
- **Target:** Method level
- **Purpose:** Định nghĩa custom query thay vì dùng derived query
- **Attributes:**
  - `value`: JPQL hoặc SQL query string
  - `nativeQuery`: `true` nếu dùng native SQL, mặc định `false` (JPQL)
  - `countQuery`: Query riêng để đếm (dùng với Pagination)
  - `name`: Tên của named query đã định nghĩa trong entity

**Chi tiết về @Param:**
- **Target:** Parameter level
- **Purpose:** Bind parameter name trong query với method parameter
- **Attributes:**
  - `value`: Tên parameter trong query (`:email`, `:age`, etc.)

### Native SQL

```java
@Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
Optional<User> findByEmailNative(@Param("email") String email);

@Query(value = "SELECT * FROM users WHERE created_at > :date", nativeQuery = true)
List<User> findRecentUsers(@Param("date") LocalDateTime date);
```

**Khi nào dùng Native SQL:**
- Cần dùng database-specific functions
- Query phức tạp mà JPQL không hỗ trợ
- Performance optimization với raw SQL

---

## 7. Update / Delete Custom Query

### @Modifying

```java
@Modifying
@Transactional
@Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
int updateUserName(@Param("id") Long id, @Param("name") String name);

@Modifying
@Transactional
@Query("DELETE FROM User u WHERE u.status = :status")
int deleteByStatus(@Param("status") String status);
```

**Chi tiết về @Modifying:**
- **Target:** Method level
- **Purpose:** Đánh dấu query là UPDATE hoặc DELETE (thay đổi dữ liệu)
- **Attributes:**
  - `clearAutomatically`: `true` để clear Persistence Context sau khi thực thi (mặc định `false`)
  - `flushAutomatically`: `true` để flush trước khi thực thi (mặc định `false`)
- **Lưu ý:** 
  - **BẮT BUỘC** dùng cùng với `@Transactional`
  - Return type thường là `int` hoặc `void` (số bản ghi bị ảnh hưởng)
  - Không tự động update Persistence Context, cần clear manually nếu cần

**Ví dụ với clearAutomatically:**

```java
@Modifying(clearAutomatically = true)
@Transactional
@Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
int updateStatus(@Param("id") Long id, @Param("status") String status);
```

---

## 8. Các Keyword trong Derived Query Methods

| Keyword | Ví dụ | SQL tương đương |
|---------|-------|-----------------|
| `And` | `findByNameAndAge` | `WHERE name = ? AND age = ?` |
| `Or` | `findByNameOrAge` | `WHERE name = ? OR age = ?` |
| `Between` | `findByAgeBetween` | `WHERE age BETWEEN ? AND ?` |
| `LessThan` | `findByAgeLessThan` | `WHERE age < ?` |
| `LessThanEqual` | `findByAgeLessThanEqual` | `WHERE age <= ?` |
| `GreaterThan` | `findByAgeGreaterThan` | `WHERE age > ?` |
| `GreaterThanEqual` | `findByAgeGreaterThanEqual` | `WHERE age >= ?` |
| `Like` | `findByNameLike` | `WHERE name LIKE ?` |
| `NotLike` | `findByNameNotLike` | `WHERE name NOT LIKE ?` |
| `Containing` | `findByNameContaining` | `WHERE name LIKE '%?%'` |
| `StartingWith` | `findByNameStartingWith` | `WHERE name LIKE '?%'` |
| `EndingWith` | `findByNameEndingWith` | `WHERE name LIKE '%?'` |
| `In` | `findByIdIn` | `WHERE id IN (?)` |
| `NotIn` | `findByIdNotIn` | `WHERE id NOT IN (?)` |
| `Not` | `findByStatusNot` | `WHERE status <> ?` |
| `IsNull` | `findByDeletedAtIsNull` | `WHERE deleted_at IS NULL` |
| `IsNotNull` | `findByDeletedAtIsNotNull` | `WHERE deleted_at IS NOT NULL` |
| `True` | `findByActiveTrue` | `WHERE active = true` |
| `False` | `findByActiveFalse` | `WHERE active = false` |
| `OrderBy` | `findByStatusOrderByNameAsc` | `ORDER BY name ASC` |
| `Distinct` | `findDistinctByStatus` | `SELECT DISTINCT` |
| `First` / `Top` | `findFirstByOrderByAgeDesc` | `LIMIT 1` |

**Ví dụ phức tạp:**

```java
// Tìm 10 user đầu tiên có age > 18, status = ACTIVE, sắp xếp theo name
List<User> findTop10ByAgeGreaterThanAndStatusOrderByNameAsc(int age, String status);

// Tìm user có email chứa domain và không bị xóa
List<User> findByEmailContainingAndDeletedAtIsNull(String domain);
```

---

## 9. Transaction Management

**Mặc định trong Repository:**
- **Read operations:** Tự động có transaction (read-only)
- **Write operations:** Cần `@Transactional` ở Service layer

**Best Practice:**

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Read - không cần @Transactional
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    // Write - cần @Transactional
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    // Multiple operations - cần @Transactional
    @Transactional
    public void transferData(Long fromId, Long toId) {
        User from = userRepository.findById(fromId).get();
        User to = userRepository.findById(toId).get();
        // ... business logic
        userRepository.save(from);
        userRepository.save(to);
    }
}
```

**Chi tiết về @Transactional:**
- **Target:** Class hoặc Method level
- **Purpose:** Quản lý transaction boundary
- **Attributes quan trọng:**
  - `readOnly`: `true` cho read operations (optimize performance)
  - `propagation`: Cách transaction lan truyền (REQUIRED, REQUIRES_NEW, etc.)
  - `isolation`: Mức độ cô lập transaction (READ_COMMITTED, SERIALIZABLE, etc.)
  - `timeout`: Thời gian timeout (seconds)
  - `rollbackFor`: Exception nào sẽ trigger rollback
  - `noRollbackFor`: Exception nào không rollback

**Ví dụ nâng cao:**

```java
@Transactional(
    readOnly = false,
    propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_COMMITTED,
    timeout = 30,
    rollbackFor = Exception.class
)
public void complexOperation() {
    // Business logic
}
```

---

## 10. Methods thường dùng nhất trong thực tế

**90% thời gian bạn sẽ dùng:**

```java
// CRUD cơ bản
save(entity)
findById(id)
findAll()
deleteById(id)
existsById(id)
count()

// Pagination
findAll(Pageable)

// Custom queries
findByEmail(email)
findByStatus(status)
findByNameContaining(keyword)
```

**Ví dụ Service thực tế:**

```java
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    public Page<User> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return userRepository.findAll(pageable);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
```

---

## 11. Tổng kết các Annotations quan trọng

### @Query
- **Mục đích:** Định nghĩa custom JPQL hoặc Native SQL query
- **Vị trí:** Method level trong Repository interface
- **Thuộc tính:**
  - `value`: Query string
  - `nativeQuery = true`: Sử dụng native SQL
  - `countQuery`: Query đếm riêng cho pagination
  - `name`: Tham chiếu đến named query

### @Param
- **Mục đích:** Bind parameter name trong query với method parameter
- **Vị trí:** Parameter level
- **Bắt buộc:** Khi dùng named parameters (`:paramName`) trong @Query

### @Modifying
- **Mục đích:** Đánh dấu query là UPDATE/DELETE
- **Vị trí:** Method level, dùng cùng @Query
- **Thuộc tính:**
  - `clearAutomatically = true`: Clear EntityManager sau khi execute
  - `flushAutomatically = true`: Flush trước khi execute
- **Bắt buộc:** Phải dùng cùng @Transactional

### @Transactional
- **Mục đích:** Quản lý transaction boundary
- **Vị trí:** Class hoặc Method level (thường ở Service layer)
- **Thuộc tính quan trọng:**
  - `readOnly = true`: Optimize cho read operations
  - `propagation`: REQUIRED, REQUIRES_NEW, NESTED, etc.
  - `isolation`: READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE
  - `timeout`: Timeout in seconds
  - `rollbackFor`: Exceptions trigger rollback
  - `noRollbackFor`: Exceptions không rollback