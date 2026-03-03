# Java OOP (Object-Oriented Programming) - Tóm Tắt

## 7 Khái Niệm OOP Cốt Lõi

1. **Abstraction** (Trừu tượng hóa)
2. **Encapsulation** (Đóng gói)
3. **Polymorphism** (Đa hình)
4. **Inheritance** (Kế thừa)
5. **Association** (Liên kết)
6. **Aggregation** (Tập hợp)
7. **Composition** (Kết hợp)

---

## 1. Abstraction (Trừu Tượng Hóa)

### Khái Niệm
Ẩn giấu chi tiết triển khai phức tạp, chỉ hiển thị những gì cần thiết.

### Ví Dụ Thực Tế
- Khi bạn lái xe, bạn chỉ cần biết ga, phanh, vô lăng → không cần biết động cơ hoạt động thế nào
- ATM: Bạn rút tiền mà không cần biết cách máy xử lý giao dịch

### Code Example
```java
// Abstract class
abstract class Vehicle {
    // Abstract method - không có implementation
    abstract void start();
    
    // Concrete method
    void stop() {
        System.out.println("Vehicle stopped");
    }
}

class Car extends Vehicle {
    @Override
    void start() {
        System.out.println("Car starts with key");
    }
}

class Bike extends Vehicle {
    @Override
    void start() {
        System.out.println("Bike starts with kick");
    }
}

// Sử dụng
Vehicle v1 = new Car();
v1.start();  // Car starts with key
v1.stop();   // Vehicle stopped
```

### Cách Đạt Được Abstraction
- **Abstract classes** (50-100% abstraction)
- **Interfaces** (100% abstraction)
- **Encapsulation**

---

## 2. Encapsulation (Đóng Gói)

### Khái Niệm
Gói dữ liệu (variables) và code (methods) thành một đơn vị, kiểm soát truy cập bằng access modifiers.

### Lợi Ích
- Bảo vệ dữ liệu khỏi truy cập trái phép
- Dễ bảo trì và thay đổi code
- Tăng tính linh hoạt

### Code Example
```java
public class BankAccount {
    // Private variables - không truy cập trực tiếp từ bên ngoài
    private String accountNumber;
    private double balance;
    
    // Constructor
    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }
    
    // Public getter
    public double getBalance() {
        return balance;
    }
    
    // Public setter với validation
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid amount");
        }
    }
    
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount");
        }
    }
}

// Sử dụng
BankAccount account = new BankAccount("123456", 1000);
// account.balance = -500;  // ERROR! Không thể truy cập trực tiếp
account.deposit(500);       // OK - qua method có validation
System.out.println(account.getBalance());  // 1500
```

### Access Modifiers
- **private:** Chỉ trong class
- **default:** Trong cùng package
- **protected:** Trong package + subclass
- **public:** Mọi nơi

---

## 3. Polymorphism (Đa Hình)

### Khái Niệm
Một object có thể có nhiều hình thái khác nhau. "Poly" = nhiều, "morph" = hình dạng.

### 3.1. Compile-Time Polymorphism (Method Overloading)

**Đặc điểm:**
- Cùng tên method, khác parameters
- Quyết định tại compile time
- Trong cùng một class

**Ví dụ:**
```java
public class Calculator {
    // Method 1: 2 parameters
    public int add(int a, int b) {
        return a + b;
    }
    
    // Method 2: 3 parameters
    public int add(int a, int b, int c) {
        return a + b + c;
    }
    
    // Method 3: double parameters
    public double add(double a, double b) {
        return a + b;
    }
}

// Sử dụng
Calculator calc = new Calculator();
System.out.println(calc.add(5, 3));        // 8 - gọi method 1
System.out.println(calc.add(5, 3, 2));     // 10 - gọi method 2
System.out.println(calc.add(5.5, 3.2));    // 8.7 - gọi method 3
```

**Ví dụ thực tế:**
```java
public class Printer {
    public void print(String text) {
        System.out.println("Text: " + text);
    }
    
    public void print(int number) {
        System.out.println("Number: " + number);
    }
    
    public void print(String text, int copies) {
        for (int i = 0; i < copies; i++) {
            System.out.println("Copy " + (i+1) + ": " + text);
        }
    }
}
```

---

### 3.2. Runtime Polymorphism (Method Overriding)

**Đặc điểm:**
- Subclass override method của superclass
- Quyết định tại runtime
- Cần có IS-A relationship (inheritance)

**Ví dụ:**
```java
// Superclass
class Animal {
    public void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

// Subclass 1
class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Dog barks: Woof! Woof!");
    }
}

// Subclass 2
class Cat extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Cat meows: Meow! Meow!");
    }
}

// Sử dụng - Runtime Polymorphism
Animal animal1 = new Dog();  // Upcasting
Animal animal2 = new Cat();

animal1.makeSound();  // Dog barks: Woof! Woof!
animal2.makeSound();  // Cat meows: Meow! Meow!

// Quyết định tại runtime
Animal animal3 = getAnimal();  // Không biết trước sẽ là Dog hay Cat
animal3.makeSound();           // Tùy vào object thực tế
```

