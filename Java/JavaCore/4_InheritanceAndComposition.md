# Inheritance và Composition trong Java - Tóm Tắt

## Giới Thiệu
Inheritance (Kế thừa) là nguyên lý cơ bản của OOP, cho phép class con kế thừa fields và methods từ class cha, thúc đẩy code reuse và maintainability.

---

## Các Loại Inheritance trong Java

### 1. Single Inheritance (Kế Thừa Đơn)
Subclass kế thừa từ 1 parent class duy nhất.

```java
// Parent class
class Animal {
    void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

// Child class
class Dog extends Animal {
    void bark() {
        System.out.println("Dog barks");
    }
}

// Sử dụng
public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.makeSound(); // Inherited method
        dog.bark();      // Own method
    }
}
```

---

### 2. Multilevel Inheritance (Kế Thừa Nhiều Cấp)
Subclass kế thừa từ subclass khác, tạo thành hierarchy.

```java
// Grandparent class
class Animal {
    void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

// Parent class
class Mammal extends Animal {
    void eat() {
        System.out.println("Mammal eats");
    }
}

// Child class
class Dog extends Mammal {
    void bark() {
        System.out.println("Dog barks");
    }
}

// Dog có tất cả methods từ Animal và Mammal
public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.makeSound(); // từ Animal
        dog.eat();       // từ Mammal
        dog.bark();      // của Dog
    }
}
```

---

### 3. Hierarchical Inheritance (Kế Thừa Phân Cấp)
Nhiều classes kế thừa từ cùng 1 parent class.

```java
// Parent class
class Animal {
    void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

// Child class 1
class Dog extends Animal {
    void bark() {
        System.out.println("Dog barks");
    }
}

// Child class 2
class Cat extends Animal {
    void meow() {
        System.out.println("Cat meows");
    }
}

// Sử dụng
public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        Cat cat = new Cat();
        
        dog.makeSound(); // Animal makes a sound
        dog.bark();      // Dog barks
        
        cat.makeSound(); // Animal makes a sound
        cat.meow();      // Cat meows
    }
}
```

---

### 4. Hybrid Inheritance (Kế Thừa Lai)
Kết hợp 2+ loại inheritance. Java không hỗ trợ trực tiếp nhưng có thể đạt được qua interfaces.

```java
// Interface 1
interface Flyable {
    void fly();
}

// Interface 2
interface Walkable {
    void walk();
}

// Parent class
class Animal {
    void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

// Child class: extends class + implements interfaces
class Bird extends Animal implements Flyable, Walkable {
    @Override
    public void fly() {
        System.out.println("Bird flies");
    }
    
    @Override
    public void walk() {
        System.out.println("Bird walks");
    }
}

// Sử dụng
public class Main {
    public static void main(String[] args) {
        Bird bird = new Bird();
        bird.makeSound(); // từ Animal
        bird.fly();       // từ Flyable
        bird.walk();      // từ Walkable
    }
}
```

---

## Multiple Inheritance - Tại Sao Java Không Hỗ Trợ?

### Diamond Problem
```java
// Giả sử Java hỗ trợ multiple inheritance (KHÔNG ĐƯỢC)
class A {
    void display() {
        System.out.println("A");
    }
}

class B extends A {
    void display() {
        System.out.println("B");
    }
}

class C extends A {
    void display() {
        System.out.println("C");
    }
}

// Diamond problem
class D extends B, C {  // ERROR - Java không cho phép
    // display() nên gọi từ B hay C?
}
```

### Giải Pháp: Dùng Interfaces
```java
interface InterfaceB {
    default void display() {
        System.out.println("Interface B");
    }
}

interface InterfaceC {
    default void display() {
        System.out.println("Interface C");
    }
}

class D implements InterfaceB, InterfaceC {
    // Phải override để giải quyết conflict
    @Override
    public void display() {
        InterfaceB.super.display(); // Chọn implementation
        // Hoặc
        InterfaceC.super.display();
        // Hoặc tự implement
    }
}
```

---

## Method Overriding

