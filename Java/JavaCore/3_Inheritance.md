# Java Inheritance - Tóm Tắt

## Khái Niệm
Inheritance (Kế thừa) là cơ chế tạo hierarchy giữa các classes, cho phép class con kế thừa properties và methods từ class cha.

**Đặc điểm:**
- IS-A relationship (Dog IS-A Animal)
- Sử dụng keyword `extends`
- Transitive: Sedan extends Car, Car extends Vehicle → Sedan cũng extends Vehicle
- Mọi class đều extends `Object` class

---

## Ví Dụ Cơ Bản

### Superclass: Animal
```java
package com.journaldev.inheritance;

public class Animal {
    private boolean vegetarian;
    private String eats;
    private int noOfLegs;
    
    public Animal() {}
    
    public Animal(boolean veg, String food, int legs) {
        this.vegetarian = veg;
        this.eats = food;
        this.noOfLegs = legs;
    }
    
    // Getters and Setters
    public boolean isVegetarian() {
        return vegetarian;
    }
    
    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }
    
    public String getEats() {
        return eats;
    }
    
    public void setEats(String eats) {
        this.eats = eats;
    }
    
    public int getNoOfLegs() {
        return noOfLegs;
    }
    
    public void setNoOfLegs(int noOfLegs) {
        this.noOfLegs = noOfLegs;
    }
}
```

### Subclass: Cat
```java
package com.journaldev.inheritance;

public class Cat extends Animal {  // extends keyword
    private String color;
    
    public Cat(boolean veg, String food, int legs) {
        super(veg, food, legs);  // Call superclass constructor
        this.color = "White";
    }
    
    public Cat(boolean veg, String food, int legs, String color) {
        super(veg, food, legs);
        this.color = color;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
}
```

### Test Program
```java
package com.journaldev.inheritance;

public class AnimalInheritanceTest {
    public static void main(String[] args) {
        Cat cat = new Cat(false, "milk", 4, "black");
        
        System.out.println("Cat is Vegetarian? " + cat.isVegetarian());
        System.out.println("Cat eats " + cat.getEats());
        System.out.println("Cat has " + cat.getNoOfLegs() + " legs.");
        System.out.println("Cat color is " + cat.getColor());
    }
}
```

**Output:**
```
Cat is Vegetarian? false
Cat eats milk
Cat has 4 legs.
Cat color is black
```

---

## Ví Dụ Thực Tế 1: Vehicle Hierarchy

```java
// Superclass
class Vehicle {
    protected String brand;
    protected int year;
    
    public Vehicle(String brand, int year) {
        this.brand = brand;
        this.year = year;
    }
    
    public void start() {
        System.out.println(brand + " is starting...");
    }
    
    public void stop() {
        System.out.println(brand + " is stopping...");
    }
}

// Subclass 1
class Car extends Vehicle {
    private int doors;
    
    public Car(String brand, int year, int doors) {
        super(brand, year);
        this.doors = doors;
    }
    
    @Override
    public void start() {
        System.out.println(brand + " car with " + doors + " doors is starting...");
    }
    
    public void honk() {
        System.out.println("Beep beep!");
    }
}

// Subclass 2
class Motorcycle extends Vehicle {
    private boolean hasSidecar;
    
    public Motorcycle(String brand, int year, boolean hasSidecar) {
        super(brand, year);
        this.hasSidecar = hasSidecar;
    }
    
    @Override
    public void start() {
        System.out.println(brand + " motorcycle is starting...");
    }
}

// Sử dụng
public class TestVehicle {
    public static void main(String[] args) {
        Car car = new Car("Toyota", 2023, 4);
        car.start();  // Toyota car with 4 doors is starting...
        car.honk();   // Beep beep!
        
        Motorcycle bike = new Motorcycle("Harley", 2022, false);
        bike.start(); // Harley motorcycle is starting...
    }
}
```

---

## Ví Dụ Thực Tế 2: Employee System

