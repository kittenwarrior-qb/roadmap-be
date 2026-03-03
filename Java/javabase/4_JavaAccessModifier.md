# Access Modifiers trong Java

## Khái niệm
Access modifiers kiểm soát phạm vi truy cập của class, method và biến. Java có 4 loại:
- `public` - truy cập từ mọi nơi
- `protected` - truy cập trong package và subclass
- `default` (không khai báo) - chỉ trong package
- `private` - chỉ trong class

## Mức độ truy cập
```
private < default < protected < public
(Hạn chế nhất)              (Rộng nhất)
```

## Access Modifiers với Class

### Public Class
```java
public class TestA {
    // Truy cập từ mọi nơi
}
```
- Chỉ có 1 public class trong 1 file
- Tên file phải trùng tên public class

### Default Class
```java
class TestB {
    // Chỉ truy cập trong cùng package
}
```

## Access Modifiers với Class Members

### 1. Public
Truy cập từ mọi nơi:

```java
public class Example {
    public int value;
    
    public void display() {
        System.out.println(value);
    }
}
```

### 2. Private
Chỉ truy cập trong class:

```java
public class Example {
    private int value;
    
    private void calculate() {
        // Chỉ dùng trong class này
    }
    
    // Getter/Setter để truy cập từ bên ngoài
    public int getValue() {
        return value;
    }
}
```

### 3. Protected
Truy cập trong package và subclass:

```java
public class Parent {
    protected void method() {
        // Truy cập được từ subclass
    }
}

public class Child extends Parent {
    public void test() {
        method();  // OK
    }
}
```

### 4. Default (Package-Private)
Chỉ truy cập trong cùng package:

```java
class Example {
    int value;  // default access
    
    void method() {
        // Chỉ truy cập trong package
    }
}
```

## Bảng so sánh

| Modifier | Cùng Class | Cùng Package | Subclass | Mọi nơi |
|----------|------------|--------------|----------|---------|
| `private` | ✓ | ✗ | ✗ | ✗ |
| `default` | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

## Ví dụ thực tế

### Cùng package
```java
// File: TestA.java
package com.example;

class TestA {
    public void methodPublic() {}
    protected void methodProtected() {}
    void methodDefault() {}
    private void methodPrivate() {}
}

// File: TestB.java
package com.example;

public class TestB {
    public static void main(String[] args) {
        TestA a = new TestA();
        a.methodPublic();      // OK
        a.methodProtected();   // OK
        a.methodDefault();     // OK
        // a.methodPrivate();  // ERROR
    }
}
```

### Khác package
```java
// File: TestC.java
package com.other;

import com.example.TestB;

public class TestC {
    public static void main(String[] args) {
        TestB b = new TestB();
        b.methodPublic();      // OK
        // b.methodProtected(); // ERROR
        // b.methodDefault();   // ERROR
    }
}
```

### Subclass khác package
```java
// File: TestE.java
package com.other;

import com.example.TestB;

public class TestE extends TestB {
    public static void main(String[] args) {
        TestB b = new TestB();
        b.methodPublic();           // OK
        // b.methodProtected();     // ERROR - qua instance của parent
        
        TestE e = new TestE();
        e.methodProtected();        // OK - qua instance của subclass
    }
}
```

## Best Practices

1. **Biến luôn private**: Dùng getter/setter để truy cập
```java
public class User {
    private String name;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

2. **Method helper là private**: Chỉ dùng trong class
```java
public class Calculator {
    public int calculate(int a, int b) {
        return add(a, b);
    }
    
    private int add(int a, int b) {
        return a + b;
    }
}
```

3. **Protected cho inheritance**: Cho phép subclass override
```java
public class Animal {
    protected void makeSound() {
        // Subclass có thể override
    }
}
```

4. **Public cho API**: Chỉ expose những gì cần thiết
```java
public class Service {
    public void publicAPI() {
        // API công khai
        privateHelper();
    }
    
    private void privateHelper() {
        // Logic nội bộ
    }
}
```

## Lưu ý quan trọng

1. Class chỉ có `public` hoặc `default`
2. `protected` khác `default`: protected cho phép subclass truy cập
3. Subclass truy cập `protected` qua instance của chính nó, không qua parent instance
4. Luôn dùng access modifier hạn chế nhất có thể (principle of least privilege)
5. Không dùng `public` cho biến trừ khi là constant (`public static final`)
