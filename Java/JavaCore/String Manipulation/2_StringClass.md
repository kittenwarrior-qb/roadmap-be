# Java String Class - Methods và Manipulation

## Tổng quan về String

String là một trong những class được sử dụng nhiều nhất trong Java, được định nghĩa trong package `java.lang`.

**Đặc điểm chính:**
- String là sequence of characters nhưng không phải primitive type
- String là immutable object (không thể thay đổi sau khi tạo)
- String là class duy nhất hỗ trợ operator overloading trong Java (toán tử `+`)
- Java cung cấp StringBuffer và StringBuilder để thao tác String hiệu quả

---

## Cách tạo String

### 1. Sử dụng String Literal
```java
String str = "abc";
```
- JVM tìm trong String Pool xem có String nào có cùng giá trị không
- Nếu có: trả về reference đến String đó
- Nếu không: tạo String mới và lưu vào pool

### 2. Sử dụng new keyword
```java
String str = new String("abc");

char[] a = {'a', 'b', 'c'};
String str2 = new String(a);
```
- Tạo String object mới trong heap space
- Không tự động lưu vào String Pool

---

## So sánh String

### equals() và equalsIgnoreCase()
So sánh giá trị của String:

```java
String s1 = "abc";
String s2 = "abc";
String s3 = "def";
String s4 = "ABC";

System.out.println(s1.equals(s2));              // true
System.out.println(s2.equals(s3));              // false
System.out.println(s1.equals(s4));              // false
System.out.println(s1.equalsIgnoreCase(s4));    // true
```

### compareTo() và compareToIgnoreCase()
So sánh lexicographically (theo thứ tự từ điển):

```java
String a1 = "abc";
String a2 = "abc";
String a3 = "def";
String a4 = "ABC";

System.out.println(a1.compareTo(a2));              // 0 (bằng nhau)
System.out.println(a2.compareTo(a3));              // -3 (nhỏ hơn)
System.out.println(a1.compareTo(a4));              // 32 (lớn hơn)
System.out.println(a1.compareToIgnoreCase(a4));    // 0
```

**Kết quả:**
- Trả về 0: hai String bằng nhau
- Trả về số âm: String đầu nhỏ hơn
- Trả về số dương: String đầu lớn hơn

---

## String Methods phổ biến

### 1. split()
Tách String thành mảng dựa trên regex:

```java
String s = "a/b/c/d";

// split(String regex)
String[] a1 = s.split("/");
for (String string : a1) {
    System.out.println(string);
}
// Output: a, b, c, d

// split(String regex, int limit)
String[] a2 = s.split("/", 2);
for (String string : a2) {
    System.out.println(string);
}
// Output: a, b/c/d
```

### 2. contains()
Kiểm tra String có chứa sequence of characters không:

```java
String s = "Hello World";

System.out.println(s.contains("W"));    // true
System.out.println(s.contains("X"));    // false
```

### 3. length()
Trả về độ dài của String:

```java
String s1 = "abc";
String s2 = "abcdef";
String s3 = "abcdefghi";

System.out.println(s1.length());    // 3
System.out.println(s2.length());    // 6
System.out.println(s3.length());    // 9
```

### 4. replace()
Thay thế một phần của String:

**4 variants:**

```java
// replace(char oldChar, char newChar)
String s = "Hello World";
s = s.replace('l', 'm');
System.out.println(s);    // Hemmo Wormd

// replaceAll(String regex, String replacement)
String s1 = "Hello journaldev, Hello pankaj";
s1 = s1.replaceAll("Hello", "Hi");
System.out.println(s1);    // Hi journaldev, Hi pankaj

// replaceFirst(String regex, String replacement)
String s2 = "Hello guys, Hello world";
s2 = s2.replaceFirst("Hello", "Hi");
System.out.println(s2);    // Hi guys, Hello world
```

### 5. format()
Format String với các tham số:

```java
String s = "journaldev.com";

// format(String format, Object... args)
System.out.println(String.format("This is %s", s));
// Output: This is journaldev.com

// format(Locale l, String format, Object... args)
System.out.println(String.format(Locale.US, "%f", 3.14));
// Output: 3.140000
```

### 6. substring()
Lấy một phần của String dựa trên index:

```java
String s = "This is journaldev.com";
s = s.substring(8, 18);
System.out.println(s);    // journaldev
```

---

## String Concatenation

Nối String bằng toán tử `+` hoặc method `concat()`:

