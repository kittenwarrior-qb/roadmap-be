# Java Keywords: static, break, continue - Tóm Tắt

## 1. Static Keyword

### Khái Niệm
`static` tạo biến/method thuộc về **Class** (không thuộc về instance/object). Được chia sẻ giữa tất cả các instances.

### 5 Cách Sử Dụng Static

#### 1.1. Static Variable (Biến Tĩnh)

**Đặc điểm:**
- Thuộc về class, không thuộc về object
- Được chia sẻ giữa tất cả instances
- Truy cập: `ClassName.variableName`

**Ví dụ:**
```java
public class Counter {
    private static int count = 0;  // Biến static
    public static String appName = "MyApp";
    public static final String DB_USER = "admin";  // Hằng số
    
    public Counter() {
        count++;  // Tất cả objects dùng chung biến count
    }
}

// Sử dụng
Counter.appName = "NewApp";  // Truy cập trực tiếp
Counter c1 = new Counter();  // count = 1
Counter c2 = new Counter();  // count = 2 (dùng chung)
```

---

#### 1.2. Static Method (Phương Thức Tĩnh)

**Đặc điểm:**
- Thuộc về class, gọi mà không cần tạo object
- Chỉ truy cập được static variables/methods
- Không truy cập được instance variables/methods
- Thường dùng cho utility methods

**Ví dụ:**
```java
public class MathUtil {
    // Static method - utility
    public static int add(int a, int b) {
        return a + b;
    }
    
    public static int sum(int... numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }
}

// Sử dụng - không cần tạo object
int result = MathUtil.add(5, 3);  // 8
int total = MathUtil.sum(1, 2, 3, 4);  // 10

// main() cũng là static method
public static void main(String[] args) {
    // Entry point của chương trình
}
```

---

#### 1.3. Static Block (Khối Tĩnh)

**Đặc điểm:**
- Chạy 1 lần duy nhất khi class được load vào memory
- Dùng để khởi tạo static resources
- Chạy trước constructor
- Có thể có nhiều static blocks (chạy theo thứ tự)

**Ví dụ:**
```java
public class Database {
    private static String connectionUrl;
    private static int maxConnections;
    
    // Static block 1
    static {
        System.out.println("Loading database config...");
        connectionUrl = "jdbc:mysql://localhost:3306/mydb";
        maxConnections = 10;
    }
    
    // Static block 2
    static {
        System.out.println("Database initialized!");
    }
    
    public Database() {
        System.out.println("Creating Database instance");
    }
}

// Khi chạy:
Database db = new Database();
// Output:
// Loading database config...
// Database initialized!
// Creating Database instance
```

---

#### 1.4. Static Nested Class (Lớp Lồng Tĩnh)

**Đặc điểm:**
- Class con được khai báo static bên trong class cha
- Không cần instance của outer class để tạo object
- Dùng để nhóm các class liên quan (packaging convenience)

**Ví dụ:**
```java
public class OuterClass {
    private static String outerStaticVar = "Outer Static";
    private String outerInstanceVar = "Outer Instance";
    
    // Static nested class
    public static class StaticNestedClass {
        public void display() {
            System.out.println(outerStaticVar);  // OK
            // System.out.println(outerInstanceVar);  // ERROR!
        }
    }
}

// Sử dụng
OuterClass.StaticNestedClass nested = new OuterClass.StaticNestedClass();
nested.display();  // Output: Outer Static
```

---

#### 1.5. Static Import

**Đặc điểm:**
- Import static members để dùng trực tiếp (không cần tên class)
- Giảm code nhưng có thể làm giảm tính rõ ràng

**Ví dụ:**
```java
// File: MathConstants.java
public class MathConstants {
    public static final double PI = 3.14159;
    public static final int MAX_VALUE = 1000;
    
    public static void printInfo() {
        System.out.println("Math utility class");
    }
}

// File: Calculator.java
import static MathConstants.PI;
import static MathConstants.MAX_VALUE;
import static MathConstants.printInfo;
// Hoặc import tất cả: import static MathConstants.*;

public class Calculator {
    public static void main(String[] args) {
        System.out.println(PI);  // Thay vì MathConstants.PI
        System.out.println(MAX_VALUE);  // Thay vì MathConstants.MAX_VALUE
        printInfo();  // Thay vì MathConstants.printInfo()
    }
}
```

---

### Ví Dụ Tổng Hợp Static