**Ví dụ với Interface:**
```java
interface Shape {
    void draw();
    double getArea();
}

class Circle implements Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing Circle");
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle implements Shape {
    private double width, height;
    
    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing Rectangle");
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
}

// Sử dụng
Shape shape1 = new Circle(5);
Shape shape2 = new Rectangle(4, 6);

shape1.draw();  // Drawing Circle
shape2.draw();  // Drawing Rectangle

System.out.println("Area: " + shape1.getArea());  // 78.54
System.out.println("Area: " + shape2.getArea());  // 24.0
```

---

## 4. Inheritance (Kế Thừa)

### Khái Niệm
Class con (subclass) kế thừa properties và methods từ class cha (superclass). Tái sử dụng code.

### Đặc Điểm
- Sử dụng keyword `extends`
- IS-A relationship (Dog IS-A Animal)
- Java chỉ hỗ trợ single inheritance (1 class chỉ extends 1 class)
- Hỗ trợ multilevel inheritance (A → B → C)

### Code Example
```java
// Superclass (Parent)
class Animal {
    protected String name;
    protected int age;
    
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public void eat() {
        System.out.println(name + " is eating");
    }
    
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

// Subclass (Child)
class Dog extends Animal {
    private String breed;
    
    public Dog(String name, int age, String breed) {
        super(name, age);  // Gọi constructor của superclass
        this.breed = breed;
    }
    
    // Method riêng của Dog
    public void bark() {
        System.out.println(name + " is barking: Woof!");
    }
    
    // Override method của Animal
    @Override
    public void eat() {
        System.out.println(name + " the dog is eating dog food");
    }
}

// Sử dụng
Dog myDog = new Dog("Buddy", 3, "Golden Retriever");
myDog.eat();    // name the dog is eating dog food (overridden)
myDog.sleep();  // Buddy is sleeping (inherited)
myDog.bark();   // Buddy is barking: Woof! (own method)
```

### Multilevel Inheritance
```java
class Animal {
    public void eat() {
        System.out.println("Animal eats");
    }
}

class Mammal extends Animal {
    public void breathe() {
        System.out.println("Mammal breathes air");
    }
}

class Dog extends Mammal {
    public void bark() {
        System.out.println("Dog barks");
    }
}

// Dog có tất cả methods từ Animal và Mammal
Dog dog = new Dog();
dog.eat();      // từ Animal
dog.breathe();  // từ Mammal
dog.bark();     // của Dog
```

---

## 5. Association (Liên Kết)

### Khái Niệm
Mối quan hệ giữa 2 objects độc lập. Cả 2 có lifecycle riêng, không phụ thuộc nhau.

### Đặc Điểm
- Objects có thể tồn tại độc lập
- Có thể là one-to-one, one-to-many, many-to-many
- Không có ownership

### Code Example
```java
class Teacher {
    private String name;
    
    public Teacher(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}

class Student {
    private String name;
    
    public Student(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}

// Association - Teacher và Student độc lập
class School {
    public void assignTeacher(Teacher teacher, Student student) {
        System.out.println(teacher.getName() + " teaches " + student.getName());
    }
}

// Sử dụng
Teacher teacher = new Teacher("Mr. Smith");
Student student = new Student("John");

School school = new School();
school.assignTeacher(teacher, student);
// Output: Mr. Smith teaches John

// Teacher và Student vẫn tồn tại độc lập
// Xóa teacher không ảnh hưởng student và ngược lại
```

---

## 6. Aggregation (Tập Hợp)

### Khái Niệm
Dạng đặc biệt của Association. HAS-A relationship với ownership nhưng objects vẫn có lifecycle độc lập.

### Đặc Điểm
- HAS-A relationship (Department HAS-A Teacher)
- Có ownership nhưng không bắt buộc
- Objects có thể tồn tại độc lập
- Weak relationship

### Code Example
```java
class Teacher {
    private String name;
    
    public Teacher(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}

class Department {
    private String name;
    private List<Teacher> teachers;  // Department HAS-A Teachers
    
    public Department(String name) {
        this.name = name;
        this.teachers = new ArrayList<>();
    }
    
    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
    
    public void showTeachers() {
        System.out.println("Department: " + name);
        for (Teacher t : teachers) {
            System.out.println("- " + t.getName());
        }
    }
}

// Sử dụng
Teacher t1 = new Teacher("Mr. Smith");
Teacher t2 = new Teacher("Ms. Johnson");

Department dept = new Department("Computer Science");
dept.addTeacher(t1);
dept.addTeacher(t2);

dept.showTeachers();
// Output:
// Department: Computer Science
// - Mr. Smith
// - Ms. Johnson

// Nếu xóa department, teachers vẫn tồn tại
dept = null;
System.out.println(t1.getName());  // Mr. Smith vẫn còn
```

