# Java Autoboxing & Unboxing

## Khái Niệm

Autoboxing và Unboxing là cơ chế tự động chuyển đổi giữa primitive types và wrapper classes trong Java.

---

## Hai Nhóm Kiểu Dữ Liệu

### 1. Primitive Types (Kiểu Nguyên Thủy)
```java
int, double, boolean, char, long, float, short, byte
```

### 2. Wrapper Classes (Kiểu Object)

| Primitive | Wrapper Class |
|-----------|---------------|
| int | Integer |
| double | Double |
| boolean | Boolean |
| char | Character |
| long | Long |
| float | Float |
| short | Short |
| byte | Byte |

---

## 1. Autoboxing

### Định Nghĩa
**Autoboxing** = Tự động chuyển primitive → wrapper object

### Ví Dụ Cơ Bản
```java
// Autoboxing: int -> Integer
Integer a = 10;

// Thực chất Java ngầm hiểu là:
Integer a = Integer.valueOf(10);
```

### Ví Dụ Với Các Types Khác
```java
// int -> Integer
Integer num = 100;

// double -> Double
Double price = 99.99;

// boolean -> Boolean
Boolean isActive = true;

// char -> Character
Character letter = 'A';
```


### Autoboxing trong Method
```java
public void printNumber(Integer num) {
    System.out.println("Number: " + num);
}

// Gọi method với primitive
printNumber(42);  // Autoboxing: int -> Integer
```

---

## 2. Unboxing

### Định Nghĩa
**Unboxing** = Tự động chuyển wrapper → primitive

### Ví Dụ Cơ Bản
```java
Integer a = 10;
int b = a;   // Unboxing: Integer -> int

// Thực chất Java làm:
int b = a.intValue();
```

### Ví Dụ Với Các Types Khác
```java
// Integer -> int
Integer num = 100;
int primitiveNum = num;

// Double -> double
Double price = 99.99;
double primitivePrice = price;

// Boolean -> boolean
Boolean isActive = true;
boolean primitiveActive = isActive;

// Character -> char
Character letter = 'A';
char primitiveLetter = letter;
```

### Unboxing trong Phép Toán
```java
Integer a = 10;
Integer b = 20;

// Unboxing tự động trong phép toán
int sum = a + b;  // a và b được unbox thành int
System.out.println(sum);  // 30
```

---

## Ví Dụ Thực Tế với ArrayList

### Tại Sao Cần Autoboxing?
ArrayList chỉ chứa objects, không thể chứa primitive types.

```java
import java.util.ArrayList;

// ✗ Sai: Không thể dùng primitive
// ArrayList<int> list = new ArrayList<>();  // Compile error!

// ✓ Đúng: Phải dùng wrapper class
ArrayList<Integer> list = new ArrayList<>();

// Autoboxing: int -> Integer
list.add(5);
list.add(10);
list.add(15);

// Unboxing: Integer -> int
int first = list.get(0);  // 5
int second = list.get(1); // 10

System.out.println("First: " + first);   // 5
System.out.println("Second: " + second); // 10
```

### Ví Dụ Với Loop
```java
ArrayList<Integer> numbers = new ArrayList<>();

// Autoboxing trong loop
for (int i = 1; i <= 5; i++) {
    numbers.add(i);  // int -> Integer
}

// Unboxing trong loop
for (Integer num : numbers) {
    int value = num;  // Integer -> int
    System.out.println(value * 2);
}
```


---

## NullPointerException - Cẩn Thận!

### Vấn Đề
Đây là lỗi RẤT HAY GẶP khi làm việc với wrapper classes.

```java
Integer a = null;
int b = a;   // ❌ NullPointerException!
```

### Tại Sao Lỗi?
1. `a` là `null`
2. Java cố unbox → gọi `a.intValue()`
3. Nhưng `null` không có method → crash!

### Ví Dụ Chi Tiết
```java
Integer score = null;

// ❌ Lỗi: NullPointerException
// int finalScore = score;

// ✓ Đúng: Kiểm tra null trước
int finalScore = (score != null) ? score : 0;
System.out.println("Final score: " + finalScore);  // 0
```