```java
// Base class
class Employee {
    protected String name;
    protected int id;
    protected double baseSalary;
    
    public Employee(String name, int id, double baseSalary) {
        this.name = name;
        this.id = id;
        this.baseSalary = baseSalary;
    }
    
    public double calculateSalary() {
        return baseSalary;
    }
    
    public void displayInfo() {
        System.out.println("ID: " + id + ", Name: " + name);
    }
}

// Manager extends Employee
class Manager extends Employee {
    private double bonus;
    
    public Manager(String name, int id, double baseSalary, double bonus) {
        super(name, id, baseSalary);
        this.bonus = bonus;
    }
    
    @Override
    public double calculateSalary() {
        return baseSalary + bonus;
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();  // Call parent method
        System.out.println("Role: Manager, Salary: " + calculateSalary());
    }
}

// Developer extends Employee
class Developer extends Employee {
    private String programmingLanguage;
    
    public Developer(String name, int id, double baseSalary, String language) {
        super(name, id, baseSalary);
        this.programmingLanguage = language;
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Role: Developer, Language: " + programmingLanguage);
    }
}

// Sử dụng
public class TestEmployee {
    public static void main(String[] args) {
        Manager manager = new Manager("Alice", 101, 5000, 2000);
        manager.displayInfo();
        // Output:
        // ID: 101, Name: Alice
        // Role: Manager, Salary: 7000.0
        
        Developer dev = new Developer("Bob", 102, 4000, "Java");
        dev.displayInfo();
        // Output:
        // ID: 102, Name: Bob
        // Role: Developer, Language: Java
    }
}
```

---

## Multilevel Inheritance

```java
// Level 1
class Animal {
    public void eat() {
        System.out.println("Animal eats");
    }
}

// Level 2
class Mammal extends Animal {
    public void breathe() {
        System.out.println("Mammal breathes air");
    }
}

// Level 3
class Dog extends Mammal {
    public void bark() {
        System.out.println("Dog barks");
    }
}

// Dog kế thừa từ cả Mammal và Animal
public class TestMultilevel {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.eat();      // từ Animal
        dog.breathe();  // từ Mammal
        dog.bark();     // của Dog
        
        // Output:
        // Animal eats
        // Mammal breathes air
        // Dog barks
    }
}
```

---

## Các Điểm Quan Trọng

### 1. Code Reuse
```java
class Shape {
    protected String color;
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getColor() {
        return color;
    }
}

class Circle extends Shape {
    private double radius;
    
    // Reuse setColor() và getColor() từ Shape
    public double getArea() {
        return Math.PI * radius * radius;
    }
}
```

### 2. Private Members Không Truy Cập Trực Tiếp
```java
class Parent {
    private int privateVar = 10;
    protected int protectedVar = 20;
    
    public int getPrivateVar() {
        return privateVar;
    }
}

class Child extends Parent {
    public void display() {
        // System.out.println(privateVar);  // ERROR!
        System.out.println(protectedVar);   // OK
        System.out.println(getPrivateVar()); // OK - qua getter
    }
}
```

### 3. Constructor Chaining
```java
class Parent {
    public Parent() {
        System.out.println("Parent constructor");
    }
    
    public Parent(String name) {
        System.out.println("Parent constructor: " + name);
    }
}

class Child extends Parent {
    public Child() {
        super();  // Gọi Parent()
        System.out.println("Child constructor");
    }
    
    public Child(String name) {
        super(name);  // Gọi Parent(String)
        System.out.println("Child constructor: " + name);
    }
}

// Test
Child c = new Child("Test");
// Output:
// Parent constructor: Test
// Child constructor: Test
```

### 4. Upcasting và Downcasting

**Upcasting (Tự động):**
```java
Cat cat = new Cat(false, "milk", 4, "black");
Animal animal = cat;  // Upcasting - tự động
animal.eat();  // OK - method của Animal
// animal.getColor();  // ERROR - Animal không có method này
```

