# NullPointerException (NPE)

> `NullPointerException` là **Runtime Exception** — xảy ra khi cố dùng một object nhưng nó đang là `null`.  
> Không cần `try-catch` bắt buộc, nhưng cần phòng tránh chủ động.

---

## 1. Các nguyên nhân thường gặp

| # | Tình huống | Ví dụ |
|---|-----------|-------|
| 1 | Gọi method trên object null | `t.foo()` khi `t == null` |
| 2 | Truy cập field của object null | `t.x` khi `t == null` |
| 3 | Truyền `null` làm argument | `foo(null)` |
| 4 | `throw null` | `throw null;` |
| 5 | Truy cập `array.length` hay `array[i]` khi array là null | `data.length` khi `data == null` |
| 6 | `synchronized` trên null object | `synchronized(mutex)` khi `mutex == null` |

---

## 2. Ví dụ từng trường hợp

### ① Gọi method trên null object

```java
Temp t = initT();   // trả về null
t.foo("Hi");        // ❌ NullPointerException
```

### ② Truy cập field của null object

```java
Temp t = initT();   // trả về null
int i = t.x;        // ❌ NullPointerException
```

### ③ Truyền null vào method

```java
foo(null);          // caller truyền null

public static void foo(String s) {
    s.toLowerCase(); // ❌ NullPointerException — s là null
}
```

### ④ `throw null`

```java
throw null;         // ❌ NullPointerException
```

### ⑤ Truy cập null array

```java
int[] data = null;
int len = data.length; // ❌ NullPointerException
int val = data[2];     // ❌ NullPointerException
```

### ⑥ `synchronized` trên null

```java
public static String mutex = null;
synchronized(mutex) { ... } // ❌ NullPointerException
```

### ⑦ HTTP 500 – NPE trong Web Application

```java
// user.getUserId() trả về null → gọi .toLowerCase() → NPE
System.out.println(user.getUserId().toLowerCase()); // ❌
```

---

## 3. Cách phát hiện

Đọc **stack trace** — nó chỉ thẳng class và dòng xảy ra lỗi:

```
Exception in thread "main" java.lang.NullPointerException
    at Temp.main(Temp.java:7)   ← xem dòng này
```

→ Mở file, tìm dòng 7, xác định biến nào đang là `null`.

---

## 4. Cách fix và phòng tránh

### Null check cơ bản

```java
if (mutex == null) mutex = "";
synchronized(mutex) { ... } // ✅ An toàn

if (user != null && user.getUserId() != null) {
    System.out.println(user.getUserId().toLowerCase()); // ✅
}
```

### So sánh literal trước (tránh NPE khi s là null)

```java
// ❌ Dễ NPE nếu s == null
if (s.equals("Test")) { ... }

// ✅ Literal đứng trước — không bao giờ NPE
if ("Test".equals(s)) { ... }
```

### Throw sớm với `IllegalArgumentException`

```java
public int getArrayLength(Object[] array) {
    if (array == null) throw new IllegalArgumentException("array is null");
    return array.length;
}
```

### Dùng ternary operator

```java
String msg = (str == null) ? "" : str.substring(0, str.length() - 1);
```

### Dùng `String.valueOf()` thay vì `toString()`

```java
Object obj = null;

System.out.println(String.valueOf(obj)); // ✅ In "null"
System.out.println(obj.toString());      // ❌ NullPointerException
```

### Trả về empty object thay vì null

```java
// ❌ Dễ gây NPE cho caller
public List<String> getNames() { return null; }

// ✅ Trả về empty list — caller không cần null check
public List<String> getNames() { return Collections.emptyList(); }
```

### Dùng method của Collections

```java
if (map.containsKey("key")) { ... }      // ✅ Thay vì map.get("key") != null
if (list.contains(element)) { ... }      // ✅
```

---

## 5. Tóm tắt Best Practices

| Nguyên tắc | Cách làm |
|-----------|---------|
| Kiểm tra null trước khi dùng | `if (obj != null) obj.method()` |
| Literal đứng trước khi so sánh | `"value".equals(str)` |
| Trả về empty thay vì null | `return Collections.emptyList()` |
| Dùng `String.valueOf()` | Tránh NPE khi print object |
| Throw sớm nếu input null | `if (x == null) throw new IllegalArgumentException(...)` |
| Dùng `containsKey()` / `contains()` | Trước khi truy cập map/list |