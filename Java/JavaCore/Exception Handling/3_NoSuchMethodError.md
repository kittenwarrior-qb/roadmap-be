# NoSuchMethodError

> `NoSuchMethodError` là **Error** (không phải Exception) xảy ra tại **runtime** khi JVM không tìm thấy một method đã được gọi.  
> Thường gặp khi có **sự không tương thích giữa các version của thư viện** (binary incompatibility).

---

## 1. Tại sao xảy ra?

`NoSuchMethodError` **không phải lỗi lập trình thông thường** — hầu hết do:

| Nguyên nhân | Mô tả |
|------------|-------|
| **Version conflict** | Code được compile với version A của thư viện, nhưng runtime lại dùng version B (cũ hơn hoặc khác API) |
| **Classpath sai** | Nhiều phiên bản JAR cùng tồn tại trên classpath → JVM load nhầm bản cũ |
| **Compile không đồng bộ** | Class A gọi method của class B, nhưng class B đã bị sửa/xoá method và chưa recompile lại A |
| **Reflection sai tên method** | Dùng `Class.getMethod("methodName")` với tên method không tồn tại |

---

## 2. So sánh với `NoSuchMethodException`

| | `NoSuchMethodError` | `NoSuchMethodException` |
|-|--------------------|-----------------------|
| Loại | `Error` (extends `LinkageError`) | `Exception` (Checked) |
| Khi xảy ra | Runtime — JVM không tìm được method | Runtime — dùng **Reflection** và method không tồn tại |
| Cần `catch`? | ❌ Không (không nên) | ✅ Phải bắt khi dùng Reflection |
| Nguyên nhân chính | Classpath / version conflict | Gọi `getMethod("wrongName")` |

---

## 3. Ví dụ minh hoạ

### Trường hợp 1: Version conflict

```java
// LibraryV1.jar — phiên bản cũ
public class Calculator {
    public int add(int a, int b) { return a + b; }
}

// LibraryV2.jar — phiên bản mới, method bị đổi tên
public class Calculator {
    public int sum(int a, int b) { return a + b; } // "add" không còn nữa
}
```

```java
// Code của bạn được compile với V2 (dùng sum())
// Nhưng runtime lại load V1 trên classpath → "sum" không tồn tại trong V1
calculator.sum(1, 2); // ❌ NoSuchMethodError
```

### Trường hợp 2: Chưa recompile sau khi sửa class

```java
// Bước 1: ClassB có method doSomething()
public class ClassB {
    public void doSomething() { ... }
}

// Bước 2: Sửa ClassB — đổi tên thành doSomethingElse()
public class ClassB {
    public void doSomethingElse() { ... } // "doSomething" đã bị xoá
}

// Bước 3: ClassA chưa được recompile → vẫn gọi doSomething() cũ
public class ClassA {
    ClassB b = new ClassB();
    b.doSomething(); // ❌ NoSuchMethodError tại runtime
}
```

### Trường hợp 3: Reflection

```java
try {
    Class<?> clazz = Class.forName("com.example.MyClass");
    Method m = clazz.getMethod("nonExistentMethod"); // ❌ NoSuchMethodException
} catch (NoSuchMethodException e) {
    System.out.println("Method không tồn tại: " + e.getMessage());
}
```

---

## 4. Stack trace điển hình

```
Exception in thread "main" java.lang.NoSuchMethodError:
    com.example.Calculator.sum(II)I
    at com.example.Main.main(Main.java:10)
```

**Cách đọc:**
- `com.example.Calculator` → class chứa method bị thiếu
- `sum` → tên method
- `(II)I` → method descriptor: nhận 2 `int`, trả về `int`
- `Main.java:10` → nơi gọi method trong code của bạn

---

## 5. Cách phát hiện & fix

### Bước 1: Đọc stack trace

Xác định method nào bị thiếu và class nào chứa nó.

### Bước 2: Kiểm tra classpath

```bash
# Kiểm tra JAR nào đang được load
mvn dependency:tree          # Maven
gradle dependencies          # Gradle
```

Tìm xem có **nhiều phiên bản** của cùng một thư viện không:

```
[INFO] +- com.example:lib:jar:1.0.0
[INFO] \- com.other:other-lib:jar:2.0.0
[INFO]    \- com.example:lib:jar:0.9.0  ← ⚠️ Conflict!
```

### Bước 3: Loại trừ dependency conflict (Maven)

```xml
<dependency>
    <groupId>com.other</groupId>
    <artifactId>other-lib</artifactId>
    <version>2.0.0</version>
    <exclusions>
        <exclusion>
            <groupId>com.example</groupId>
            <artifactId>lib</artifactId>  <!-- Loại bỏ bản cũ -->
        </exclusion>
    </exclusions>
</dependency>
```

### Bước 4: Recompile toàn bộ project

```bash
mvn clean install      # Maven
gradle clean build     # Gradle
```

> 💡 **Clean build** giúp đảm bảo tất cả class được biên dịch lại từ đầu, tránh class cũ còn sót trong `target/`.

---

## 6. Phòng tránh

| Cách | Mô tả |
|------|-------|
| **Quản lý dependency chặt** | Dùng Maven/Gradle để kiểm soát version, tránh conflict |
| **Clean build thường xuyên** | Đặc biệt sau khi thay đổi API của class |
| **Đồng bộ version** | Luôn compile và chạy với cùng một version của thư viện |
| **Tránh hardcode tên method** | Khi dùng Reflection, kiểm tra method tồn tại trước |
| **Unit test** | Viết test để phát hiện sớm sự thay đổi API |

---

## 7. Tóm tắt

```
NoSuchMethodError
├── Không phải lỗi code thông thường
├── Thường do: version conflict hoặc chưa recompile
├── Xuất hiện tại runtime, không tại compile time
└── Fix: kiểm tra classpath, loại trừ conflict, clean build
```

> ⚠️ **Không nên dùng `try-catch` để bắt `Error`** — hãy fix nguyên nhân gốc rễ thay vì che giấu lỗi.