```java
public class Student {
    // Static variable
    private static int totalStudents = 0;
    public static String schoolName = "ABC School";
    
    // Instance variable
    private String name;
    private int id;
    
    // Static block
    static {
        System.out.println("Student class loaded!");
        schoolName = "ABC High School";
    }
    
    // Constructor
    public Student(String name) {
        this.name = name;
        this.id = ++totalStudents;
    }
    
    // Static method
    public static int getTotalStudents() {
        return totalStudents;
    }
    
    // Instance method
    public void displayInfo() {
        System.out.println("ID: " + id + ", Name: " + name);
        System.out.println("School: " + schoolName);
    }
}

// Sử dụng
public class Main {
    public static void main(String[] args) {
        // Output: Student class loaded!
        
        System.out.println(Student.schoolName);  // ABC High School
        
        Student s1 = new Student("John");
        Student s2 = new Student("Jane");
        
        s1.displayInfo();
        // ID: 1, Name: John
        // School: ABC High School
        
        s2.displayInfo();
        // ID: 2, Name: Jane
        // School: ABC High School
        
        System.out.println("Total: " + Student.getTotalStudents());  // 2
    }
}
```

---

## 2. Break Keyword

### Khái Niệm
`break` dùng để **thoát khỏi** loop hoặc switch statement.

### 2.1. Unlabeled Break (Break Thông Thường)

**Đặc điểm:**
- Thoát khỏi loop gần nhất (for, while, do-while, switch)
- Dùng khi tìm thấy kết quả hoặc đạt điều kiện dừng

**Ví dụ:**
```java
// Break trong for loop
String[] arr = {"A", "E", "I", "O", "U"};
for (int i = 0; i < arr.length; i++) {
    if (arr[i].equals("O")) {
        System.out.println("Found 'O' at index: " + i);
        break;  // Dừng loop ngay khi tìm thấy
    }
}
// Output: Found 'O' at index: 3

// Break trong while loop
int i = 0;
while (i < arr.length) {
    if (arr[i].equals("E")) {
        System.out.println("Found 'E' at index: " + i);
        break;
    }
    i++;
}
// Output: Found 'E' at index: 1

// Break trong do-while loop
int j = 0;
do {
    if (arr[j].equals("U")) {
        System.out.println("Found 'U' at index: " + j);
        break;
    }
    j++;
} while (j < arr.length);
// Output: Found 'U' at index: 4
```

**Ví dụ thực tế:**
```java
// Tìm số nguyên tố đầu tiên > 100
for (int num = 101; num < 200; num++) {
    boolean isPrime = true;
    for (int i = 2; i <= Math.sqrt(num); i++) {
        if (num % i == 0) {
            isPrime = false;
            break;  // Không cần kiểm tra thêm
        }
    }
    if (isPrime) {
        System.out.println("First prime > 100: " + num);
        break;  // Tìm thấy rồi, dừng luôn
    }
}
```

---

### 2.2. Labeled Break (Break Có Nhãn)

**Đặc điểm:**
- Thoát khỏi loop bên ngoài (outer loop)
- Cần đặt label (nhãn) cho loop muốn thoát
- Hữu ích với nested loops (vòng lặp lồng nhau)

**Ví dụ:**
```java
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9},
    {10, 11, 12}
};

boolean found = false;
int row = 0, col = 0;

// Label cho outer loop
searchLoop:
for (row = 0; row < matrix.length; row++) {
    for (col = 0; col < matrix[row].length; col++) {
        if (matrix[row][col] > 10) {
            found = true;
            break searchLoop;  // Thoát cả 2 loops
        }
    }
}

if (found) {
    System.out.println("Found at [" + row + "," + col + "]");
}
// Output: Found at [3,1]
```

**So sánh có/không có label:**
```java
// KHÔNG có label - chỉ thoát inner loop
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (j == 1) break;  // Chỉ thoát inner loop
        System.out.print(i + "," + j + " ");
    }
}
// Output: 0,0 1,0 2,0

// CÓ label - thoát cả outer loop
outer:
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (j == 1) break outer;  // Thoát cả 2 loops
        System.out.print(i + "," + j + " ");
    }
}
// Output: 0,0
```

---

## 3. Continue Keyword

### Khái Niệm
`continue` dùng để **bỏ qua** iteration hiện tại và chuyển sang iteration tiếp theo.

### 3.1. Unlabeled Continue

