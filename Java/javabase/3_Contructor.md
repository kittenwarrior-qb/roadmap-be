# Constructor trong Java

## Khái niệm
Constructor là phương thức đặc biệt dùng để khởi tạo đối tượng:
- Tên giống tên class
- Không có return type (kể cả `void`)
- Tự động được gọi khi dùng `new`

```java
public Employee() {
    System.out.println("Employee Constructor");
}
```

## Các loại Constructor

### 1. Default Constructor
Java tự động tạo nếu không định nghĩa constructor nào.

```java
public class Data {
    // Java tự tạo: public Data() {}
}
```

### 2. No-Args Constructor
Không có tham số nhưng có logic khởi tạo.

```java
public Data() {
    System.out.println("Khởi tạo Data");
}
```

### 3. Parameterized Constructor
Nhận tham số để khởi tạo giá trị.

```java
public Data(String name, int id) {
    this.name = name;
    this.id = id;
}
```

## Constructor Overloading
Định nghĩa nhiều constructor với tham số khác nhau:

```java
public class Data {
    private String name;
    private int id;

    public Data() {
        this.name = "Default";
    }

    public Data(String name) {
        this.name = name;
    }

    public Data(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
```

## Constructor Chaining
Gọi constructor khác trong cùng class bằng `this()`:

```java
public class Employee {
    private int id;
    private String name;
    
    public Employee() {
        this("John Doe", 999);  // Phải là dòng đầu tiên
    }
    
    public Employee(int id) {
        this("John Doe", id);
    }
    
    public Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
```

## Private Constructor
Dùng cho Singleton Pattern:

```java
public class Singleton {
    private static Singleton instance;
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

## Super Constructor
Gọi constructor của class cha bằng `super()`:

```java
public class Person {
    private int age;
    
    public Person(int age) {
        this.age = age;
    }
}

public class Student extends Person {
    private String name;
    
    public Student(int age, String name) {
        super(age);  // Phải là dòng đầu tiên
        this.name = name;
    }
}
```

**Lưu ý:** Nếu không gọi `super()`, Java tự động gọi `super()` no-args.

## Copy Constructor
Tạo bản sao độc lập của object (deep copy):

```java
public class Fruits {
    private List<String> fruitsList;
    
    public Fruits(List<String> fruits) {
        this.fruitsList = fruits;
    }
    
    // Copy constructor
    public Fruits(Fruits other) {
        this.fruitsList = new ArrayList<>();
        for (String fruit : other.fruitsList) {
            this.fruitsList.add(fruit);
        }
    }
}

// Sử dụng
Fruits original = new Fruits(Arrays.asList("Apple", "Banana"));
Fruits copy = new Fruits(original);  // Deep copy
copy.getFruitsList().add("Orange");  // Không ảnh hưởng original
```

## Constructor trong Framework

### Spring - Constructor Injection
```java
@Component
public class UserService {
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### Hibernate - Yêu cầu No-Args Constructor
```java
@Entity
public class User {
    @Id
    private Long id;
    private String name;
    
    // Bắt buộc cho Hibernate
    public User() {}
    
    public User(String name) {
        this.name = name;
    }
}
```

## So sánh Constructor vs Method

| Đặc điểm | Constructor | Method |
|----------|-------------|---------|
| Tên | Giống tên class | Tùy ý |
| Return type | Không có | Có |
| Gọi | Tự động khi `new` | Phải gọi thủ công |
| Overriding | Không | Có |
| Static | Không | Có |

## Lợi ích của Constructor
- Đảm bảo object luôn được khởi tạo đúng
- Tập trung logic khởi tạo ở một chỗ
- Hỗ trợ immutability và dependency injection
- Bắt buộc truyền tham số cần thiết ngay từ đầu

## Lưu ý quan trọng
1. Constructor không có return type (kể cả `void`)
2. `this()` và `super()` phải là câu lệnh đầu tiên
3. Không thể dùng `abstract`, `final`, `static` với constructor
4. Hibernate/JPA yêu cầu no-args constructor
5. Spring khuyến khích dùng constructor injection
