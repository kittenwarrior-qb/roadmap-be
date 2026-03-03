# Java Data Types - Primitives

## Giới Thiệu

Java là ngôn ngữ lập trình **statically typed** (kiểu tĩnh).
- Phải khai báo data type khi tạo variable
- Khác với dynamically typed languages (PHP, JavaScript)
- Giúp optimize code và phát hiện lỗi sớm (compile time)

### Lợi Ích của Static Typing
1. **Optimization:** Mỗi data type có resource requirements cụ thể
2. **Error Detection:** Phát hiện lỗi trước khi testing
3. **Code Safety:** Không thể gán sai kiểu dữ liệu

---

## Hai Loại Data Types trong Java

1. **Primitive Types** (Kiểu nguyên thủy)
2. **Reference Types** (Kiểu tham chiếu / Non-primitive)

---

## Primitive Types

### Đặc Điểm
- Đơn giản và cơ bản nhất
- Đại diện cho raw values: numbers, characters
- Luôn bắt đầu bằng chữ thường: int, boolean, char
- Không phải objects, không có methods
- Lưu trực tiếp giá trị trong memory

### Các Primitive Types Phổ Biến
- **int** - Integers (số nguyên)
- **boolean** - Boolean values (true/false)
- **char** - Characters (ký tự đơn)
- **double** - Floating-point numbers (số thực)
- **long** - Large integers
- **float** - Floating-point numbers (nhỏ hơn double)
- **byte** - Very small integers
- **short** - Small integers

---

## 1. Integer (int)

### Đặc Điểm
- Lưu trữ số nguyên (cả âm và dương)
- Range: -2,147,483,648 đến 2,147,483,647
- Đủ lớn cho hầu hết mục đích sử dụng

### Cú Pháp
```java
int variableName = value;
```

### Ví Dụ
```java
int theAnswer = 42;
System.out.println("The answer to all questions is " + theAnswer);
// Output: The answer to all questions is 42
```

### Quy Tắc Khai Báo
1. Chỉ định data type trước: `int`
2. Đặt tên variable: `theAnswer`
3. Gán giá trị với `=`: `42`


### Naming Convention
- Dùng **Camel Case**: `theAnswer`, `myAge`, `totalCount`
- Bắt đầu bằng chữ thường
- Từ thứ hai trở đi viết hoa chữ cái đầu

### Ví Dụ Thực Tế
```java
int age = 25;
int score = 100;
int temperature = -5;
int maxValue = 2147483647;

System.out.println("Age: " + age);           // Age: 25
System.out.println("Score: " + score);       // Score: 100
System.out.println("Temperature: " + temperature);  // Temperature: -5
```

---

## 2. Boolean (boolean)

### Đặc Điểm
- Lưu trữ giá trị logic: `true` hoặc `false`
- Dùng cho điều kiện, flags, switches
- Chỉ có 2 giá trị có thể

### Cú Pháp
```java
boolean variableName = true;  // hoặc false
```

### Ví Dụ
```java
boolean isJavaFun = true;
System.out.println("Java is fun: " + isJavaFun);
// Output: Java is fun: true
```

### Ví Dụ Thực Tế
```java
boolean isLoggedIn = false;
boolean hasPermission = true;
boolean isAdult = age >= 18;
boolean isWeekend = (day == 6 || day == 7);

System.out.println("Logged in: " + isLoggedIn);      // false
System.out.println("Has permission: " + hasPermission);  // true
```

### Sử Dụng trong Điều Kiện
```java
boolean isRaining = true;

if (isRaining) {
    System.out.println("Take an umbrella!");
} else {
    System.out.println("Enjoy the sunshine!");
}
// Output: Take an umbrella!
```

---

## 3. Character (char)

### Đặc Điểm
- Lưu trữ 1 ký tự đơn
- Dùng **single quotes** (dấu nháy đơn): `'a'`
- Double quotes dùng cho String: `"hello"`
- Building block cho String class

### Cú Pháp
```java
char variableName = 'a';
```

### Ví Dụ
```java
char firstLetter = 'a';
char grade = 'A';
char symbol = '$';
char digit = '5';

System.out.println("First letter: " + firstLetter);  // a
System.out.println("Grade: " + grade);               // A
```


### Lưu Ý
```java
char letter = 'a';      // ✓ Đúng: single quotes
char word = "hello";    // ✗ Sai: double quotes cho String

char single = 'ab';     // ✗ Sai: chỉ 1 ký tự
char correct = 'a';     // ✓ Đúng
```