```java
String s1 = "Hello";
String s2 = "World";

// Sử dụng + operator
String s3 = s1 + s2;
System.out.println(s3);    // HelloWorld

// Sử dụng concat() method
System.out.println(s1.concat(s2));    // HelloWorld
```

---

## String Pool

String Pool là vùng nhớ đặc biệt trong Java Heap Memory để lưu trữ String literals:

```java
String a = "abc";
String b = "abc";
String c = "def";

// Cùng reference
if (a == b) {
    System.out.println("Both string refer to the same object");
}

// Khác reference
if (a == c) {
    System.out.println("Both strings refer to the same object");
} else {
    System.out.println("Both strings refer to the different object");
}
```

**Output:**
```
Both string refer to the same object
Both strings refer to the different object
```

---

## intern() Method

Chuyển String từ heap memory vào String Pool:

```java
String s1 = "pankaj";
String s2 = "pankaj";
String s3 = new String("pankaj");

System.out.println(s1 == s2);    // true
System.out.println(s2 == s3);    // false

String s4 = s3.intern();
System.out.println(s1 == s4);    // true
```

**Cách hoạt động:**
- Kiểm tra xem có String với cùng giá trị trong pool không
- Nếu có: trả về reference từ pool
- Nếu không: tạo String mới trong pool và trả về reference

---

## Java 8 - String join()

Method static mới để nối các CharSequence với delimiter:

```java
List<String> words = Arrays.asList(new String[]{"Hello", "World", "2019"});
String msg = String.join(" ", words);
System.out.println(msg);    // Hello World 2019
```

---

## Java 9 - codePoints() và chars()

Trả về IntStream để thao tác với code points và characters:

```java
String s = "abc";

s.codePoints().forEach(x -> System.out.println(x));
// Output: 97, 98, 99

s.chars().forEach(x -> System.out.println(x));
// Output: 97, 98, 99
```

---

## Java 11 - New String Methods

### 1. isBlank()
Kiểm tra String rỗng hoặc chỉ chứa whitespace:

```java
String s = "abc";
System.out.println(s.isBlank());    // false

s = "";
System.out.println(s.isBlank());    // true
```

### 2. lines()
Trả về Stream các dòng được tách bởi line terminators:

```java
String s1 = "Hi\nHello\rHowdy";
List<String> lines = s1.lines().collect(Collectors.toList());
System.out.println(lines);    // [Hi, Hello, Howdy]
```

### 3. strip(), stripLeading(), stripTrailing()
Loại bỏ whitespace ở đầu và cuối String:

```java
String s2 = "  Java,  \tPython\t ";

System.out.println("#" + s2 + "#");
// #  Java,  	Python	 #

System.out.println("#" + s2.strip() + "#");
// #Java,  	Python#

System.out.println("#" + s2.stripLeading() + "#");
// #Java,  	Python	 #

System.out.println("#" + s2.stripTrailing() + "#");
// #  Java,  	Python#
```

### 4. repeat()
Lặp lại String một số lần nhất định:

```java
String s3 = "Hello\n";
System.out.println(s3.repeat(3));
// Hello
// Hello
// Hello

s3 = "Co";
System.out.println(s3.repeat(2));    // CoCo
```

---

## Ví dụ thực tế

### Ví dụ 1: Xử lý URL
```java
String url = "https://www.example.com/api/users";

// Tách protocol
String protocol = url.substring(0, url.indexOf("://"));
System.out.println("Protocol: " + protocol);    // https

// Kiểm tra có chứa "api" không
if (url.contains("api")) {
    System.out.println("This is an API endpoint");
}

// Thay thế domain
String newUrl = url.replace("example.com", "newdomain.com");
System.out.println(newUrl);    // https://www.newdomain.com/api/users
```

### Ví dụ 2: Xử lý CSV data
```java
String csvLine = "John,Doe,30,Engineer";
String[] fields = csvLine.split(",");

System.out.println("First Name: " + fields[0]);    // John
System.out.println("Last Name: " + fields[1]);     // Doe
System.out.println("Age: " + fields[2]);           // 30
System.out.println("Job: " + fields[3]);           // Engineer
```

### Ví dụ 3: Format output
```java
String name = "Alice";
int age = 25;
double salary = 50000.50;

String info = String.format("Name: %s, Age: %d, Salary: $%.2f", 
                            name, age, salary);
System.out.println(info);
// Name: Alice, Age: 25, Salary: $50000.50
```

### Ví dụ 4: Validate input
```java
String email = "user@example.com";

if (email.contains("@") && email.contains(".")) {
    System.out.println("Valid email format");
} else {
    System.out.println("Invalid email format");
}

// Kiểm tra độ dài password
String password = "abc123";
if (password.length() >= 8) {
    System.out.println("Password is strong");
} else {
    System.out.println("Password is too short");
}
```

