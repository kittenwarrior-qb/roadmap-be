# Java Exception Handling

> Exception là sự kiện lỗi xảy ra trong quá trình thực thi, làm gián đoạn luồng bình thường của chương trình.  
> Java cung cấp cơ chế xử lý exception theo hướng đối tượng, mạnh mẽ và rõ ràng.

---

## 1. Cách hoạt động

Khi lỗi xảy ra, Java:
1. Tạo **exception object** (chứa thông tin: loại lỗi, dòng xảy ra, call stack)
2. **Throw** (ném) exception object cho JRE xử lý
3. JRE tìm **exception handler** phù hợp theo thứ tự ngược call stack: `C → B → A`
4. Nếu không tìm được handler → chương trình **dừng và in lỗi** ra console

---

## 2. Các Keywords

| Keyword | Mục đích |
|---------|---------|
| `throw` | Ném exception thủ công trong code |
| `throws` | Khai báo method có thể ném exception để caller biết |
| `try-catch` | Bao bọc code có thể lỗi và xử lý exception |
| `finally` | Luôn chạy dù có exception hay không (dùng để giải phóng tài nguyên) |

---

## 3. Ví dụ cơ bản

```java
public static void main(String[] args) throws FileNotFoundException, IOException {
    try {
        testException(-5);   // Ném FileNotFoundException
        testException(-10);  // Không bao giờ chạy tới đây
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        System.out.println("Releasing resources"); // Luôn chạy
    }

    testException(15); // Ném IOException → không được bắt → chương trình dừng
}

public static void testException(int i) throws FileNotFoundException, IOException {
    if (i < 0)  throw new FileNotFoundException("Negative Integer " + i);
    if (i > 10) throw new IOException("Only supported for index 0 to 10");
}
```

**Output:**
```
java.io.FileNotFoundException: Negative Integer -5
    at ...testException(ExceptionHandling.java:24)
Releasing resources
Exception in thread "main" java.io.IOException: Only supported for index 0 to 10
```

**Lưu ý quan trọng:**
- Không thể có `catch` / `finally` mà không có `try`
- `try` phải có ít nhất `catch` hoặc `finally`
- Không được viết code giữa các block `try-catch-finally`
- Có thể có nhiều `catch`, chỉ có thể có **một** `finally`

---

## 4. Cây phân cấp Exception

```
Throwable
├── Error                  ← Lỗi nghiêm trọng, KHÔNG nên bắt
│   ├── OutOfMemoryError
│   └── StackOverflowError
└── Exception
    ├── IOException        ← Checked Exception
    │   └── FileNotFoundException
    └── RuntimeException   ← Unchecked Exception
        ├── NullPointerException
        ├── ArrayIndexOutOfBoundsException
        └── IllegalArgumentException
```

### 3 loại Exception

| Loại | Ví dụ | Bắt buộc xử lý? | Ghi chú |
|------|-------|:---------------:|---------|
| **Error** | `OutOfMemoryError` | ❌ | Lỗi hệ thống — không thể recover |
| **Checked Exception** | `FileNotFoundException` | ✅ | Phải `catch` hoặc `throws` |
| **Runtime Exception** | `NullPointerException` | ❌ | Do lập trình sai — nên tránh bằng code tốt hơn |

---

## 5. Các method hữu ích của `Throwable`

| Method | Mô tả |
|--------|-------|
| `getMessage()` | Trả về message của exception |
| `getLocalizedMessage()` | Trả về message theo locale (subclass có thể override) |
| `getCause()` | Trả về nguyên nhân gốc (hoặc `null`) |
| `toString()` | Tên class + message |
| `printStackTrace()` | In toàn bộ stack trace ra console |

---

## 6. Cải tiến trong Java 7

### Multi-catch – Bắt nhiều exception trong một `catch`

```java
// Trước Java 7 — lặp code
catch (IOException e)  { logger.error(e); throw new MyException(e.getMessage()); }
catch (SQLException e) { logger.error(e); throw new MyException(e.getMessage()); }

// Java 7+ — gọn hơn
catch (IOException | SQLException ex) {
    logger.error(ex);
    throw new MyException(ex.getMessage());
}
```

### Try-with-resources – Tự động đóng tài nguyên

```java
// Trước Java 7 — phải đóng thủ công trong finally
try {
    MyResource mr = new MyResource();
    // ...
} finally {
    mr.close(); // Dễ quên!
}

// Java 7+ — tự động đóng khi thoát try-catch
try (MyResource mr = new MyResource()) {
    System.out.println("Resource auto-closed after this block");
} catch (Exception e) {
    e.printStackTrace();
}
```

---

## 7. Custom Exception

**Tạo exception riêng** khi cần mang thêm thông tin (ví dụ: error code):

```java
// 1. Định nghĩa custom exception
public class MyException extends Exception {
    private String errorCode;

    public MyException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
```

```java
// 2. Sử dụng
private static void processFile(String file) throws MyException {
    if (file == null)
        throw new MyException("File name can't be null", "NULL_FILE_NAME");

    try {
        InputStream fis = new FileInputStream(file);
    } catch (FileNotFoundException e) {
        throw new MyException(e.getMessage(), "FILE_NOT_FOUND_EXCEPTION");
    }
}

// 3. Xử lý theo error code
private static void processErrorCodes(MyException e) throws MyException {
    switch (e.getErrorCode()) {
        case "FILE_NOT_FOUND_EXCEPTION":
            System.out.println("File Not Found, notify user");
            throw e;
        case "FILE_CLOSE_EXCEPTION":
            System.out.println("File Close failed, just log it.");
            break;
        default:
            e.printStackTrace();
    }
}
```

> 💡 Extend `Exception` → Checked (caller bắt buộc xử lý)  
> 💡 Extend `RuntimeException` → Unchecked (không cần khai báo `throws`)

---

## 8. Best Practices

| # | Nguyên tắc | Mô tả |
|---|-----------|-------|
| 1 | **Dùng exception cụ thể** | Throw `FileNotFoundException` thay vì `Exception` — dễ debug hơn |
| 2 | **Throw sớm (Fail-Fast)** | Kiểm tra null/invalid input ngay đầu method, đừng để lỗi sâu trong stack |
| 3 | **Catch muộn** | Chỉ catch khi bạn **thực sự xử lý được**; không thì để caller xử lý |
| 4 | **Đóng resources** | Dùng `finally` hoặc **try-with-resources** (Java 7+) |
| 5 | **Log đầy đủ** | Không được để `catch` block rỗng; luôn log message rõ ràng |
| 6 | **Multi-catch** | Dùng `catch (A \| B ex)` khi xử lý giống nhau — giảm code lặp |
| 7 | **Custom Exception** | Dùng error code để caller xử lý linh hoạt theo từng trường hợp |
| 8 | **Đặt tên đúng** | Custom exception phải kết thúc bằng `Exception` (vd: `PaymentException`) |
| 9 | **Dùng đúng chỗ** | Exception tốn hiệu năng — đôi khi trả về `boolean` tốt hơn |
| 10 | **Document** | Dùng `@throws` trong Javadoc để mô tả exception method có thể ném |

---

## 9. Tóm tắt nhanh

```
try {
    // Code có thể lỗi
} catch (SpecificException e) {
    // Xử lý lỗi cụ thể
} catch (IOException | SQLException e) {
    // Bắt nhiều loại cùng lúc (Java 7+)
} finally {
    // Luôn chạy — dùng để dọn dẹp
}
```

```
// Khai báo method ném exception
public void method() throws IOException { ... }

// Ném exception thủ công
throw new IllegalArgumentException("Invalid input");
```