### Ví Dụ Với ArrayList
```java
ArrayList<Integer> list = new ArrayList<>();
list.add(null);  // OK: ArrayList chấp nhận null

// ❌ Lỗi khi unbox
// int value = list.get(0);  // NullPointerException!

// ✓ Đúng: Kiểm tra null
Integer value = list.get(0);
if (value != null) {
    int primitiveValue = value;
    System.out.println(primitiveValue);
} else {
    System.out.println("Value is null");
}
```

### Best Practice
```java
// ✓ Luôn kiểm tra null khi unbox
Integer num = getNumber();  // Có thể return null

if (num != null) {
    int value = num;  // An toàn
    // Xử lý value
} else {
    // Xử lý trường hợp null
}
```

---

## So Sánh Nhanh

| | Autoboxing | Unboxing |
|---|------------|----------|
| **Chuyển đổi** | primitive → wrapper | wrapper → primitive |
| **Ví dụ** | `Integer x = 5;` | `int y = x;` |
| **Thực chất** | `Integer.valueOf()` | `intValue()` |
| **Nguy cơ** | Không có | NullPointerException |

---

## Khi Nào Dùng Primitive vs Wrapper?

### Dùng Primitive Khi:
1. **Tính toán nhiều** - Performance tốt hơn
2. **Muốn nhanh hơn** - Primitive nhanh hơn object
3. **Không cần null** - Primitive không thể null

```java
// ✓ Tốt cho tính toán
int sum = 0;
for (int i = 0; i < 1000000; i++) {
    sum += i;  // Nhanh
}
```

### Dùng Wrapper Khi:
1. **Làm việc với Collections** - ArrayList, HashMap
2. **Cần dùng null** - Biểu thị "không có giá trị"
3. **Dùng Generics** - `<T>` không chấp nhận primitive

```java
// ✓ Cần wrapper cho ArrayList
ArrayList<Integer> list = new ArrayList<>();

// ✓ Cần null để biểu thị "chưa có giá trị"
Integer age = null;  // Chưa nhập tuổi

// ✓ Cần wrapper cho Generic
public <T> void print(T value) {
    System.out.println(value);
}
print(42);  // Autoboxing: int -> Integer
```


---

## Integer Cache - Phần Dễ Ra Đề Thi!

### Khái Niệm
Java cache (lưu trữ) các Integer objects từ **-128 đến 127** để tối ưu memory.

### Ví Dụ Với Số Nhỏ (-128 đến 127)
```java
Integer a = 100;
Integer b = 100;

System.out.println(a == b);        // true ✓
System.out.println(a.equals(b));   // true ✓
```

**Giải thích:**
- `100` nằm trong cache range (-128 đến 127)
- `a` và `b` tham chiếu đến **CÙNG 1 object** trong cache
- `==` so sánh địa chỉ → true

### Ví Dụ Với Số Lớn (Ngoài -128 đến 127)
```java
Integer a = 1000;
Integer b = 1000;

System.out.println(a == b);        // false ✗
System.out.println(a.equals(b));   // true ✓
```

**Giải thích:**
- `1000` nằm ngoài cache range
- `a` và `b` là **2 objects KHÁC NHAU**
- `==` so sánh địa chỉ → false
- `.equals()` so sánh giá trị → true

### Minh Họa Bằng Hình
```
Cache Range: -128 đến 127
┌─────────────────────────────┐
│  Integer Cache (Shared)     │
│  -128, -127, ..., 0, ..., 127│
└─────────────────────────────┘
       ↑           ↑
       a           b
   (cùng object)

Ngoài Cache: < -128 hoặc > 127
┌──────────┐    ┌──────────┐
│ Integer  │    │ Integer  │
│  1000    │    │  1000    │
└──────────┘    └──────────┘
      ↑              ↑
      a              b
  (khác object)
```

### Ví Dụ Chi Tiết
```java
// Trong cache range
Integer x1 = 127;
Integer x2 = 127;
System.out.println(x1 == x2);  // true

Integer y1 = -128;
Integer y2 = -128;
System.out.println(y1 == y2);  // true

// Ngoài cache range
Integer z1 = 128;
Integer z2 = 128;
System.out.println(z1 == z2);  // false

Integer w1 = -129;
Integer w2 = -129;
System.out.println(w1 == w2);  // false
```


