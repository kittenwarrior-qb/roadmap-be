# Abstract Class vs Interface

## Khái Niệm

**Abstract Class** và **Interface** đều cung cấp abstraction nhưng có nhiều điểm khác biệt quan trọng.

---

## So Sánh Chi Tiết

| Tiêu Chí | Abstract Class | Interface |
|----------|----------------|-----------|
| **Keyword** | `abstract class` | `interface` |
| **Methods** | Abstract + Concrete | Abstract (mặc định) |
| **Variables** | Bất kỳ loại | public static final |
| **Constructor** | Có | Không |
| **Inheritance** | Single (extends) | Multiple (implements) |
| **Access Modifiers** | public, private, protected, default | public only |
| **Instantiate** | Không thể | Không thể |
| **Extend** | Extend class + implement interface | Extend interface only |
| **main() method** | Có thể có và chạy được | Không thể chạy |

---

## Ví Dụ So Sánh

### Abstract Class
```java
public abstract class Animal {
    private String name;  // Có thể có instance variables
    
    // Có constructor
    public Animal(String name) {
        this.name = name;
    }
    
    // Abstract method
    public abstract void makeSound();
    
    // Concrete method
    public void sleep() {
        System.out.println(name + " is sleeping");
    }
}

public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }
    
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}
```

### Interface
```java
public interface Drawable {
    // public static final (mặc định)
    String TYPE = "Drawable";
    
    // public abstract (mặc định)
    void draw();
}

public class Circle implements Drawable {
    @Override
    public void draw() {
        System.out.println("Drawing Circle");
    }
}
```

---

## Khi Nào Dùng Abstract Class?

### ✓ Dùng Khi:

1. **Có common implementation** cho subclasses
```java
public abstract class Vehicle {
    // Common implementation
    public void startEngine() {
        System.out.println("Engine started");
    }
    
    // Abstract method
    public abstract void drive();
}
```

2. **Cần constructor**
```java
public abstract class Person {
    private String name;
    
    public Person(String name) {
        this.name = name;
    }
}
```

3. **Cần non-public members**
```java
public abstract class BankAccount {
    private double balance;  // private
    protected String accountNumber;  // protected
}
```

4. **Share code giữa related classes**
```java
public abstract class Shape {
    protected String color;
    
    public void setColor(String color) {
        this.color = color;
    }
}
```

---

## Khi Nào Dùng Interface?

### ✓ Dùng Khi:

1. **Pure abstraction** - chỉ định nghĩa contract
```java
public interface Comparable<T> {
    int compareTo(T other);
}
```

2. **Multiple inheritance**
```java
public class MyClass implements Interface1, Interface2, Interface3 {
    // Implement nhiều interfaces
}
```

3. **Define contract** cho unrelated classes
```java
public interface Flyable {
    void fly();
}

// Cả Bird và Airplane đều có thể fly
public class Bird implements Flyable { }
public class Airplane implements Flyable { }
```

4. **Loose coupling**
```java
public interface PaymentProcessor {
    void processPayment(double amount);
}

// Dễ dàng thay đổi implementation
```

---

## Best Practice: Kết Hợp Cả Hai

### Pattern: Interface + Abstract Class

```java
// 1. Interface định nghĩa contract
public interface List<E> {
    void add(E element);
    E get(int index);
    int size();
    // ... nhiều methods khác
}

// 2. Abstract class cung cấp skeletal implementation
public abstract class AbstractList<E> implements List<E> {
    // Implement một số common methods
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    // Để subclass implement các methods còn lại
}

// 3. Concrete class chỉ implement methods cần thiết
public class ArrayList<E> extends AbstractList<E> {
    @Override
    public void add(E element) {
        // Implementation
    }
    
    @Override
    public E get(int index) {
        // Implementation
    }
    
    @Override
    public int size() {
        // Implementation
    }
}
```

### Lợi Ích:
- Interface: Flexibility (có thể implement nhiều)
- Abstract class: Code reuse (share common implementation)
- Subclass: Chỉ implement methods cần thiết

---

## Java 8+ Changes

### Interface Có Default Methods
```java
public interface MyInterface {
    // Abstract method
    void abstractMethod();
    
    // Default method - có implementation
    default void defaultMethod() {
        System.out.println("Default implementation");
    }
    
    // Static method
    static void staticMethod() {
        System.out.println("Static method");
    }
}
```

### Khoảng Cách Đã Thu Hẹp
Từ Java 8, interface có thể có method implementations → gần giống abstract class hơn.

**Nhưng vẫn khác:**
- Interface không có constructor
- Interface không có instance variables
- Interface hỗ trợ multiple inheritance

---

## Decision Tree

```
Cần multiple inheritance?
├─ Yes → Interface
└─ No
   └─ Có common implementation?
      ├─ Yes → Abstract Class
      └─ No → Interface
```

---

## Ví Dụ Thực Tế

### Scenario 1: Payment System
```java
// Interface cho contract
public interface PaymentProcessor {
    boolean processPayment(double amount);
    void refund(String transactionId);
}

// Abstract class cho common logic
public abstract class BasePaymentProcessor implements PaymentProcessor {
    protected void logTransaction(String message) {
        System.out.println("LOG: " + message);
    }
    
    protected boolean validateAmount(double amount) {
        return amount > 0;
    }
}

// Concrete implementations
public class CreditCardProcessor extends BasePaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        if (!validateAmount(amount)) return false;
        logTransaction("Processing credit card payment");
        // Credit card logic
        return true;
    }
    
    @Override
    public void refund(String transactionId) {
        logTransaction("Refunding credit card");
        // Refund logic
    }
}

public class PayPalProcessor extends BasePaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        if (!validateAmount(amount)) return false;
        logTransaction("Processing PayPal payment");
        // PayPal logic
        return true;
    }
    
    @Override
    public void refund(String transactionId) {
        logTransaction("Refunding PayPal");
        // Refund logic
    }
}
```

---

## Tổng Kết

### Abstract Class
- **Dùng cho:** Related classes với shared code
- **Ưu điểm:** Code reuse, có constructor, flexible access modifiers
- **Nhược điểm:** Single inheritance only

### Interface
- **Dùng cho:** Contract, unrelated classes, multiple inheritance
- **Ưu điểm:** Multiple inheritance, loose coupling, flexibility
- **Nhược điểm:** Không có constructor, không có instance variables

### Best Practice
1. **Bắt đầu với Interface** - định nghĩa contract
2. **Thêm Abstract Class** nếu cần share implementation
3. **Kết hợp cả hai** cho flexibility và code reuse
4. **Java 8+:** Interface với default methods là lựa chọn tốt

### Remember
- Interface = "CAN DO" (Flyable, Drawable, Comparable)
- Abstract Class = "IS A" (Animal, Vehicle, Shape)
- Kết hợp cả hai = Best of both worlds!