### Special Characters
```java
char newline = '\n';    // Xuống dòng
char tab = '\t';        // Tab
char backslash = '\\';  // Backslash
char quote = '\'';      // Single quote
```

---

## Sử Dụng JShell để Test

### Khởi Động JShell
```bash
jshell
```

Output:
```
|  Welcome to JShell -- Version 11.0.16
|  For an introduction type: /help intro

jshell>
```

### Test int
```java
jshell> int theAnswer = 42;
theAnswer ==> 42

jshell> System.out.println("The answer is " + theAnswer);
The answer is 42
```

### Test boolean
```java
jshell> boolean isJavaFun = true;
isJavaFun ==> true

jshell> System.out.println("Java is fun: " + isJavaFun);
Java is fun: true
```

### Test char
```java
jshell> char firstLetter = 'a';
firstLetter ==> 'a'

jshell> System.out.println("First letter: " + firstLetter);
First letter: a
```

### Thoát JShell
```java
jshell> /exit
```

---

## Tất Cả Primitive Types

| Type | Size | Range | Default | Ví Dụ |
|------|------|-------|---------|-------|
| **byte** | 8 bit | -128 to 127 | 0 | `byte b = 100;` |
| **short** | 16 bit | -32,768 to 32,767 | 0 | `short s = 1000;` |
| **int** | 32 bit | -2³¹ to 2³¹-1 | 0 | `int i = 100000;` |
| **long** | 64 bit | -2⁶³ to 2⁶³-1 | 0L | `long l = 100000L;` |
| **float** | 32 bit | ~±3.4e38 | 0.0f | `float f = 3.14f;` |
| **double** | 64 bit | ~±1.7e308 | 0.0d | `double d = 3.14;` |
| **char** | 16 bit | 0 to 65,535 | '\u0000' | `char c = 'A';` |
| **boolean** | 1 bit | true/false | false | `boolean b = true;` |


### Ví Dụ Tất Cả Types
```java
// Integer types
byte myByte = 127;
short myShort = 32000;
int myInt = 2147483647;
long myLong = 9223372036854775807L;  // Thêm 'L' ở cuối

// Floating-point types
float myFloat = 3.14f;    // Thêm 'f' ở cuối
double myDouble = 3.14159265359;

// Character
char myChar = 'A';

// Boolean
boolean myBoolean = true;

// In ra
System.out.println("byte: " + myByte);
System.out.println("short: " + myShort);
System.out.println("int: " + myInt);
System.out.println("long: " + myLong);
System.out.println("float: " + myFloat);
System.out.println("double: " + myDouble);
System.out.println("char: " + myChar);
System.out.println("boolean: " + myBoolean);
```

---

## Reference Types (Non-Primitive)

### Đặc Điểm
- Tham chiếu đến objects (không lưu trực tiếp giá trị)
- Được tạo từ classes
- Có methods và properties
- Bắt đầu bằng chữ HOA: String, Integer, ArrayList
- Có thể là null

### So Sánh Primitive vs Reference

```java
// Primitive: lưu trực tiếp giá trị
int age = 25;

// Reference: tham chiếu đến object
String name = "John";
```

### Tại Sao Reference Types Quan Trọng?
1. **Objects có methods:** Có thể thực hiện actions
2. **Advanced properties:** Phức tạp hơn primitive
3. **Essential cho OOP:** Cần thiết cho lập trình hướng đối tượng

### Ví Dụ Reference Type
```java
// String là reference type
String greeting = "Hello, World!";
System.out.println(greeting.length());      // 13
System.out.println(greeting.toUpperCase()); // HELLO, WORLD!

// Integer là wrapper class của int
Integer number = 42;
System.out.println(number.toString());      // "42"
```


---

## Primitive vs Reference - Chi Tiết

### Memory Storage

**Primitive Types:**
```java
int x = 10;
// Memory: [x: 10]
// Lưu trực tiếp giá trị
```

**Reference Types:**
```java
String name = "John";
// Memory: [name: 0x1234] -> [Object: "John"]
// Lưu địa chỉ tham chiếu đến object
```

### Comparison

**Primitive:**
```java
int a = 5;
int b = 5;
System.out.println(a == b);  // true (so sánh giá trị)
```

**Reference:**
```java
String s1 = new String("Hello");
String s2 = new String("Hello");
System.out.println(s1 == s2);        // false (so sánh địa chỉ)
System.out.println(s1.equals(s2));   // true (so sánh nội dung)
```

