# Java Abstract Class

## Khái Niệm

**Abstract Class** là class không thể tạo object trực tiếp, dùng làm template cho các subclass.

---

## Ví Dụ Cơ Bản

### Abstract Class
```java
public abstract class Person {
    private String name;
    private String gender;
    
    public Person(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }
    
    // Abstract method - không có body
    public abstract void work();
    
    // Concrete method - có implementation
    public void changeName(String newName) {
        this.name = newName;
    }
    
    @Override
    public String toString() {
        return "Name=" + this.name + "::Gender=" + this.gender;
    }
}
```

### Concrete Class (Subclass)
```java
public class Employee extends Person {
    private int empId;
    
    public Employee(String name, String gender, int id) {
        super(name, gender);
        this.empId = id;
    }
    
    // Phải implement abstract method
    @Override
    public void work() {
        if (empId == 0) {
            System.out.println("Not working");
        } else {
            System.out.println("Working as employee!!");
        }
    }
}
```

### Sử Dụng
```java
public static void main(String[] args) {
    // Coding in terms of abstract class
    Person student = new Employee("Dove", "Female", 0);
    Person employee = new Employee("Pankaj", "Male", 123);
    
    student.work();    // Not working
    employee.work();   // Working as employee!!
    
    // Sử dụng method từ abstract class
    employee.changeName("Pankaj Kumar");
    System.out.println(employee.toString());
    // Output: Name=Pankaj Kumar::Gender=Male
}
```

---

## Đặc Điểm Quan Trọng

### 1. Không Thể Tạo Object
```java
// ✗ Sai: Không thể instantiate abstract class
Person person = new Person("John", "Male");  // Compile error!

// ✓ Đúng: Tạo object từ subclass
Person person = new Employee("John", "Male", 123);
```

### 2. Abstract Method
```java
public abstract class Animal {
    // Abstract method - không có body
    public abstract void makeSound();
    
    // Concrete method - có body
    public void sleep() {
        System.out.println("Sleeping...");
    }
}
```

### 3. Subclass Phải Implement
```java
// ✓ Đúng: Implement tất cả abstract methods
public class Dog extends Animal {
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}

// ✓ Hoặc: Subclass cũng là abstract
public abstract class Pet extends Animal {
    // Không cần implement makeSound()
}
```

---

## Quy Tắc

### 1. Từ Khóa abstract
```java
// Khai báo abstract class
public abstract class Shape {
    // Khai báo abstract method
    public abstract double getArea();
}
```

### 2. Abstract Class Không Bắt Buộc Có Abstract Method
```java
// ✓ Hợp lệ: Abstract class không có abstract method
public abstract class BaseClass {
    public void display() {
        System.out.println("Base class");
    }
}
```

### 3. Class Có Abstract Method Phải Là Abstract
```java
// ✗ Sai: Class thường không thể có abstract method
public class MyClass {
    public abstract void myMethod();  // Compile error!
}

// ✓ Đúng: Phải khai báo class là abstract
public abstract class MyClass {
    public abstract void myMethod();
}
```

---

## Ví Dụ Thực Tế

### Example 1: Shape Hierarchy
```java
public abstract class Shape {
    private String color;
    
    public Shape(String color) {
        this.color = color;
    }
    
    // Abstract methods
    public abstract double getArea();
    public abstract double getPerimeter();
    
    // Concrete method
    public String getColor() {
        return color;
    }
}

public class Circle extends Shape {
    private double radius;
    
    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }
}

public class Rectangle extends Shape {
    private double width;
    private double height;
    
    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
    
    @Override
    public double getPerimeter() {
        return 2 * (width + height);
    }
}
```

### Sử Dụng
```java
Shape circle = new Circle("Red", 5.0);
Shape rectangle = new Rectangle("Blue", 4.0, 6.0);

System.out.println("Circle area: " + circle.getArea());
System.out.println("Rectangle area: " + rectangle.getArea());
```

---

## Abstract Class vs Interface

| | Abstract Class | Interface |
|---|----------------|-----------|
| **Từ khóa** | `abstract class` | `interface` |
| **Methods** | Abstract + Concrete | Abstract (default: abstract) |
| **Variables** | Bất kỳ | public static final |
| **Constructor** | Có | Không |
| **Inheritance** | Single (extends) | Multiple (implements) |
| **Khi nào dùng** | Có common code | Pure abstraction |

---

## Khi Nào Dùng Abstract Class?

### ✓ Dùng Khi:
1. Có common implementation cho subclasses
2. Cần constructor
3. Cần non-public members
4. Muốn share code giữa related classes

### ✗ Không Dùng Khi:
1. Không có implementation nào (dùng interface)
2. Cần multiple inheritance (dùng interface)
3. Chỉ định nghĩa contract (dùng interface)

---

## Lưu Ý Đặc Biệt

### 1. Abstract Class Có Thể Có main()
```java
public abstract class Test {
    public static void main(String[] args) {
        System.out.println("Can run abstract class!");
    }
}
// ✓ Có thể chạy: java Test
```

### 2. Abstract Class Có Thể Implement Interface
```java
public interface Drawable {
    void draw();
}

public abstract class Shape implements Drawable {
    // Không cần implement draw()
    // Để subclass implement
}
```

### 3. Luôn Dùng @Override
```java
public class Employee extends Person {
    @Override  // ✓ Best practice
    public void work() {
        System.out.println("Working...");
    }
}
```

---

## Tổng Kết

### Key Points
1. **abstract** keyword để tạo abstract class/method
2. **Không thể instantiate** abstract class
3. **Abstract method** không có body
4. **Subclass phải implement** tất cả abstract methods
5. **Có thể có** concrete methods và variables
6. **Dùng cho** common implementation và inheritance

### Remember
- Abstract class = Template cho subclasses
- Kết hợp abstract methods (contract) + concrete methods (shared code)
- Dùng khi có IS-A relationship và shared implementation
