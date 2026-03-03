# Java Nested Class - Tóm Tắt

## Khái Niệm
Java inner class là class được định nghĩa bên trong body của class khác. 

Java nested classes được chia thành 2 loại:
- Static Nested Class (class lồng tĩnh)
- Inner Class (class bên trong - non-static)

---

## 1. Static Nested Class

### Đặc Điểm
- Được khai báo với từ khóa static
- Chỉ có thể truy cập static members của outer class
- Giống như top-level class, chỉ nested để tiện packaging
- Có thể có access modifier: private, public, protected, default

### Cách Tạo Object
```java
OuterClass.StaticNestedClass nestedObject = 
    new OuterClass.StaticNestedClass();
```

### Ví Dụ
```java
public class OuterClass {
    private static String name = "OuterClass";
    private int instanceVar = 10;
    
    // Static Nested Class
    static class StaticNestedClass {
        private int a;
        public int d;
        
        public String getName() {
            return name;  // OK: access static variable
        }
    }
}
```


---

## 2. Inner Class (Non-Static Nested Class)

### Đặc Điểm
- Không có từ khóa static
- Gắn liền với object của outer class
- Có thể truy cập TẤT CẢ members của outer class (kể cả private)
- Không thể có static variables
- Có thể có access modifier: private, public, protected, default

### Cách Tạo Object
```java
OuterClass outerObject = new OuterClass();
OuterClass.InnerClass innerObject = outerObject.new InnerClass();
```

### Ví Dụ
```java
public class OuterClass {
    private static String name = "OuterClass";
    private int i = 1;
    protected int j = 2;
    
    // Inner Class
    class InnerClass {
        private int w;
        public int z;
        
        public void setValues() {
            this.w = i;  // Access outer class instance variable
            this.z = j;
        }
        
        public String getName() {
            return name;  // Access outer class static variable
        }
    }
}
```


---

## 3. Local Inner Class

### Đặc Điểm
- Class được định nghĩa trong method body
- Không thể dùng access modifier (private, public, protected)
- Chỉ có thể dùng: abstract hoặc final
- Có thể access tất cả members của outer class
- Có thể access local final variables của method
- Có thể đọc non-final local variables nhưng KHÔNG thể modify

### Ví Dụ
```java
public class MainClass {
    private String s_main_class = "Main";
    
    public void print() {
        String s_print_method = "Print";
        
        // Local Inner Class inside method
        class Logger {
            String name = s_main_class;  // OK: access outer class variable
            String name1 = s_print_method;  // OK: access method variable
            
            public void foo() {
                System.out.println(s_print_method);  // OK: read
                // s_print_method = "New";  // ERROR: cannot modify
            }
        }
        
        // Instantiate local inner class
        Logger logger = new Logger();
        logger.foo();
    }
}
```

### Local Inner Class trong Block
```java
public class MainClass {
    static {
        class Foo { }
        Foo f = new Foo();  // OK: trong scope
    }
    
    public void bar() {
        if (1 < 2) {
            class Test { }
            Test t1 = new Test();  // OK: trong scope
        }
        // Test t = new Test();  // ERROR: ngoài scope
    }
}
```


---

## 4. Anonymous Inner Class

### Đặc Điểm
- Local inner class KHÔNG CÓ TÊN
- Được định nghĩa và khởi tạo trong 1 statement
- Luôn extend một class HOẶC implement một interface
- Không thể định nghĩa constructor (vì không có tên)
- Chỉ có thể truy cập tại điểm được định nghĩa

### Ví Dụ - Implement Interface
```java
import java.io.File;
import java.io.FilenameFilter;

public class OuterClass {
    // Anonymous inner class implementing FilenameFilter
    public String[] getFilesInDir(String dir, final String ext) {
        File file = new File(dir);
        
        String[] filesList = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(ext);
            }
        });
        
        return filesList;
    }
}
```

### Ví Dụ - Extend Class
```java
abstract class Animal {
    abstract void makeSound();
}

public class Test {
    public void test() {
        // Anonymous inner class extending Animal
        Animal dog = new Animal() {
            @Override
            void makeSound() {
                System.out.println("Woof!");
            }
        };
        
        dog.makeSound();  // Output: Woof!
    }
}
```


---

## Ví Dụ Đầy Đủ

### OuterClass.java
```java
package com.journaldev.nested;

import java.io.File;
import java.io.FilenameFilter;

public class OuterClass {
    private static String name = "OuterClass";
    private int i;
    protected int j;
    int k;
    public int l;

    public OuterClass(int i, int j, int k, int l) {
        this.i = i;
        this.j = j;
        this.k = k;
        this.l = l;
    }

    public int getI() {
        return this.i;
    }

    // 1. STATIC NESTED CLASS
    static class StaticNestedClass {
        private int a;
        public int d;

        public String getName() {
            return name;  // Access static variable
        }
    }

    // 2. INNER CLASS
    class InnerClass {
        private int w;
        public int z;

        public void setValues() {
            this.w = i;  // Access all outer class variables
            this.z = l;
        }

        public String getName() {
            return name;
        }
    }

    // 3. LOCAL INNER CLASS
    public void print(String initial) {
        class Logger {
            String name;

            public Logger(String name) {
                this.name = name;
            }

            public void log(String str) {
                System.out.println(this.name + ": " + str);
            }
        }

        Logger logger = new Logger(initial);
        logger.log(name);
        logger.log("" + this.i);
    }

    // 4. ANONYMOUS INNER CLASS
    public String[] getFilesInDir(String dir, final String ext) {
        File file = new File(dir);
        
        String[] filesList = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(ext);
            }
        });
        
        return filesList;
    }
}
```


