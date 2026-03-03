# String Immutability và String Pool trong Java

## Tại sao String là Immutable?

### 5 lý do chính:

1. **String Pool** - Tiết kiệm bộ nhớ
   - Nhiều biến có thể tham chiếu đến cùng một String object
   - Không thể thực hiện nếu String có thể thay đổi

2. **Bảo mật** - An toàn dữ liệu nhạy cảm
   - Database username/password
   - Socket programming (host, port)
   - Hacker không thể thay đổi giá trị đã tham chiếu

3. **Thread-safe** - An toàn đa luồng
   - Không cần synchronization
   - Một String instance có thể share giữa nhiều threads

4. **ClassLoader** - Đảm bảo load đúng class
   - VD: `java.sql.Connection` không bị thay đổi thành `myhacked.Connection`

5. **HashMap key hiệu quả**
   - Hashcode được cache khi tạo
   - Xử lý nhanh hơn các key objects khác

---

## String Pool

### Khái niệm:
- Vùng nhớ đặc biệt trong Java Heap Memory
- Lưu trữ các String objects
- Áp dụng Flyweight design pattern

### Cách hoạt động:

```java
String s1 = "Cat";              // Tạo trong pool
String s2 = "Cat";              // Tham chiếu đến s1
String s3 = new String("Cat");  // Tạo mới trong heap

System.out.println(s1 == s2);   // true
System.out.println(s1 == s3);   // false
```

**Literal (`"Cat"`):**
- JVM kiểm tra pool trước
- Nếu có → trả về reference
- Nếu không → tạo mới trong pool

**New operator (`new String("Cat")`):**
- Tạo object mới trong heap space
- Không lưu vào pool
- Dùng `intern()` để đưa vào pool

### Câu hỏi phỏng vấn:
```java
String str = new String("Cat");
```
**Tạo bao nhiêu String objects?**
- Nếu "Cat" đã có trong pool → 1 object (str)
- Nếu "Cat" chưa có → 2 objects (1 trong pool + 1 trong heap)

---

## Câu hỏi phỏng vấn phổ biến

### 1. String là gì?
- Class trong `java.lang` package
- Không phải primitive data type
- Immutable và final

### 2. Cách tạo String?
```java
String str = new String("abc");  // Dùng new operator
String str1 = "abc";             // Dùng literal
```

### 3. Kiểm tra Palindrome?
```java
// Cách 1: Dùng StringBuilder
private static boolean isPalindrome(String str) {
    if (str == null) return false;
    StringBuilder strBuilder = new StringBuilder(str);
    strBuilder.reverse();
    return strBuilder.toString().equals(str);
}

// Cách 2: So sánh ký tự
private static boolean isPalindromeString(String str) {
    if (str == null) return false;
    int length = str.length();
    for (int i = 0; i < length / 2; i++) {
        if (str.charAt(i) != str.charAt(length - i - 1))
            return false;
    }
    return true;
}
```

### 4. Xóa ký tự khỏi String?
```java
private static String removeChar(String str, char c) {
    if (str == null) return null;
    return str.replaceAll(Character.toString(c), "");
}
```

### 5. So sánh String?
- `compareTo()` - So sánh lexicographically
- `compareToIgnoreCase()` - Bỏ qua case
- `equals()` - So sánh giá trị
- `equalsIgnoreCase()` - So sánh giá trị, bỏ qua case

### 6. String vs StringBuffer vs StringBuilder?

| Đặc điểm | String | StringBuffer | StringBuilder |
|----------|--------|--------------|---------------|
| Immutable | Yes | No | No |
| Thread-safe | Yes | Yes | No |
| Performance | Chậm | Trung bình | Nhanh |
| Sử dụng | Single/Multi-thread | Multi-thread | Single-thread |

### 7. Tại sao char[] tốt hơn String cho password?
- String immutable → tồn tại trong memory lâu
- Rủi ro bảo mật: memory dump có thể lộ password
- char[] có thể xóa ngay sau khi dùng

### 8. So sánh String: == vs equals()?
```java
String s1 = "abc";
String s2 = "abc";
String s3 = new String("abc");

s1 == s2;        // true (cùng reference)
s1 == s3;        // false (khác reference)
s1.equals(s3);   // true (cùng giá trị)
```

### 9. intern() method?
- Đưa String vào pool
- Trả về reference nếu đã tồn tại
- Đảm bảo String từ pool unique