---

## 7. Composition (Kết Hợp)

### Khái Niệm
Dạng mạnh của Aggregation. HAS-A relationship nhưng object con KHÔNG THỂ tồn tại độc lập khi object cha bị xóa.

### Đặc Điểm
- HAS-A relationship (House HAS-A Room)
- Strong ownership
- Object con không thể tồn tại khi object cha bị xóa
- Strong relationship
- Composition > Inheritance (linh hoạt hơn)

### Code Example
```java
// Room không thể tồn tại mà không có House
class Room {
    private String name;
    private double area;
    
    public Room(String name, double area) {
        this.name = name;
        this.area = area;
    }
    
    public void display() {
        System.out.println("Room: " + name + ", Area: " + area + " sqm");
    }
}

class House {
    private String address;
    private List<Room> rooms;  // House HAS-A Rooms (Composition)
    
    public House(String address) {
        this.address = address;
        this.rooms = new ArrayList<>();
        
        // Rooms được tạo bên trong House
        rooms.add(new Room("Living Room", 30));
        rooms.add(new Room("Bedroom", 20));
        rooms.add(new Room("Kitchen", 15));
    }
    
    public void showRooms() {
        System.out.println("House at: " + address);
        for (Room room : rooms) {
            room.display();
        }
    }
}

// Sử dụng
House house = new House("123 Main St");
house.showRooms();
// Output:
// House at: 123 Main St
// Room: Living Room, Area: 30.0 sqm
// Room: Bedroom, Area: 20.0 sqm
// Room: Kitchen, Area: 15.0 sqm

// Khi xóa house, tất cả rooms cũng bị xóa
house = null;  // Rooms không thể tồn tại độc lập
```

### Ví Dụ Thực Tế Khác
```java
// Car HAS-A Engine (Composition)
class Engine {
    private int horsepower;
    
    public Engine(int horsepower) {
        this.horsepower = horsepower;
    }
    
    public void start() {
        System.out.println("Engine started: " + horsepower + " HP");
    }
}

class Car {
    private String model;
    private Engine engine;  // Composition
    
    public Car(String model, int horsepower) {
        this.model = model;
        this.engine = new Engine(horsepower);  // Engine tạo trong Car
    }
    
    public void start() {
        System.out.println("Starting " + model);
        engine.start();
    }
}

// Khi Car bị destroy, Engine cũng bị destroy
Car car = new Car("Tesla Model 3", 283);
car.start();
// Output:
// Starting Tesla Model 3
// Engine started: 283 HP
```

---

## So Sánh Các Khái Niệm

### Inheritance vs Composition

| Tiêu chí | Inheritance | Composition |
|----------|-------------|-------------|
| **Relationship** | IS-A | HAS-A |
| **Coupling** | Tight (chặt chẽ) | Loose (lỏng lẻo) |
| **Flexibility** | Thấp | Cao |
| **Code reuse** | Có | Có |
| **Ví dụ** | Dog IS-A Animal | Car HAS-A Engine |

**Best Practice:** Ưu tiên Composition hơn Inheritance (Composition over Inheritance)

---

### Association vs Aggregation vs Composition

| Tiêu chí | Association | Aggregation | Composition |
|----------|-------------|-------------|-------------|
| **Relationship** | Uses | HAS-A (weak) | HAS-A (strong) |
| **Ownership** | Không | Có (weak) | Có (strong) |
| **Lifecycle** | Độc lập | Độc lập | Phụ thuộc |
| **Ví dụ** | Teacher-Student | Department-Teacher | House-Room |

---

## Tổng Kết

### 4 Trụ Cột Chính của OOP
1. **Abstraction:** Ẩn chi tiết, hiện giao diện
2. **Encapsulation:** Đóng gói dữ liệu, kiểm soát truy cập
3. **Polymorphism:** Một object nhiều hình thái
4. **Inheritance:** Kế thừa để tái sử dụng code

### 3 Loại Relationship
5. **Association:** Quan hệ độc lập (Teacher-Student)
6. **Aggregation:** HAS-A yếu (Department-Teacher)
7. **Composition:** HAS-A mạnh (House-Room)

### Lợi Ích của OOP
- **Code reusability:** Tái sử dụng qua inheritance và composition
- **Modularity:** Code được tổ chức thành modules
- **Flexibility:** Dễ mở rộng và bảo trì
- **Security:** Bảo vệ dữ liệu qua encapsulation
- **Maintainability:** Dễ sửa lỗi và cập nhật

### Best Practices
1. Favor composition over inheritance
2. Program to interface, not implementation
3. Keep classes small and focused (Single Responsibility)
4. Use encapsulation to protect data
5. Apply polymorphism for flexibility