**Downcasting (Phải ép kiểu):**
```java
Animal animal = new Cat(false, "milk", 4, "black");
Cat cat = (Cat) animal;  // Downcasting - phải ép kiểu
cat.getColor();  // OK - giờ có thể dùng method của Cat

// Nguy hiểm nếu sai type
Animal animal2 = new Dog();
Cat cat2 = (Cat) animal2;  // ClassCastException at runtime!
```

### 5. instanceof Operator
```java
Cat cat = new Cat(false, "milk", 4, "black");
Dog dog = new Dog(false, "meat", 4);
Animal animal = cat;

// Kiểm tra type
boolean result1 = cat instanceof Cat;      // true
boolean result2 = cat instanceof Animal;   // true (Cat IS-A Animal)
boolean result3 = animal instanceof Cat;   // true (runtime type)
boolean result4 = animal instanceof Dog;   // false

// Sử dụng an toàn
if (animal instanceof Cat) {
    Cat c = (Cat) animal;  // Safe downcasting
    System.out.println(c.getColor());
}
```

### 6. Method Overriding với @Override
```java
class Animal {
    public void makeSound() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {
    @Override  // Annotation - best practice
    public void makeSound() {
        System.out.println("Bark!");
    }
    
    // Nếu sai tên method, compiler sẽ báo lỗi
    // @Override
    // public void makeSounds() {  // ERROR - không match method nào
    // }
}
```

### 7. super Keyword
```java
class Parent {
    protected String name = "Parent";
    
    public void display() {
        System.out.println("Parent display");
    }
}

class Child extends Parent {
    private String name = "Child";
    
    public void display() {
        super.display();  // Gọi method của parent
        System.out.println("Child display");
    }
    
    public void showNames() {
        System.out.println(name);        // Child
        System.out.println(super.name);  // Parent
    }
}
```

---

## Access Modifiers trong Inheritance

| Modifier | Same Class | Same Package | Subclass | Other Package |
|----------|------------|--------------|----------|---------------|
| private | ✓ | ✗ | ✗ | ✗ |
| default | ✓ | ✓ | ✗ | ✗ |
| protected | ✓ | ✓ | ✓ | ✗ |
| public | ✓ | ✓ | ✓ | ✓ |

```java
// Package: com.example
class Parent {
    private int privateVar = 1;
    int defaultVar = 2;
    protected int protectedVar = 3;
    public int publicVar = 4;
}

// Package: com.example
class Child1 extends Parent {
    public void test() {
        // System.out.println(privateVar);    // ERROR
        System.out.println(defaultVar);       // OK - same package
        System.out.println(protectedVar);     // OK
        System.out.println(publicVar);        // OK
    }
}

// Package: com.other
class Child2 extends Parent {
    public void test() {
        // System.out.println(privateVar);    // ERROR
        // System.out.println(defaultVar);    // ERROR - different package
        System.out.println(protectedVar);     // OK - subclass
        System.out.println(publicVar);        // OK
    }
}
```

---

## Hạn Chế của Inheritance

### 1. Java Không Hỗ Trợ Multiple Inheritance
```java
class A {
    public void methodA() {}
}

class B {
    public void methodB() {}
}

// ERROR - không thể extends nhiều class
// class C extends A, B {  // Compile error!
// }

// Giải pháp: Dùng interfaces
interface InterfaceA {
    void methodA();
}

interface InterfaceB {
    void methodB();
}

class C implements InterfaceA, InterfaceB {  // OK
    public void methodA() {}
    public void methodB() {}
}
```

### 2. Final Classes Không Thể Extends
```java
final class FinalClass {
    // ...
}

// ERROR
// class SubClass extends FinalClass {  // Compile error!
// }
```