```java
// Parent class
class Animal {
    void makeSound() {
        System.out.println("Animal makes a sound");
    }
}

// Child class
class Dog extends Animal {
    @Override  // Best practice - luôn dùng annotation
    void makeSound() {
        System.out.println("Dog barks: Woof!");
    }
}

// Sử dụng
public class Main {
    public static void main(String[] args) {
        Animal animal = new Animal();
        animal.makeSound(); // Animal makes a sound
        
        Dog dog = new Dog();
        dog.makeSound();    // Dog barks: Woof!
        
        // Polymorphism
        Animal polymorphicDog = new Dog();
        polymorphicDog.makeSound(); // Dog barks: Woof!
    }
}
```

---

## Inheritance vs Composition

### So Sánh

| Tiêu chí | Inheritance | Composition |
|----------|-------------|-------------|
| **Relationship** | IS-A | HAS-A |
| **Flexibility** | Thấp (tight coupling) | Cao (loose coupling) |
| **Code Reuse** | Có | Có |
| **Complexity** | Có thể phức tạp | Đơn giản hơn |
| **Keyword** | extends | instance variable |

### Ví Dụ So Sánh

**Inheritance (IS-A):**
```java
class Vehicle {
    void start() {
        System.out.println("Vehicle starting...");
    }
}

class Car extends Vehicle {  // Car IS-A Vehicle
    void drive() {
        System.out.println("Car driving...");
    }
}
```

**Composition (HAS-A):**
```java
class Engine {
    void start() {
        System.out.println("Engine starting...");
    }
}

class Car {
    private Engine engine;  // Car HAS-A Engine
    
    public Car() {
        this.engine = new Engine();
    }
    
    void start() {
        engine.start();  // Delegate
    }
}
```

---

## Ví Dụ Thực Tế: Khi Nào Dùng Gì?

### Dùng Inheritance
```java
// IS-A relationship rõ ràng
abstract class Employee {
    protected String name;
    protected double salary;
    
    public abstract double calculateBonus();
}

class Manager extends Employee {  // Manager IS-A Employee
    @Override
    public double calculateBonus() {
        return salary * 0.2;
    }
}

class Developer extends Employee {  // Developer IS-A Employee
    @Override
    public double calculateBonus() {
        return salary * 0.15;
    }
}
```

### Dùng Composition
```java
// HAS-A relationship
class Address {
    private String street;
    private String city;
    
    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }
}

class Person {
    private String name;
    private Address address;  // Person HAS-A Address
    
    public Person(String name, Address address) {
        this.name = name;
        this.address = address;
    }
}
```

---

## Performance Considerations

### 1. Memory Consumption
```java
class Parent {
    private int[] data = new int[1000];  // 4KB
}

class Child extends Parent {
    private int[] moreData = new int[1000];  // 4KB thêm
}

// Mỗi Child instance = 8KB (Parent + Child data)
Child child = new Child();  // 8KB memory
```

### 2. Method Resolution Overhead
```java
// Deep inheritance tree
class A { void method() {} }
class B extends A { void method() {} }
class C extends B { void method() {} }
class D extends C { void method() {} }

// JVM phải traverse hierarchy để tìm method
D d = new D();
d.method();  // Slight overhead trong method lookup
```

### 3. Deep Inheritance Trees
```java
// TỐT: Shallow hierarchy
class Animal { }
class Dog extends Animal { }

// XẤU: Deep hierarchy (khó maintain)
class A { }
class B extends A { }
class C extends B { }
class D extends C { }
class E extends D { }
class F extends E { }  // Too deep!
```

---

## Khi Nào Tránh Inheritance?

### 1. Deep Hierarchy
```java
// XẤU
class Vehicle { }
class LandVehicle extends Vehicle { }
class MotorVehicle extends LandVehicle { }
class Car extends MotorVehicle { }
class Sedan extends Car { }
class LuxurySedan extends Sedan { }  // Too deep!

// TÔT: Dùng composition
class Car {
    private Engine engine;
    private Transmission transmission;
    private LuxuryFeatures luxuryFeatures;
}
```

### 2. Không Có IS-A Relationship
```java
// SAI: Person không phải IS-A Address
class Person extends Address {  // WRONG!
    private String name;
}

// ĐÚNG: Person HAS-A Address
class Person {
    private String name;
    private Address address;  // Composition
}
```