### InnerClassTest.java
```java
package com.journaldev.nested;

import java.util.Arrays;
import com.journaldev.nested.OuterClass.InnerClass;
import com.journaldev.nested.OuterClass.StaticNestedClass;

public class InnerClassTest {
    public static void main(String[] args) {
        OuterClass outer = new OuterClass(1, 2, 3, 4);
        
        // 1. Static Nested Class
        StaticNestedClass staticNested = new StaticNestedClass();
        System.out.println(staticNested.getName());  // OuterClass
        staticNested.d = 10;
        
        // 2. Inner Class
        InnerClass inner = outer.new InnerClass();
        System.out.println(inner.getName());  // OuterClass
        inner.setValues();
        
        // 3. Local Inner Class
        outer.print("Outer");
        // Output:
        // Outer: OuterClass
        // Outer: 1
        
        // 4. Anonymous Inner Class
        String[] files = outer.getFilesInDir("src", ".java");
        System.out.println(Arrays.toString(files));
    }
}
```

### Output
```
OuterClass
OuterClass
Outer: OuterClass
Outer: 1
[OuterClass.java, InnerClassTest.java]
```


---

## Compiled Class Files

Khi compile OuterClass, sẽ tạo ra các file riêng biệt:
```
OuterClass.class
OuterClass$StaticNestedClass.class
OuterClass$InnerClass.class
OuterClass$1.class                    (anonymous inner class)
OuterClass$1Logger.class              (local inner class)
```

---

## Lợi Ích của Java Inner Class

### 1. Logical Grouping
Nếu một class chỉ hữu ích cho 1 class khác, nên giữ chúng lồng nhau.
Giúp packaging tốt hơn.

```java
class OuterClass {
    // Helper class chỉ dùng trong OuterClass
    private class Helper {
        void help() { }
    }
}
```

### 2. Encapsulation
Inner class có thể access private members của outer class.
Đồng thời có thể ẩn inner class khỏi bên ngoài.

```java
public class BankAccount {
    private double balance;
    
    // Inner class access private balance
    private class Transaction {
        void updateBalance(double amount) {
            balance += amount;  // Access private member
        }
    }
}
```

### 3. Code Readability
Giữ class nhỏ gần với nơi sử dụng, code dễ đọc và maintain hơn.

```java
class Button {
    // Event handler gần với Button
    private class ClickListener {
        void onClick() {
            // Handle click
        }
    }
}
```


---

## So Sánh Các Loại Nested Class

| Loại | Static? | Access Outer Members | Constructor | Access Modifier |
|------|---------|---------------------|-------------|-----------------|
| Static Nested Class | Yes | Chỉ static members | Yes | private, public, protected, default |
| Inner Class | No | Tất cả members | Yes | private, public, protected, default |
| Local Inner Class | No | Tất cả members + local variables | Yes | abstract, final only |
| Anonymous Inner Class | No | Tất cả members + local variables | No | None |

---

## Khi Nào Dùng?

### Static Nested Class
- Khi không cần access instance members của outer class
- Khi muốn group classes liên quan
- Ví dụ: Builder pattern

```java
class Person {
    private String name;
    
    static class Builder {
        private String name;
        
        Builder setName(String name) {
            this.name = name;
            return this;
        }
        
        Person build() {
            return new Person(name);
        }
    }
}
```

### Inner Class
- Khi cần access instance members của outer class
- Khi class chỉ có ý nghĩa trong context của outer class
- Ví dụ: Iterator, Event Listener

```java
class ArrayList {
    class Iterator {
        // Access ArrayList's internal data
    }
}
```

### Local Inner Class
- Khi class chỉ dùng trong 1 method
- Khi cần access local variables của method

### Anonymous Inner Class
- Khi cần implement interface/class ngay lập tức
- Khi class chỉ dùng 1 lần
- Ví dụ: Event handlers, callbacks

```java
button.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick() {
        // Handle click
    }
});
```

---

## Tổng Kết

### Key Points
1. Static Nested Class: Chỉ access static members
2. Inner Class: Access tất cả members của outer class
3. Local Inner Class: Định nghĩa trong method
4. Anonymous Inner Class: Không có tên, dùng 1 lần

### Best Practices
- Dùng static nested class khi không cần access instance members
- Dùng inner class để encapsulation tốt hơn
- Dùng anonymous inner class cho code ngắn gọn
- Tránh lạm dụng nested class quá sâu (khó đọc)

### Remember
Inner classes giúp code organized, encapsulated, và readable hơn khi dùng đúng chỗ!