### Default Values

**Primitive:**
```java
class Test {
    int number;        // Default: 0
    boolean flag;      // Default: false
    char letter;       // Default: '\u0000'
}
```

**Reference:**
```java
class Test {
    String name;       // Default: null
    Integer number;    // Default: null
}
```

---

## Best Practices

### 1. Chọn Đúng Type
```java
// ✓ Đúng: Dùng int cho số nguyên thông thường
int age = 25;

// ✗ Sai: Dùng long khi không cần
long age = 25L;

// ✓ Đúng: Dùng boolean cho flags
boolean isActive = true;

// ✗ Sai: Dùng int cho boolean
int isActive = 1;  // Confusing!
```

### 2. Naming Conventions
```java
// ✓ Đúng: Camel case, descriptive names
int studentAge = 20;
boolean isLoggedIn = true;
char firstInitial = 'J';

// ✗ Sai: Unclear names
int x = 20;
boolean b = true;
char c = 'J';
```

### 3. Initialize Variables
```java
// ✓ Đúng: Initialize khi khai báo
int count = 0;
boolean isReady = false;

// ⚠️ Cẩn thận: Uninitialized local variables
void method() {
    int x;
    // System.out.println(x);  // Compile error!
}
```


---

## Ví Dụ Thực Hành

### Example 1: Calculator
```java
int num1 = 10;
int num2 = 5;

int sum = num1 + num2;
int difference = num1 - num2;
int product = num1 * num2;
int quotient = num1 / num2;

System.out.println("Sum: " + sum);           // 15
System.out.println("Difference: " + difference);  // 5
System.out.println("Product: " + product);   // 50
System.out.println("Quotient: " + quotient); // 2
```

### Example 2: Grade Checker
```java
int score = 85;
char grade;

if (score >= 90) {
    grade = 'A';
} else if (score >= 80) {
    grade = 'B';
} else if (score >= 70) {
    grade = 'C';
} else {
    grade = 'F';
}

System.out.println("Score: " + score);
System.out.println("Grade: " + grade);
// Output:
// Score: 85
// Grade: B
```

### Example 3: User Status
```java
String username = "john_doe";
int age = 25;
boolean isActive = true;
boolean isPremium = false;
char accountType = 'S';  // S=Standard, P=Premium

System.out.println("=== User Profile ===");
System.out.println("Username: " + username);
System.out.println("Age: " + age);
System.out.println("Active: " + isActive);
System.out.println("Premium: " + isPremium);
System.out.println("Account Type: " + accountType);

// Output:
// === User Profile ===
// Username: john_doe
// Age: 25
// Active: true
// Premium: false
// Account Type: S
```

---

## Common Mistakes

### 1. Mixing Single and Double Quotes
```java
// ✗ Sai
char letter = "a";  // Compile error!

// ✓ Đúng
char letter = 'a';
String word = "hello";
```

### 2. Integer Overflow
```java
int maxInt = 2147483647;
int overflow = maxInt + 1;
System.out.println(overflow);  // -2147483648 (overflow!)

// ✓ Đúng: Dùng long nếu cần số lớn hơn
long bigNumber = 2147483648L;
```

### 3. Forgetting Suffixes
```java
// ✗ Sai
long bigNum = 9223372036854775807;  // Compile error!
float pi = 3.14;  // Compile error!

// ✓ Đúng
long bigNum = 9223372036854775807L;  // Thêm 'L'
float pi = 3.14f;  // Thêm 'f'
```

### 4. Uninitialized Variables
```java
// ✗ Sai
int count;
System.out.println(count);  // Compile error!

// ✓ Đúng
int count = 0;
System.out.println(count);  // 0
```

---

## Tổng Kết

### Key Points
1. **Primitive Types:** Lưu trực tiếp giá trị (int, boolean, char, etc.)
2. **Reference Types:** Tham chiếu đến objects (String, Integer, etc.)
3. **Static Typing:** Phải khai báo type khi tạo variable
4. **Naming:** Dùng Camel case cho variable names

### Primitive Types Phổ Biến
- **int:** Số nguyên (-2³¹ to 2³¹-1)
- **boolean:** true hoặc false
- **char:** 1 ký tự đơn ('a', 'B', '5')
- **double:** Số thực (3.14, 2.718)

### Remember
- Primitive types: lowercase, simple values
- Reference types: uppercase, objects with methods
- Chọn đúng type để optimize memory và performance
- Initialize variables để tránh errors!