### 3. Composition Linh Hoạt Hơn
```java
// Inheritance: Rigid
class FlyingCar extends Car {  // Stuck with this design
}

// Composition: Flexible
class Car {
    private FlyingCapability flyingCapability;  // Optional
    
    public void enableFlying() {
        this.flyingCapability = new FlyingCapability();
    }
    
    public void disableFlying() {
        this.flyingCapability = null;
    }
}
```

---

## Best Practices

### 1. Favor Composition Over Inheritance
```java
// Thay vì
class Stack extends ArrayList {  // BAD - exposes all ArrayList methods
}

// Dùng
class Stack {
    private ArrayList list = new ArrayList();  // GOOD - encapsulation
    
    public void push(Object item) {
        list.add(item);
    }
    
    public Object pop() {
        return list.remove(list.size() - 1);
    }
}
```

### 2. Luôn Dùng @Override
```java
class Parent {
    void process() {
        System.out.println("Processing...");
    }
}

class Child extends Parent {
    @Override  // GOOD - compiler check
    void process() {
        System.out.println("Child processing...");
    }
    
    // @Override
    // void proces() {  // ERROR - typo detected!
    // }
}
```

### 3. Keep Hierarchy Shallow
```java
// GOOD: 2-3 levels max
class Shape { }
class Circle extends Shape { }

// AVOID: Too many levels
class A { }
class B extends A { }
class C extends B { }
class D extends C { }
class E extends D { }  // Too deep!
```

### 4. Dùng Abstract Classes Cho Base
```java
// Prevent instantiation of base class
abstract class Shape {
    protected String color;
    
    public abstract double getArea();  // Force implementation
}

class Circle extends Shape {
    private double radius;
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

// Shape shape = new Shape();  // ERROR - good!
```

---

## FAQ - Câu Hỏi Thường Gặp

### 1. Inheritance là gì?
Cơ chế cho phép subclass kế thừa properties và behaviors từ parent class.

### 2. Java hỗ trợ những loại inheritance nào?
- Single Inheritance
- Multilevel Inheritance
- Hierarchical Inheritance
- Hybrid Inheritance (qua interfaces)
- KHÔNG hỗ trợ Multiple Inheritance (classes)

### 3. extends keyword hoạt động thế nào?
```java
class Child extends Parent {
    // Child có tất cả public/protected members của Parent
}
```

### 4. Khác biệt Inheritance vs Composition?
- **Inheritance:** IS-A, ít linh hoạt, tight coupling
- **Composition:** HAS-A, linh hoạt hơn, loose coupling

### 5. Subclass có thể override method không?
Có, dùng `@Override` annotation.

```java
class Parent {
    void method() { }
}

class Child extends Parent {
    @Override
    void method() { }  // Override
}
```

### 6. Tại sao Java không hỗ trợ multiple inheritance?
Để tránh Diamond Problem và giữ ngôn ngữ đơn giản. Có thể đạt được qua interfaces.

### 7. Khi nào tránh inheritance?
- Deep hierarchy phức tạp
- Composition phù hợp hơn
- Không có IS-A relationship rõ ràng

---

## Tổng Kết

### Key Points
1. **Inheritance:** IS-A relationship, code reuse
2. **4 loại:** Single, Multilevel, Hierarchical, Hybrid
3. **Java không hỗ trợ:** Multiple inheritance (classes)
4. **Composition:** Thường tốt hơn inheritance
5. **Best practice:** Shallow hierarchy, @Override, abstract base

### Khi Nào Dùng Gì?
- **Inheritance:** IS-A relationship rõ ràng, polymorphism
- **Composition:** HAS-A relationship, flexibility, loose coupling

### Remember
**"Favor Composition Over Inheritance"** - Một trong những nguyên tắc quan trọng nhất trong OOP!

### Performance Tips
- Tránh deep inheritance trees
- Cân nhắc memory consumption
- Composition thường perform tốt hơn
- Profile code để tối ưu

### Final Advice
Hiểu rõ khi nào dùng inheritance và khi nào dùng composition là chìa khóa để thiết kế ứng dụng Java hiệu quả, dễ maintain và scalable.