---

## So Sánh == vs .equals()

### Với Primitive Types
```java
int a = 100;
int b = 100;

System.out.println(a == b);  // true
// Primitive: == so sánh GIÁ TRỊ
```

### Với Wrapper Classes

#### Trường Hợp 1: Trong Cache (-128 đến 127)
```java
Integer a = 100;
Integer b = 100;

System.out.println(a == b);        // true (cùng object)
System.out.println(a.equals(b));   // true (cùng giá trị)
```

#### Trường Hợp 2: Ngoài Cache
```java
Integer a = 1000;
Integer b = 1000;

System.out.println(a == b);        // false (khác object)
System.out.println(a.equals(b));   // true (cùng giá trị)
```

#### Trường Hợp 3: Dùng new
```java
Integer a = new Integer(100);  // Tạo object mới
Integer b = new Integer(100);  // Tạo object mới

System.out.println(a == b);        // false (khác object)
System.out.println(a.equals(b));   // true (cùng giá trị)
```

### Quy Tắc Vàng
```java
// ✗ KHÔNG dùng == để so sánh wrapper objects
Integer a = 1000;
Integer b = 1000;
if (a == b) { }  // Sai! Không đáng tin cậy

// ✓ LUÔN dùng .equals() để so sánh giá trị
if (a.equals(b)) { }  // Đúng!

// ✓ Hoặc unbox về primitive
if (a.intValue() == b.intValue()) { }  // Đúng!
```

---

## Câu Hỏi Phỏng Vấn Thường Gặp

### Câu 1: Kết quả là gì?
```java
Integer a = 127;
Integer b = 127;
System.out.println(a == b);
```

**Đáp án:** `true`
**Giải thích:** 127 nằm trong cache range (-128 đến 127)

### Câu 2: Kết quả là gì?
```java
Integer a = 128;
Integer b = 128;
System.out.println(a == b);
```

**Đáp án:** `false`
**Giải thích:** 128 nằm ngoài cache range, tạo 2 objects khác nhau

### Câu 3: Kết quả là gì?
```java
Integer a = null;
int b = a;
```

**Đáp án:** `NullPointerException`
**Giải thích:** Không thể unbox null thành primitive

### Câu 4: Kết quả là gì?
```java
Integer a = 100;
Integer b = 100;
Integer c = new Integer(100);

System.out.println(a == b);
System.out.println(a == c);
System.out.println(a.equals(c));
```

**Đáp án:**
```
true   (a và b cùng object trong cache)
false  (c là object mới, không dùng cache)
true   (cùng giá trị)
```


---

## Performance Considerations

### Primitive vs Wrapper Performance
```java
// Test 1: Primitive (Nhanh)
long start = System.currentTimeMillis();
int sum = 0;
for (int i = 0; i < 10000000; i++) {
    sum += i;
}
long end = System.currentTimeMillis();
System.out.println("Primitive: " + (end - start) + "ms");

// Test 2: Wrapper (Chậm hơn)
start = System.currentTimeMillis();
Integer sumWrapper = 0;
for (int i = 0; i < 10000000; i++) {
    sumWrapper += i;  // Autoboxing/Unboxing mỗi lần
}
end = System.currentTimeMillis();
System.out.println("Wrapper: " + (end - start) + "ms");
```

**Kết quả:** Primitive nhanh hơn 5-10 lần!

### Memory Usage
```java
// Primitive: 4 bytes
int x = 100;

// Wrapper: 16 bytes (object overhead + value)
Integer y = 100;
```

### Best Practice
```java
// ✗ Tránh: Autoboxing/Unboxing không cần thiết
Integer sum = 0;
for (int i = 0; i < 1000; i++) {
    sum += i;  // Chậm: unbox, tính toán, autobox
}

// ✓ Tốt hơn: Dùng primitive cho tính toán
int sum = 0;
for (int i = 0; i < 1000; i++) {
    sum += i;  // Nhanh: chỉ tính toán
}
Integer result = sum;  // Autobox 1 lần cuối
```

