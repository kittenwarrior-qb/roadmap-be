# Spring Data JPA

## Giới thiệu

Spring Data JPA thực sự là một lớp trừu tượng (abstraction layer) mạnh mẽ, giúp bạn giảm thiểu đáng kể lượng mã boilerplate (code lặp lại) khi làm việc với JPA (Java Persistence API).

Spring Data JPA là một phần của dự án Spring Data lớn hơn, cung cấp một cách tiếp cận nhất quán để truy cập dữ liệu trên nhiều công nghệ lưu trữ khác nhau. Nó xây dựng trên nền tảng JPA (Java Persistence API) và Hibernate, giúp đơn giản hóa việc phát triển các ứng dụng Java cần tương tác với cơ sở dữ liệu quan hệ.

## Tính năng nổi bật

Spring Data JPA cung cấp nhiều tính năng mạnh mẽ:

1. **Repository Pattern**: Tạo và hỗ trợ các repository được xây dựng với Spring và JPA, cho phép bạn định nghĩa interface mà không cần viết implementation.

2. **Query Methods**: Hỗ trợ QueryDSL và các truy vấn JPA tùy chỉnh. Bạn có thể tạo truy vấn chỉ bằng cách đặt tên phương thức theo quy ước.

3. **Auditing**: Chức năng Audit (kiểm toán) cho các lớp domain, tự động ghi lại thông tin như:
   - Thời gian tạo (createdDate)
   - Người tạo (createdBy)
   - Thời gian cập nhật (lastModifiedDate)
   - Người cập nhật (lastModifiedBy)

4. **Advanced Features**: 
   - Hỗ trợ tải theo lô (batch loading)
   - Phân loại (sorting) và phân trang (pagination)
   - Các truy vấn động (dynamic queries)
   - Specifications API cho truy vấn phức tạp

5. **Flexible Mapping**: Hỗ trợ ánh xạ XML cho các entity (mặc dù annotation là cách phổ biến hơn).

6. **Minimal Code**: Giảm thiểu kích thước mã nguồn cho các thao tác CRUD cơ bản bằng cách sử dụng:
   - `CrudRepository` - các thao tác CRUD cơ bản
   - `JpaRepository` - mở rộng CrudRepository với các tính năng JPA bổ sung
   - `PagingAndSortingRepository` - hỗ trợ phân trang và sắp xếp

## Khi nào nên sử dụng Spring Data JPA?

Spring Data JPA là lựa chọn lý tưởng khi:

- Bạn cần nhanh chóng tạo một lớp repository dựa trên JPA mà chủ yếu phục vụ các thao tác CRUD (Create, Read, Update, Delete)
- Bạn không muốn tự tay tạo các abstract DAO (Data Access Object) hay triển khai các interface với quá nhiều mã lặp lại
- Bạn muốn tập trung vào logic nghiệp vụ thay vì sa lầy vào chi tiết triển khai tầng bền vững (persistence layer)
- Dự án của bạn sử dụng cơ sở dữ liệu quan hệ (MySQL, PostgreSQL, Oracle, SQL Server, v.v.)
- Bạn cần các tính năng như auditing, pagination, hoặc dynamic queries

## Kiến trúc Spring Data JPA

```
Application Layer
       ↓
Service Layer
       ↓
Repository Interface (Spring Data JPA)
       ↓
JPA Implementation (Hibernate)
       ↓
JDBC
       ↓
Database
```

## Ví dụ thực tế

Với ví dụ về Spring Data JPA của chúng ta, chúng ta sẽ xây dựng một dịch vụ web RESTful đơn giản kết nối với cơ sở dữ liệu PostgreSQL. Chúng ta sẽ triển khai các thao tác CRUD cơ bản trên một tập dữ liệu mẫu về người dùng (people).

### Dữ liệu mẫu

Sử dụng truy vấn SQL dưới đây để tạo bảng trong cơ sở dữ liệu PostgreSQL và thêm một số dữ liệu kiểm thử ban đầu:

```sql
-- Tạo bảng people
CREATE TABLE people (
    id SERIAL NOT NULL PRIMARY KEY,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    age INTEGER NOT NULL
);

-- Thêm dữ liệu mẫu
INSERT INTO people (id, first_name, last_name, age) VALUES
    (1, 'Vlad', 'Boyarskiy', 21),
    (2, 'Oksi', 'Bahatskaya', 30),
    (3, 'Vadim', 'Vadimich', 32);

-- Reset sequence để đảm bảo ID tiếp theo đúng
SELECT setval('people_id_seq', (SELECT MAX(id) FROM people));
```