### 10. Tại sao String là HashMap key tốt?
- Immutable
- Hashcode được cache
- Xử lý nhanh hơn

---

## Bài tập đoán Output

### Bài 1:
```java
String s1 = new String("digitalocean");
String s2 = new String("DIGITALOCEAN");
System.out.println(s1 = s2);
```
**Output:** `DIGITALOCEAN` (phép gán, không phải so sánh)

### Bài 2:
```java
public void foo(String s) { System.out.println("String"); }
public void foo(StringBuffer sb) { System.out.println("StringBuffer"); }

new Test().foo(null);
```
**Output:** Compile error (ambiguous method call)

### Bài 3:
```java
String s1 = new String("abc");
String s2 = new String("abc");
System.out.println(s1 == s2);
```
**Output:** `false` (khác reference)

### Bài 4:
```java
String s1 = "abc";
StringBuffer s2 = new StringBuffer(s1);
System.out.println(s1.equals(s2));
```
**Output:** `false` (khác class type)

### Bài 5:
```java
String s1 = "abc";
String s2 = new String("abc");
s2.intern();
System.out.println(s1 == s2);
```
**Output:** `false` (s2 vẫn trỏ đến heap object)

### Bài 6:
```java
String s1 = new String("Hello");  
String s2 = new String("Hello");
```
**Tạo bao nhiêu objects?** 
- Nếu "Hello" chưa có: 3 objects (1 pool + 2 heap)
- Nếu "Hello" đã có: 2 objects (2 heap)

---

## Tips quan trọng

- Dùng `equals()` để so sánh giá trị String
- Dùng StringBuilder cho single-thread
- Dùng StringBuffer cho multi-thread
- String literal tự động vào pool
- `new String()` không vào pool (trừ khi dùng `intern()`)
- String là thread-safe vì immutable
- Char array an toàn hơn cho password

---

## Ví dụ thực tế

### Ví dụ 1: String Pool tiết kiệm bộ nhớ
```java
// Tạo 1000 String objects với cùng giá trị
String[] arr1 = new String[1000];
for (int i = 0; i < 1000; i++) {
    arr1[i] = "Java";  // Chỉ 1 object trong pool
}

String[] arr2 = new String[1000];
for (int i = 0; i < 1000; i++) {
    arr2[i] = new String("Java");  // 1000 objects trong heap
}
// arr1 tiết kiệm bộ nhớ hơn nhiều
```

### Ví dụ 2: Bảo mật với String immutable
```java
public void connectDatabase(String username, String password) {
    // Nếu String mutable, hacker có thể thay đổi giá trị
    // sau khi validate nhưng trước khi connect
    validateCredentials(username, password);
    // username và password vẫn giữ nguyên giá trị
    database.connect(username, password);
}
```

### Ví dụ 3: Thread-safe tự nhiên
```java
public class SharedString {
    private String message = "Hello";
    
    // Không cần synchronized vì String immutable
    public String getMessage() {
        return message;
    }
    
    // Thread 1 và Thread 2 có thể gọi getMessage() đồng thời
    // mà không gây ra race condition
}
```

### Ví dụ 4: HashMap key hiệu quả
```java
Map<String, User> userMap = new HashMap<>();

String key1 = "user123";
userMap.put(key1, new User("John"));

// Hashcode của key1 đã được cache
// Tra cứu rất nhanh
User user = userMap.get("user123");
```

### Ví dụ 5: Password với char array
```java
// Không an toàn
String password = "myPassword123";
// password tồn tại trong memory cho đến khi GC chạy

// An toàn hơn
char[] passwordArray = {'m','y','P','a','s','s','w','o','r','d','1','2','3'};
// Sử dụng password
authenticateUser(passwordArray);
// Xóa ngay sau khi dùng
Arrays.fill(passwordArray, '0');
```

### Ví dụ 6: String concatenation
```java
// Không hiệu quả - tạo nhiều String objects
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i;  // Tạo 1000 String objects mới
}

// Hiệu quả - dùng StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);  // Modify cùng 1 object
}
String result = sb.toString();
```

### Ví dụ 7: intern() method
```java
String s1 = "Hello";
String s2 = new String("Hello");
String s3 = new String("Hello").intern();

System.out.println(s1 == s2);  // false
System.out.println(s1 == s3);  // true (s3 trỏ đến pool)

// Khi nào dùng intern()?
// - Khi có nhiều String duplicate
// - Muốn tiết kiệm memory
// - Cần so sánh bằng == thay vì equals()
```