---

## Ví Dụ Thực Tế

### Example 1: Tính Tổng Từ ArrayList
```java
ArrayList<Integer> numbers = new ArrayList<>();
numbers.add(10);
numbers.add(20);
numbers.add(30);

// Tính tổng
int sum = 0;
for (Integer num : numbers) {
    sum += num;  // Unboxing: Integer -> int
}

System.out.println("Sum: " + sum);  // 60
```

### Example 2: HashMap với Integer Key
```java
import java.util.HashMap;

HashMap<Integer, String> map = new HashMap<>();

// Autoboxing: int -> Integer
map.put(1, "One");
map.put(2, "Two");
map.put(3, "Three");

// Unboxing khi get
String value = map.get(2);  // 2 được autobox thành Integer
System.out.println(value);  // "Two"
```

### Example 3: Optional với Integer
```java
import java.util.Optional;

Optional<Integer> optionalNum = Optional.of(42);

// Unboxing an toàn
int value = optionalNum.orElse(0);
System.out.println(value);  // 42

// Xử lý null
Optional<Integer> emptyOptional = Optional.empty();
int defaultValue = emptyOptional.orElse(0);
System.out.println(defaultValue);  // 0
```


---

## Common Mistakes

### 1. So Sánh Wrapper Bằng ==
```java
// ✗ Sai
Integer a = 1000;
Integer b = 1000;
if (a == b) {  // false! Không như mong đợi
    System.out.println("Equal");
}

// ✓ Đúng
if (a.equals(b)) {  // true
    System.out.println("Equal");
}
```

### 2. Unbox Null
```java
// ✗ Sai
Integer num = null;
int value = num;  // NullPointerException!

// ✓ Đúng
Integer num = null;
int value = (num != null) ? num : 0;
```

### 3. Lạm Dụng Wrapper Trong Loop
```java
// ✗ Sai: Chậm
Integer sum = 0;
for (int i = 0; i < 1000000; i++) {
    sum += i;  // Autobox/Unbox mỗi lần
}

// ✓ Đúng: Nhanh
int sum = 0;
for (int i = 0; i < 1000000; i++) {
    sum += i;
}
```

### 4. Quên Kiểm Tra Null Trong Collection
```java
// ✗ Sai
ArrayList<Integer> list = new ArrayList<>();
list.add(null);
int value = list.get(0);  // NullPointerException!

// ✓ Đúng
Integer value = list.get(0);
if (value != null) {
    int primitiveValue = value;
}
```

---

## Tổng Kết

### Key Points

1. **Autoboxing:** primitive → wrapper (tự động)
   ```java
   Integer x = 5;  // int -> Integer
   ```

2. **Unboxing:** wrapper → primitive (tự động)
   ```java
   int y = x;  // Integer -> int
   ```

3. **Integer Cache:** -128 đến 127 được cache
   ```java
   Integer a = 100;
   Integer b = 100;
   a == b;  // true (cùng object)
   
   Integer c = 1000;
   Integer d = 1000;
   c == d;  // false (khác object)
   ```

4. **NullPointerException:** Cẩn thận khi unbox null
   ```java
   Integer x = null;
   int y = x;  // Crash!
   ```

5. **So sánh:** Dùng `.equals()` cho wrapper
   ```java
   Integer a = 1000;
   Integer b = 1000;
   a.equals(b);  // true ✓
   a == b;       // false ✗
   ```

### Best Practices

1. ✓ Dùng primitive cho tính toán (nhanh hơn)
2. ✓ Dùng wrapper cho Collections và Generics
3. ✓ Luôn dùng `.equals()` để so sánh wrapper
4. ✓ Kiểm tra null trước khi unbox
5. ✓ Tránh autoboxing/unboxing không cần thiết trong loop

### Remember

- **Autoboxing/Unboxing** giúp code ngắn gọn hơn
- **Integer Cache** là trick hay gặp trong phỏng vấn
- **NullPointerException** là lỗi phổ biến nhất
- **Performance:** Primitive > Wrapper cho tính toán
- **Collections:** Chỉ dùng được Wrapper, không dùng được Primitive