**Đặc điểm:**
- Bỏ qua phần code còn lại trong iteration hiện tại
- Chuyển sang iteration tiếp theo của loop
- Khác với break: continue không thoát loop

**Ví dụ:**
```java
// In số chẵn từ 1-10
for (int i = 1; i <= 10; i++) {
    if (i % 2 != 0) {
        continue;  // Bỏ qua số lẻ
    }
    System.out.print(i + " ");
}
// Output: 2 4 6 8 10

// Bỏ qua phần tử null
String[] names = {"John", null, "Jane", null, "Bob"};
for (String name : names) {
    if (name == null) {
        continue;  // Bỏ qua null
    }
    System.out.println("Hello, " + name);
}
// Output:
// Hello, John
// Hello, Jane
// Hello, Bob
```

**Ví dụ thực tế:**
```java
// Xử lý danh sách, bỏ qua invalid items
int[] scores = {85, -1, 92, 0, 78, -5, 88};
int total = 0;
int count = 0;

for (int score : scores) {
    if (score < 0) {
        System.out.println("Invalid score: " + score);
        continue;  // Bỏ qua điểm không hợp lệ
    }
    total += score;
    count++;
}

double average = (double) total / count;
System.out.println("Average: " + average);
// Output:
// Invalid score: -1
// Invalid score: -5
// Average: 68.6
```

---

### 3.2. Labeled Continue

**Đặc điểm:**
- Bỏ qua iteration hiện tại của outer loop
- Chuyển sang iteration tiếp theo của loop được label

**Ví dụ:**
```java
// In bảng cửu chương, bỏ qua dòng có i = 5
outer:
for (int i = 1; i <= 10; i++) {
    if (i == 5) {
        continue outer;  // Bỏ qua toàn bộ dòng i=5
    }
    for (int j = 1; j <= 3; j++) {
        System.out.print(i + "x" + j + "=" + (i*j) + " ");
    }
    System.out.println();
}
// Output:
// 1x1=1 1x2=2 1x3=3
// 2x1=2 2x2=4 2x3=6
// 3x1=3 3x2=6 3x3=9
// 4x1=4 4x2=8 4x3=12
// (dòng 5 bị bỏ qua)
// 6x1=6 6x2=12 6x3=18
// ...
```

---

## So Sánh Break vs Continue

| Tiêu chí | Break | Continue |
|----------|-------|----------|
| **Chức năng** | Thoát khỏi loop | Bỏ qua iteration hiện tại |
| **Loop sau đó** | Dừng hẳn | Tiếp tục iteration tiếp theo |
| **Khi nào dùng** | Tìm thấy kết quả, đạt điều kiện dừng | Bỏ qua invalid data, điều kiện đặc biệt |
| **Với switch** | Có thể dùng | Không dùng |

**Ví dụ so sánh:**
```java
System.out.println("=== BREAK ===");
for (int i = 1; i <= 5; i++) {
    if (i == 3) {
        break;  // Dừng hẳn khi i=3
    }
    System.out.print(i + " ");
}
// Output: 1 2

System.out.println("\n=== CONTINUE ===");
for (int i = 1; i <= 5; i++) {
    if (i == 3) {
        continue;  // Bỏ qua i=3, tiếp tục với i=4,5
    }
    System.out.print(i + " ");
}
// Output: 1 2 4 5
```

---

## Tổng Kết

### Static Keyword
- **Static variable:** Biến dùng chung cho tất cả objects
- **Static method:** Method gọi mà không cần tạo object
- **Static block:** Chạy 1 lần khi class load
- **Static class:** Nested class độc lập
- **Static import:** Import để dùng trực tiếp

### Break Keyword
- **Unlabeled:** Thoát loop gần nhất
- **Labeled:** Thoát outer loop được chỉ định
- Dùng khi: Tìm thấy kết quả, đạt điều kiện dừng

### Continue Keyword
- **Unlabeled:** Bỏ qua iteration hiện tại
- **Labeled:** Bỏ qua iteration của outer loop
- Dùng khi: Bỏ qua invalid data, trường hợp đặc biệt

### Best Practices
1. Dùng `static` cho utility methods và constants
2. Dùng `break` để tối ưu performance (dừng sớm khi có thể)
3. Dùng `continue` để code sạch hơn (tránh nested if)
4. Tránh lạm dụng labeled break/continue (khó đọc)
