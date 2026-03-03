# Java Interface

## Khái Niệm

**Interface** là contract (hợp đồng) định nghĩa methods mà class phải implement.
Cung cấp **absolute abstraction** - chỉ định nghĩa "what" không có "how".

---

## Ví Dụ Cơ Bản

### Định Nghĩa Interface
```java
public interface Shape {
    // Implicitly: public static final
    String LABEL = "Shape";
    
    // Implicitly: public abstract
    void draw();
    double getArea();
}
```

### Implementation
```java
public class Circle implements Shape {
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
    
    public double getRadius() {
        return radius;
    }
}
```

```java
public class Rectangle implements Shape {
    private double width;
    private double height;
    
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
```

### Sử Dụng
```java
public class ShapeTest {
    public static void main(String[] args) {
        // Programming for interfaces not implementation
        Shape shape = new Circle(10);
        shape.draw();
        System.out.println("Area=" + shape.getArea());
        
        // Dễ dàng switch implementation
        shape = new Rectangle(10, 10);
        shape.draw();
        System.out.println("Area=" + shape.getArea());
    }
}
```

**Output:**
```
Drawing Circle
Area=314.1592653589793
Drawing Rectangle
Area=100.0
```

---

## Đặc Điểm Quan Trọng

### 1. Không Thể Instantiate
```java
// ✗ Sai
Shape shape = new Shape();  // Compile error!

// ✓ Đúng
Shape shape = new Circle(10);
```

### 2. Attributes Mặc Định: public static final
```java
public interface MyInterface {
    // Tất cả đều là: public static final
    String NAME = "Test";
    int MAX_VALUE = 100;
}
```

### 3. Methods Mặc Định: public abstract
```java
public interface MyInterface {
    // Implicitly: public abstract
    void method1();
    int method2();
}
```

### 4. Không Có Constructor
```java
public interface MyInterface {
    // ✗ Không thể có constructor
    // MyInterface() { }  // Compile error!
}
```

### 5. Multiple Inheritance
```java
// ✓ Interface có thể extend nhiều interfaces
public interface Shape extends Cloneable, Serializable {
    void draw();
}

// ✓ Class có thể implement nhiều interfaces
public class Circle implements Shape, Comparable<Circle> {
    // Implementation
}
```

---

## Abstract Class Implement Interface

```java
public abstract class ShapeAbs implements Shape {
    @Override
    public double getArea() {
        return 0;  // Default implementation
    }
    
    // Không cần implement draw() - để subclass implement
}
```

---

## Lợi Ích

### 1. Contract cho Implementation
Interface đảm bảo tất cả implementation có đầy đủ methods.

### 2. Dễ Dàng Switch Implementation
```java
Shape shape = new Circle(10);
// Sau này có thể đổi sang Rectangle dễ dàng
shape = new Rectangle(5, 10);
```

### 3. Multiple Inheritance
```java
public class MyClass implements Interface1, Interface2, Interface3 {
    // Có thể implement nhiều interfaces
}
```

### 4. Loose Coupling
```java
// Code phụ thuộc vào interface, không phụ thuộc implementation
public void processShape(Shape shape) {
    shape.draw();
    System.out.println(shape.getArea());
}
```

---

## Nhược Điểm

### 1. Không Thể Thay Đổi Sau Khi Release
Nếu thêm method mới vào interface → tất cả implementation bị lỗi!

### 2. Không Truy Cập Methods Riêng của Implementation
```java
Shape shape = new Circle(10);
// shape.getRadius();  // ✗ Compile error!

// Phải typecast
Circle circle = (Circle) shape;
circle.getRadius();  // ✓ OK
```

---

## Best Practices

### 1. Code Theo Interface
```java
// ✓ Tốt
List<String> list = new ArrayList<>();

// ✗ Tránh
ArrayList<String> list = new ArrayList<>();
```

### 2. Luôn Dùng @Override
```java
public class Circle implements Shape {
    @Override  // ✓ Best practice
    public void draw() {
        System.out.println("Drawing Circle");
    }
}
```

### 3. Interface Làm Contract
```java
// Interface định nghĩa contract
public interface PaymentProcessor {
    boolean processPayment(double amount);
    void refund(String transactionId);
}

// Nhiều implementations khác nhau
public class CreditCardProcessor implements PaymentProcessor { }
public class PayPalProcessor implements PaymentProcessor { }
```

---

## Java 8+ Interface Changes

### Default Methods (Java 8)
```java
public interface MyInterface {
    // Default method có implementation
    default void defaultMethod() {
        System.out.println("Default implementation");
    }
    
    void abstractMethod();
}
```

### Static Methods (Java 8)
```java
public interface MyInterface {
    static void staticMethod() {
        System.out.println("Static method in interface");
    }
}

// Gọi: MyInterface.staticMethod();
```

---

## Tổng Kết

### Key Points
1. **interface** keyword để tạo interface
2. **Không thể instantiate** interface
3. **Absolute abstraction** - chỉ có method declarations
4. **Attributes:** public static final (mặc định)
5. **Methods:** public abstract (mặc định)
6. **Multiple inheritance** - class có thể implement nhiều interfaces
7. **implements** keyword để implement interface

### Remember
- Interface = Contract cho implementations
- Code theo interface, không theo implementation
- Dùng cho loose coupling và flexibility
- Java 8+: Có thể có default và static methods