### Ví dụ 5: String concatenation performance
```java
// Không hiệu quả với vòng lặp lớn
long start = System.currentTimeMillis();
String result = "";
for (int i = 0; i < 10000; i++) {
    result += i;
}
long end = System.currentTimeMillis();
System.out.println("Time with +: " + (end - start) + "ms");

// Hiệu quả hơn với StringBuilder
start = System.currentTimeMillis();
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 10000; i++) {
    sb.append(i);
}
String result2 = sb.toString();
end = System.currentTimeMillis();
System.out.println("Time with StringBuilder: " + (end - start) + "ms");
```

### Ví dụ 6: Text processing
```java
String text = "  Hello World  ";

// Loại bỏ whitespace
String trimmed = text.strip();
System.out.println("#" + trimmed + "#");    // #Hello World#

// Chuyển thành uppercase
String upper = trimmed.toUpperCase();
System.out.println(upper);    // HELLO WORLD

// Thay thế space bằng underscore
String formatted = upper.replace(" ", "_");
System.out.println(formatted);    // HELLO_WORLD
```

### Ví dụ 7: Join strings
```java
// Java 8 String.join()
String[] names = {"Alice", "Bob", "Charlie"};
String joined = String.join(", ", names);
System.out.println(joined);    // Alice, Bob, Charlie

// Với List
List<String> cities = Arrays.asList("New York", "London", "Tokyo");
String cityList = String.join(" | ", cities);
System.out.println(cityList);    // New York | London | Tokyo
```

---

## Tóm tắt các methods quan trọng

| Method | Mô tả | Ví dụ |
|--------|-------|-------|
| `length()` | Độ dài String | `"abc".length()` → 3 |
| `charAt(int)` | Ký tự tại vị trí | `"abc".charAt(0)` → 'a' |
| `substring(int, int)` | Cắt String | `"hello".substring(1,4)` → "ell" |
| `contains(String)` | Kiểm tra chứa | `"hello".contains("ll")` → true |
| `equals(String)` | So sánh giá trị | `"a".equals("a")` → true |
| `equalsIgnoreCase(String)` | So sánh bỏ qua case | `"A".equalsIgnoreCase("a")` → true |
| `compareTo(String)` | So sánh thứ tự | `"a".compareTo("b")` → -1 |
| `split(String)` | Tách String | `"a,b".split(",")` → ["a","b"] |
| `replace(char, char)` | Thay thế ký tự | `"hello".replace('l','x')` → "hexxo" |
| `replaceAll(String, String)` | Thay thế tất cả | `"aa".replaceAll("a","b")` → "bb" |
| `toUpperCase()` | Chuyển hoa | `"abc".toUpperCase()` → "ABC" |
| `toLowerCase()` | Chuyển thường | `"ABC".toLowerCase()` → "abc" |
| `trim()` | Xóa whitespace | `" a ".trim()` → "a" |
| `strip()` | Xóa whitespace (Java 11) | `" a ".strip()` → "a" |
| `startsWith(String)` | Bắt đầu bằng | `"hello".startsWith("he")` → true |
| `endsWith(String)` | Kết thúc bằng | `"hello".endsWith("lo")` → true |
| `indexOf(String)` | Vị trí đầu tiên | `"hello".indexOf("l")` → 2 |
| `lastIndexOf(String)` | Vị trí cuối cùng | `"hello".lastIndexOf("l")` → 3 |
| `isEmpty()` | Kiểm tra rỗng | `"".isEmpty()` → true |
| `isBlank()` | Kiểm tra blank (Java 11) | `"  ".isBlank()` → true |
| `concat(String)` | Nối String | `"a".concat("b")` → "ab" |
| `format(String, Object...)` | Format String | `String.format("%s", "hi")` → "hi" |
| `join(String, String...)` | Nối với delimiter | `String.join(",","a","b")` → "a,b" |
| `repeat(int)` | Lặp lại (Java 11) | `"a".repeat(3)` → "aaa" |
| `intern()` | Đưa vào String Pool | `new String("a").intern()` |

---

## Lưu ý quan trọng

- String là immutable, mọi thao tác đều tạo String object mới
- Dùng StringBuilder/StringBuffer cho concatenation trong vòng lặp
- Dùng `equals()` thay vì `==` để so sánh giá trị
- String literal tự động vào String Pool
- `new String()` tạo object mới trong heap
- Method `intern()` để đưa String vào pool