### 3. ClassCastException Examples
```java
// Case 1: Wrong downcasting
Dog dog = new Dog();
Animal animal = dog;
Cat cat = (Cat) animal;  // ClassCastException! animal là Dog, không phải Cat

// Case 2: Casting non-subclass object
Animal animal2 = new Animal();
Cat cat2 = (Cat) animal2;  // ClassCastException! animal2 không phải Cat

// Safe way: Dùng instanceof
if (animal instanceof Cat) {
    Cat cat3 = (Cat) animal;  // Safe
} else {
    System.out.println("Cannot cast to Cat");
}
```

---

## Best Practices

### 1. Luôn Dùng @Override
```java
class Parent {
    public void process() {
        System.out.println("Processing...");
    }
}

class Child extends Parent {
    @Override  // Best practice - compiler sẽ check
    public void process() {
        System.out.println("Child processing...");
    }
}
```

### 2. Dùng Abstract Class Cho Base Class
```java
// Nếu không muốn instantiate base class
abstract class Shape {
    protected String color;
    
    public abstract double getArea();  // Force subclass implement
    
    public void setColor(String color) {
        this.color = color;
    }
}

class Circle extends Shape {
    private double radius;
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

// Shape shape = new Shape();  // ERROR - cannot instantiate abstract class
Circle circle = new Circle();  // OK
```

### 3. Favor Composition Over Inheritance
```java
// Thay vì inheritance phức tạp
class FlyingCar extends Car {  // Tight coupling
    // ...
}

// Dùng composition
class Car {
    private Engine engine;
    private FlyingCapability flyingCapability;  // Optional
    
    public void enableFlying() {
        this.flyingCapability = new FlyingCapability();
    }
}
```

---

## Ví Dụ Tổng Hợp: Shape Hierarchy

```java
// Abstract base class
abstract class Shape {
    protected String color;
    
    public Shape(String color) {
        this.color = color;
    }
    
    public abstract double getArea();
    public abstract double getPerimeter();
    
    public void displayInfo() {
        System.out.println("Color: " + color);
        System.out.println("Area: " + getArea());
        System.out.println("Perimeter: " + getPerimeter());
    }
}

// Circle
class Circle extends Shape {
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

// Rectangle
class Rectangle extends Shape {
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

// Test
public class TestShapes {
    public static void main(String[] args) {
        Shape circle = new Circle("Red", 5);
        Shape rectangle = new Rectangle("Blue", 4, 6);
        
        circle.displayInfo();
        System.out.println();
        rectangle.displayInfo();
        
        // Polymorphism
        Shape[] shapes = {circle, rectangle};
        for (Shape shape : shapes) {
            System.out.println("Area: " + shape.getArea());
        }
    }
}
```

---

## Tổng Kết

### Inheritance Là Gì?
- IS-A relationship giữa classes
- Subclass kế thừa properties và methods từ superclass
- Sử dụng keyword `extends`
- Transitive: A → B → C

### Lợi Ích
1. **Code Reuse:** Tái sử dụng code từ superclass
2. **Polymorphism:** Hỗ trợ runtime polymorphism
3. **Extensibility:** Dễ mở rộng functionality
4. **Maintainability:** Thay đổi ở superclass ảnh hưởng tất cả subclasses

### Hạn Chế
1. **Tight Coupling:** Subclass phụ thuộc vào superclass
2. **No Multiple Inheritance:** Chỉ extends 1 class
3. **Fragile Base Class:** Thay đổi superclass có thể break subclasses

### Khi Nào Dùng?
- ✓ Có mối quan hệ IS-A rõ ràng
- ✓ Cần code reuse và polymorphism
- ✓ Hierarchy có ý nghĩa logic
- ✗ Chỉ để reuse code (dùng composition)

### Key Points
- Private members không truy cập trực tiếp
- Constructor không được kế thừa
- Dùng `super` để gọi superclass
- Dùng `@Override` cho method overriding
- Dùng `instanceof` để check type
- Final classes không thể extends
- Favor composition over inheritance

### Remember
**"Inheritance represents IS-A relationship. Use it wisely!"**