### Cấu trúc Entity

```java
@Entity
@Table(name = "people")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;
    
    @Column(nullable = false)
    private Integer age;
    
    // Constructors, getters, setters
}
```

### Repository Interface

```java
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Spring Data JPA tự động tạo implementation
    
    // Query methods - Spring tự động tạo truy vấn từ tên phương thức
    List<Person> findByLastName(String lastName);
    List<Person> findByAgeGreaterThan(Integer age);
    List<Person> findByFirstNameAndLastName(String firstName, String lastName);
    
    // Custom query với @Query annotation
    @Query("SELECT p FROM Person p WHERE p.age BETWEEN :minAge AND :maxAge")
    List<Person> findByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
}
```

### Service Layer

```java
@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    
    public List<Person> getAllPeople() {
        return personRepository.findAll();
    }
    
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
    }
    
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }
    
    public Person updatePerson(Long id, Person personDetails) {
        Person person = getPersonById(id);
        person.setFirstName(personDetails.getFirstName());
        person.setLastName(personDetails.getLastName());
        person.setAge(personDetails.getAge());
        return personRepository.save(person);
    }
    
    public void deletePerson(Long id) {
        Person person = getPersonById(id);
        personRepository.delete(person);
    }
}
```

## Các Repository Interface chính

### 1. Repository<T, ID>
Interface cơ bản nhất, đánh dấu một interface là repository.

### 2. CrudRepository<T, ID>
Cung cấp các phương thức CRUD cơ bản:
- `save(S entity)` - Lưu entity
- `findById(ID id)` - Tìm theo ID
- `findAll()` - Lấy tất cả
- `count()` - Đếm số lượng
- `delete(T entity)` - Xóa entity
- `existsById(ID id)` - Kiểm tra tồn tại

### 3. PagingAndSortingRepository<T, ID>
Mở rộng CrudRepository với:
- `findAll(Sort sort)` - Lấy tất cả với sắp xếp
- `findAll(Pageable pageable)` - Lấy tất cả với phân trang

### 4. JpaRepository<T, ID>
Interface đầy đủ nhất, mở rộng PagingAndSortingRepository với:
- `flush()` - Đồng bộ persistence context với database
- `saveAndFlush(T entity)` - Lưu và flush ngay lập tức
- `deleteInBatch(Iterable<T> entities)` - Xóa hàng loạt
- `getOne(ID id)` - Lấy reference (lazy loading)

## Query Methods

Spring Data JPA cho phép tạo truy vấn từ tên phương thức:

```java
// Tìm theo một thuộc tính
List<Person> findByFirstName(String firstName);

// Tìm với điều kiện AND
List<Person> findByFirstNameAndLastName(String firstName, String lastName);

// Tìm với điều kiện OR
List<Person> findByFirstNameOrLastName(String firstName, String lastName);

// So sánh
List<Person> findByAgeGreaterThan(Integer age);
List<Person> findByAgeLessThanEqual(Integer age);
List<Person> findByAgeBetween(Integer startAge, Integer endAge);

// Pattern matching
List<Person> findByFirstNameLike(String pattern);
List<Person> findByFirstNameContaining(String substring);
List<Person> findByFirstNameStartingWith(String prefix);

// Sắp xếp
List<Person> findByLastNameOrderByAgeDesc(String lastName);

// Giới hạn kết quả
Person findFirstByOrderByAgeDesc();
List<Person> findTop3ByOrderByAgeDesc();

// Kiểm tra tồn tại
boolean existsByFirstName(String firstName);

// Đếm
long countByLastName(String lastName);

// Xóa
void deleteByFirstName(String firstName);
```

## Custom Queries với @Query

```java
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    // JPQL Query
    @Query("SELECT p FROM Person p WHERE p.age > :age")
    List<Person> findPeopleOlderThan(@Param("age") Integer age);
    
    // Native SQL Query
    @Query(value = "SELECT * FROM people WHERE age > :age", nativeQuery = true)
    List<Person> findPeopleOlderThanNative(@Param("age") Integer age);
    
    // Update Query
    @Modifying
    @Query("UPDATE Person p SET p.age = :age WHERE p.id = :id")
    int updatePersonAge(@Param("id") Long id, @Param("age") Integer age);
    
    // Delete Query
    @Modifying
    @Query("DELETE FROM Person p WHERE p.age < :age")
    int deletePeopleYoungerThan(@Param("age") Integer age);
}
```

## Pagination và Sorting

