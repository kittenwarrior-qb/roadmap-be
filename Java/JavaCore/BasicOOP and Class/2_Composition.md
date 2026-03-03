# Java Composition - Tóm Tắt

## Khái Niệm
Composition là kỹ thuật tạo mối quan hệ HAS-A giữa các objects bằng cách sử dụng instance variables tham chiếu đến objects khác.

**Ví dụ:** Person HAS-A Job, Car HAS-A Engine, House HAS-A Room

---

## Ví Dụ Cơ Bản

### Class Job
```java
package com.journaldev.composition;

public class Job {
    private String role;
    private long salary;
    private int id;
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public long getSalary() {
        return salary;
    }
    
    public void setSalary(long salary) {
        this.salary = salary;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
```

### Class Person (Sử dụng Composition)
```java
package com.journaldev.composition;

public class Person {
    // Composition: Person HAS-A Job
    private Job job;
    
    public Person() {
        this.job = new Job();  // Tạo Job object
        job.setSalary(1000L);
    }
    
    public long getSalary() {
        return job.getSalary();  // Delegate to Job
    }
}
```

### Test Program
```java
package com.journaldev.composition;

public class TestPerson {
    public static void main(String[] args) {
        Person person = new Person();
        long salary = person.getSalary();
        System.out.println("Salary: " + salary);  // Output: 1000
    }
}
```

---

## Ví Dụ Thực Tế 1: Car và Engine

```java
// Engine class
class Engine {
    private String type;
    private int horsepower;
    
    public Engine(String type, int horsepower) {
        this.type = type;
        this.horsepower = horsepower;
    }
    
    public void start() {
        System.out.println(type + " engine started with " + horsepower + " HP");
    }
    
    public void stop() {
        System.out.println("Engine stopped");
    }
}

// Car class - Composition
class Car {
    private String model;
    private Engine engine;  // Car HAS-A Engine
    
    public Car(String model, String engineType, int horsepower) {
        this.model = model;
        this.engine = new Engine(engineType, horsepower);
    }
    
    public void start() {
        System.out.println("Starting " + model);
        engine.start();  // Delegate to Engine
    }
    
    public void stop() {
        System.out.println("Stopping " + model);
        engine.stop();
    }
}

// Sử dụng
public class TestCar {
    public static void main(String[] args) {
        Car car = new Car("Tesla Model 3", "Electric", 283);
        car.start();
        // Output:
        // Starting Tesla Model 3
        // Electric engine started with 283 HP
        
        car.stop();
        // Output:
        // Stopping Tesla Model 3
        // Engine stopped
    }
}
```

---

## Ví Dụ Thực Tế 2: Computer và Components

```java
// Processor class
class Processor {
    private String brand;
    private int cores;
    
    public Processor(String brand, int cores) {
        this.brand = brand;
        this.cores = cores;
    }
    
    public void displayInfo() {
        System.out.println("Processor: " + brand + ", Cores: " + cores);
    }
}

// Memory class
class Memory {
    private int sizeGB;
    private String type;
    
    public Memory(int sizeGB, String type) {
        this.sizeGB = sizeGB;
        this.type = type;
    }
    
    public void displayInfo() {
        System.out.println("Memory: " + sizeGB + "GB " + type);
    }
}

// Computer class - Multiple Composition
class Computer {
    private String brand;
    private Processor processor;  // Computer HAS-A Processor
    private Memory memory;        // Computer HAS-A Memory
    
    public Computer(String brand, String cpuBrand, int cores, int memoryGB, String memType) {
        this.brand = brand;
        this.processor = new Processor(cpuBrand, cores);
        this.memory = new Memory(memoryGB, memType);
    }
    
    public void displaySpecs() {
        System.out.println("=== " + brand + " Computer ===");
        processor.displayInfo();
        memory.displayInfo();
    }
}

// Sử dụng
public class TestComputer {
    public static void main(String[] args) {
        Computer pc = new Computer("Dell", "Intel i7", 8, 16, "DDR4");
        pc.displaySpecs();
        // Output:
        // === Dell Computer ===
        // Processor: Intel i7, Cores: 8
        // Memory: 16GB DDR4
    }
}
```

---

## Ví Dụ Thực Tế 3: University System

```java
// Address class
class Address {
    private String street;
    private String city;
    private String zipCode;
    
    public Address(String street, String city, String zipCode) {
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }
    
    public String getFullAddress() {
        return street + ", " + city + " " + zipCode;
    }
}

// Student class
class Student {
    private String name;
    private int id;
    private Address address;  // Student HAS-A Address
    
    public Student(String name, int id, String street, String city, String zipCode) {
        this.name = name;
        this.id = id;
        this.address = new Address(street, city, zipCode);
    }
    
    public void displayInfo() {
        System.out.println("Student ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address.getFullAddress());
    }
}

// Sử dụng
public class TestStudent {
    public static void main(String[] args) {
        Student student = new Student("John Doe", 12345, 
                                     "123 Main St", "New York", "10001");
        student.displayInfo();
        // Output:
        // Student ID: 12345
        // Name: John Doe
        // Address: 123 Main St, New York 10001
    }
}
```

---

## Lợi Ích của Composition

