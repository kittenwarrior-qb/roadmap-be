# String vs StringBuffer vs StringBuilder

> `StringBuffer` vs `StringBuilder` là một câu hỏi phỏng vấn Java phổ biến.

---

## 1. String trong Java

`String` đại diện cho chuỗi ký tự, có thể khởi tạo theo 2 cách:

```java
String str = "ABC";
// hoặc
String str = new String("ABC");
```

**Đặc điểm quan trọng:**

- **Immutable** – không thể thay đổi sau khi tạo → an toàn trong môi trường đa luồng.
- **String Pool** – khi dùng `""`, JVM tìm trong pool trước; nếu có thì trả về reference, không thì tạo mới → tiết kiệm bộ nhớ.
- Dùng `new String(...)` → tạo object trong **heap**, không dùng pool.
- Toán tử `+` được overload, nhưng nội bộ dùng `StringBuffer`/`StringBuilder`.
- Override `equals()` và `hashCode()` – so sánh theo nội dung, phân biệt hoa thường (`equalsIgnoreCase()` nếu không phân biệt).
- Dùng encoding **UTF-16**.
- Là `final class`; hashCode được tính lazy và cache vào field `hash`.

---

## 2. String vs StringBuffer

`String` là immutable → mỗi thao tác như nối chuỗi, substring đều tạo object mới → **tốn bộ nhớ, nhiều garbage**.

`StringBuffer` và `StringBuilder` là **mutable** → hiệu quả hơn cho các thao tác chuỗi.

| Phương thức      | Mô tả               |
|-----------------|---------------------|
| `append()`      | Nối thêm chuỗi      |
| `insert()`      | Chèn chuỗi          |
| `delete()`      | Xoá đoạn chuỗi      |
| `substring()`   | Lấy chuỗi con       |

---

## 3. StringBuffer vs StringBuilder

| Tiêu chí          | `StringBuffer`       | `StringBuilder`      |
|------------------|----------------------|----------------------|
| Ra đời           | Java 1.0             | Java 1.5             |
| Thread-safe      | ✅ (synchronized)    | ❌                   |
| Hiệu năng        | Chậm hơn             | Nhanh hơn            |
| Dùng khi nào     | Đa luồng             | Đơn luồng            |

> `StringBuffer` đồng bộ hoá tất cả public method → an toàn nhưng có **chi phí hiệu năng**.  
> `StringBuilder` được thêm vào Java 1.5 để khắc phục hạn chế đó.

---

## 4. So sánh hiệu năng

```java
package com.journaldev.java;

import java.util.GregorianCalendar;

public class TestString {
    public static void main(String[] args) {
        System.gc();
        long start = new GregorianCalendar().getTimeInMillis();
        long startMemory = Runtime.getRuntime().freeMemory();

        StringBuffer sb = new StringBuffer();
        // StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10_000_000; i++) {
            sb.append(":").append(i);
        }

        long end = new GregorianCalendar().getTimeInMillis();
        long endMemory = Runtime.getRuntime().freeMemory();
        System.out.println("Time Taken: " + (end - start));
        System.out.println("Memory used: " + (startMemory - endMemory));
    }
}
```

**Kết quả (trung bình 5 lần chạy):**

| Số vòng lặp | StringBuffer (ms, bytes) | StringBuilder (ms, bytes) |
|-------------|--------------------------|---------------------------|
| 1,000,000   | 808 ms – 149,356,704     | 633 ms – 149,356,704      |
| 10,000,000  | 7448 ms – 147,783,888    | 6179 ms – 147,783,888     |

→ `StringBuilder` nhanh hơn ngay cả trong môi trường đơn luồng do không có overhead của synchronization.

---

## 5. Tổng kết

| Đặc điểm                | `String` | `StringBuffer` | `StringBuilder` |
|------------------------|----------|----------------|-----------------|
| Mutable                | ❌       | ✅             | ✅              |
| Thread-safe            | ✅       | ✅             | ❌              |
| Hiệu năng              | Thấp nhất| Trung bình     | Cao nhất        |
| Toán tử `+` sử dụng   | –        | ✅ (nội bộ)   | ✅ (nội bộ)    |

**Khi nào dùng gì?**
- **Đa luồng** → `StringBuffer`
- **Đơn luồng** → `StringBuilder`
- **Không thao tác nhiều** → `String`