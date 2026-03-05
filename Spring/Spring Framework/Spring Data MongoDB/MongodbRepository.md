# MongoRepository

## I. Interface Chính

Tương tự JPA, MongoDB có:

```java
MongoRepository<T, ID>
```

**Ví dụ:**
```java
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // MongoDB dùng String cho ObjectId
}
```

---

## II. Các Hàm CRUD Có Sẵn

Giống JPA gần như 100%.

### CREATE / UPDATE
```java
<S extends T> S save(S entity);
<S extends T> List<S> saveAll(Iterable<S> entities);
```

**Lưu ý:** MongoDB không phân biệt insert/update như SQL:
- Nếu `_id` chưa tồn tại → insert
- Nếu `_id` đã tồn tại → update

### READ
```java
Optional<T> findById(ID id);
List<T> findAll();
List<T> findAllById(Iterable<ID> ids);
boolean existsById(ID id);
long count();
```

### DELETE
```java
void deleteById(ID id);
void delete(T entity);
void deleteAll();
void deleteAllById(Iterable<ID> ids);
```

---

## III. Paging & Sorting

MongoRepository hỗ trợ phân trang và sắp xếp:

```java
Page<T> findAll(Pageable pageable);
List<T> findAll(Sort sort);
```

**Ví dụ sử dụng:**
```java
// Paging
Pageable pageable = PageRequest.of(0, 10);  // Page 0, size 10
Page<User> users = userRepository.findAll(pageable);

// Sorting
Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
List<User> users = userRepository.findAll(sort);

// Kết hợp cả hai
Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
Page<User> users = userRepository.findAll(pageable);
```

---

## IV. Query Methods (Auto Generate)

Spring tự động parse tên method như JPA.

**Các từ khóa hỗ trợ:**
```java
// Equality
List<User> findByEmail(String email);
List<User> findByNameAndAge(String name, int age);

// Comparison
List<User> findByAgeGreaterThan(int age);
List<User> findByAgeLessThanEqual(int age);
List<User> findByAgeBetween(int start, int end);

// String operations
List<User> findByNameContaining(String keyword);
List<User> findByNameStartingWith(String prefix);
List<User> findByNameEndingWith(String suffix);
List<User> findByNameIgnoreCase(String name);

// Collection operations
List<User> findByRoleIn(List<String> roles);
List<User> findByRoleNotIn(List<String> roles);

// Null checks
List<User> findByEmailIsNull();
List<User> findByEmailIsNotNull();

// Boolean
List<User> findByActiveTrue();
List<User> findByActiveFalse();

// Sorting
List<User> findByAgeOrderByNameAsc(int age);
List<User> findByAgeOrderByNameDesc(int age);

// Limiting
User findFirstByOrderByCreatedAtDesc();
List<User> findTop10ByOrderByScoreDesc();
```

---

## V. Custom Query với @Query

Xem chi tiết trong file `Annotions.md` → Section II.

**Ví dụ nhanh:**
```java
@Query("{ 'email': ?0 }")
Optional<User> findByEmail(String email);

@Query("{ 'age': { $gte: ?0, $lte: ?1 } }")
List<User> findByAgeRange(int min, int max);
```

---

## VI. Aggregation Pipeline

Xem chi tiết trong file `Annotions.md` → Section II.

**Ví dụ nhanh:**
```java
@Aggregation(pipeline = {
    "{ '$match': { 'age': { '$gt': ?0 } } }",
    "{ '$group': { '_id': '$role', 'count': { '$sum': 1 } } }"
})
List<RoleCount> countUsersByRole(int minAge);
```

---

## VII. MongoTemplate (Advanced)

Nếu cần query phức tạp hơn, dùng `MongoTemplate`:

```java
@Service
public class UserService {
    @Autowired
    private MongoTemplate mongoTemplate;
    
    public List<User> complexQuery() {
        Query query = new Query();
        query.addCriteria(Criteria.where("age").gte(18)
            .and("active").is(true));
        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        query.limit(10);
        
        return mongoTemplate.find(query, User.class);
    }
}
```

---

## 🎯 So Sánh với JpaRepository

| Feature | JpaRepository | MongoRepository |
|---------|---------------|-----------------|
| Interface | `JpaRepository<T, ID>` | `MongoRepository<T, ID>` |
| CRUD methods | ✅ Giống nhau | ✅ Giống nhau |
| Query methods | ✅ Giống nhau | ✅ Giống nhau |
| Custom query | `@Query` với JPQL | `@Query` với JSON |
| Aggregation | JPQL/Native SQL | `@Aggregation` pipeline |
| ID type | Long, Integer... | String (ObjectId) |
| Relationships | `@OneToMany`, `@ManyToOne` | `@DBRef`, `@DocumentReference` |