### 1. Kiểm Soát Visibility
```java
class BankAccount {
    private AccountDetails details;  // Private - client không truy cập trực tiếp
    
    public BankAccount(String accountNumber, double balance) {
        this.details = new AccountDetails(accountNumber, balance);
    }
    
    // Chỉ expose những gì cần thiết
    public double getBalance() {
        return details.getBalance();
    }
    
    // Client không thể thay đổi accountNumber
    // Không có setAccountNumber() method
}
```

### 2. Code Reuse
```java
class Logger {
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

// Nhiều class có thể reuse Logger
class UserService {
    private Logger logger = new Logger();
    
    public void createUser(String name) {
        logger.log("Creating user: " + name);
        // Create user logic
    }
}

class OrderService {
    private Logger logger = new Logger();
    
    public void createOrder(int orderId) {
        logger.log("Creating order: " + orderId);
        // Create order logic
    }
}
```

### 3. Flexibility - Thay Đổi Implementation
```java
// Nếu Job.getSalary() thay đổi từ long sang String
class Job {
    public String getSalary() {  // Changed to String
        return "1000 USD";
    }
}

// Chỉ cần thay đổi Person class
class Person {
    private Job job;
    
    public Person() {
        this.job = new Job();
    }
    
    // Adapt the change here
    public String getSalaryInfo() {
        return job.getSalary();  // Now returns String
    }
}

// Client code không cần thay đổi
public class TestPerson {
    public static void main(String[] args) {
        Person person = new Person();
        String salary = person.getSalaryInfo();  // Still works
    }
}
```

### 4. Lazy Initialization
```java
class Person {
    private Job job;  // Không khởi tạo ngay
    
    public Person() {
        // Job chưa được tạo
    }
    
    // Tạo Job khi cần
    public long getSalary() {
        if (job == null) {
            job = new Job();  // Lazy initialization
            job.setSalary(1000L);
        }
        return job.getSalary();
    }
}
```

---

## So Sánh: Composition vs Inheritance

### Khi Nào Dùng Composition?

**Dùng Composition khi:**
- Có mối quan hệ HAS-A (Car HAS-A Engine)
- Muốn code reuse nhưng không có IS-A relationship
- Cần flexibility để thay đổi behavior tại runtime
- Muốn kiểm soát visibility của composed objects

**Ví dụ:**
```java
// ĐÚNG: Composition
class Car {
    private Engine engine;  // Car HAS-A Engine
}

// SAI: Inheritance
class Car extends Engine {  // Car IS-A Engine? NO!
}
```

### Ví Dụ So Sánh

**Inheritance (IS-A):**
```java
class Animal {
    public void eat() {
        System.out.println("Eating...");
    }
}

class Dog extends Animal {  // Dog IS-A Animal ✓
    public void bark() {
        System.out.println("Barking...");
    }
}
```

**Composition (HAS-A):**
```java
class Tail {
    public void wag() {
        System.out.println("Wagging tail...");
    }
}

class Dog {
    private Tail tail;  // Dog HAS-A Tail ✓
    
    public Dog() {
        this.tail = new Tail();
    }
    
    public void wagTail() {
        tail.wag();
    }
}
```

---

## Best Practices

### 1. Favor Composition Over Inheritance
```java
// Thay vì inheritance phức tạp
class FlyingCar extends Car {  // Tight coupling
    // ...
}

// Dùng composition
class Car {
    private FlyingCapability flyingCapability;  // Flexible
    
    public void enableFlying() {
        this.flyingCapability = new FlyingCapability();
    }
}
```

### 2. Encapsulation với Composition
```java
class Person {
    private Job job;  // Private - encapsulated
    
    // Controlled access
    public String getJobTitle() {
        return job != null ? job.getRole() : "Unemployed";
    }
    
    // Validation
    public void setJob(Job job) {
        if (job != null && job.getSalary() > 0) {
            this.job = job;
        }
    }
}
```

### 3. Dependency Injection
```java
// Tốt hơn: Inject dependency
class Person {
    private Job job;
    
    // Constructor injection
    public Person(Job job) {
        this.job = job;
    }
    
    // Setter injection
    public void setJob(Job job) {
        this.job = job;
    }
}

// Sử dụng
Job job = new Job();
job.setSalary(2000L);
Person person = new Person(job);  // Flexible, testable
```

---

## Tổng Kết

### Composition Là Gì?
- HAS-A relationship giữa objects
- Sử dụng instance variables để tham chiếu objects khác
- Strong ownership: object con không tồn tại độc lập

### Lợi Ích Chính
1. **Code Reuse:** Tái sử dụng code hiệu quả
2. **Flexibility:** Dễ thay đổi implementation
3. **Encapsulation:** Kiểm soát visibility tốt hơn
4. **Loose Coupling:** Giảm phụ thuộc giữa classes
5. **Testability:** Dễ test hơn (có thể mock dependencies)

### Khi Nào Dùng?
- ✓ Có mối quan hệ HAS-A
- ✓ Cần code reuse nhưng không phải IS-A
- ✓ Muốn thay đổi behavior tại runtime
- ✓ Cần kiểm soát chặt chẽ dependencies

### Remember
**"Favor Composition Over Inheritance"** - Một trong những best practices quan trọng nhất trong Java!