```java
@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    
    // Phân trang
    public Page<Person> getPeople(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return personRepository.findAll(pageable);
    }
    
    // Sắp xếp
    public List<Person> getPeopleSorted() {
        Sort sort = Sort.by(Sort.Direction.DESC, "age");
        return personRepository.findAll(sort);
    }
    
    // Kết hợp phân trang và sắp xếp
    public Page<Person> getPeopleSortedAndPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("lastName").ascending());
        return personRepository.findAll(pageable);
    }
}
```

## Auditing

Để bật tính năng auditing, thêm annotation vào entity:

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    private String lastName;
    private Integer age;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;
    
    @LastModifiedBy
    private String lastModifiedBy;
}
```

Bật auditing trong configuration:

```java
@Configuration
@EnableJpaAuditing
public class JpaConfig {
    
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("System"); // Hoặc lấy từ SecurityContext
    }
}
```

## Specifications API

Cho các truy vấn động phức tạp:

```java
public class PersonSpecifications {
    
    public static Specification<Person> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("firstName"), firstName);
    }
    
    public static Specification<Person> ageGreaterThan(Integer age) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThan(root.get("age"), age);
    }
}

// Sử dụng
public interface PersonRepository extends JpaRepository<Person, Long>, 
                                          JpaSpecificationExecutor<Person> {
}

// Trong service
List<Person> people = personRepository.findAll(
    Specification.where(hasFirstName("Vlad"))
                 .and(ageGreaterThan(20))
);
```

## Configuration

### application.properties

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect

logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

## Dependencies (Maven)

```xml
<dependencies>
    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Spring Boot Starter Web (cho REST API) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

## Dependencies (Gradle)

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    runtimeOnly 'org.postgresql:postgresql'
}
```

## Best Practices

1. **Sử dụng Optional**: Luôn xử lý trường hợp không tìm thấy entity
   ```java
   Optional<Person> person = personRepository.findById(id);
   ```

2. **Transaction Management**: Sử dụng `@Transactional` cho các thao tác phức tạp
   ```java
   @Transactional
   public void updateMultiplePeople() {
       // Multiple database operations
   }
   ```

3. **Lazy Loading**: Cẩn thận với lazy loading và LazyInitializationException
   ```java
   @Transactional(readOnly = true)
   public Person getPersonWithDetails(Long id) {
       Person person = personRepository.findById(id).orElseThrow();
       person.getOrders().size(); // Force initialization
       return person;
   }
   ```

4. **Projection**: Sử dụng projection để chỉ lấy dữ liệu cần thiết
   ```java
   public interface PersonNameOnly {
       String getFirstName();
       String getLastName();
   }
   
   List<PersonNameOnly> findAllProjectedBy();
   ```

5. **Batch Operations**: Sử dụng batch cho nhiều thao tác
   ```java
   personRepository.saveAll(listOfPeople);
   personRepository.deleteAllInBatch(listOfPeople);
   ```

## So sánh: JPA thuần vs Spring Data JPA

### JPA thuần (Traditional JPA)

```java
@Repository
public class PersonDaoImpl implements PersonDao {
    @PersistenceContext
    private EntityManager entityManager;
    
    public Person findById(Long id) {
        return entityManager.find(Person.class, id);
    }
    
    public List<Person> findAll() {
        return entityManager.createQuery("SELECT p FROM Person p", Person.class)
                           .getResultList();
    }
    
    public Person save(Person person) {
        if (person.getId() == null) {
            entityManager.persist(person);
            return person;
        } else {
            return entityManager.merge(person);
        }
    }
    
    public void delete(Long id) {
        Person person = findById(id);
        if (person != null) {
            entityManager.remove(person);
        }
    }
}
```

### Spring Data JPA

```java
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Tất cả các phương thức trên đã có sẵn!
    // Không cần viết implementation
}
```

## Lợi ích chính

- **Giảm boilerplate code**: Không cần viết implementation cho các thao tác CRUD cơ bản
- **Type-safe**: Compile-time checking cho các truy vấn
- **Maintainability**: Code ngắn gọn, dễ bảo trì
- **Productivity**: Phát triển nhanh hơn, tập trung vào business logic
- **Consistency**: Cách tiếp cận nhất quán cho data access layer
- **Testing**: Dễ dàng mock và test

## Kết luận

Spring Data JPA là một công cụ mạnh mẽ giúp đơn giản hóa việc làm việc với cơ sở dữ liệu trong ứng dụng Spring. Bằng cách cung cấp các abstraction và convention-over-configuration, nó cho phép developers tập trung vào logic nghiệp vụ thay vì phải viết nhiều code lặp lại cho data access layer.
