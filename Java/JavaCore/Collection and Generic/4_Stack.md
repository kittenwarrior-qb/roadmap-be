# Java Heap Space vs Stack Memory

> Hiểu rõ Heap và Stack giúp viết code Java hiệu quả hơn và tránh các lỗi `OutOfMemoryError` / `StackOverflowError`.

---

## 1. Tổng quan

| Tiêu chí             | Heap Memory                            | Stack Memory                          |
|---------------------|----------------------------------------|---------------------------------------|
| Dùng để             | Lưu **Objects** và JRE classes         | Lưu **local variables** và method calls |
| Phạm vi truy cập    | Toàn bộ ứng dụng (global)             | Chỉ trong thread đang chạy            |
| Thứ tự quản lý      | Phức tạp (Young/Old Generation)        | **LIFO** (Last-In-First-Out)          |
| Vòng đời            | Từ lúc tạo đến khi GC thu hồi         | Tồn tại trong vòng đời của method     |
| Kích thước          | Lớn hơn nhiều                          | Nhỏ, nhưng nhanh hơn                  |
| Lỗi khi đầy         | `OutOfMemoryError: Java Heap Space`    | `StackOverflowError`                  |
| Cấu hình JVM        | `-Xms` (min), `-Xmx` (max)            | `-Xss`                                |

---

## 2. Ví dụ minh hoạ

```java
package com.journaldev.test;

public class Memory {

    public static void main(String[] args) { // Line 1
        int i = 1;                           // Line 2
        Object obj = new Object();           // Line 3
        Memory mem = new Memory();           // Line 4
        mem.foo(obj);                        // Line 5
    }                                        // Line 9

    private void foo(Object param) {         // Line 6
        String str = param.toString();       // Line 7
        System.out.println(str);
    }                                        // Line 8
}
```

---

## 3. Phân tích từng bước

| Bước | Dòng | Sự kiện | Heap | Stack |
|------|------|---------|------|-------|
| 1 | – | JVM load | Runtime classes | – |
| 2 | Line 1 | `main()` bắt đầu | – | Tạo stack frame cho `main()` |
| 3 | Line 2 | `int i = 1` | – | `i` lưu trực tiếp trong stack |
| 4 | Line 3 | `new Object()` | Object được tạo trong heap | Stack lưu **reference** tới heap |
| 5 | Line 4 | `new Memory()` | Memory object trong heap | Stack lưu reference `mem` |
| 6 | Line 5-6 | Gọi `foo()` | – | Tạo stack frame mới cho `foo()`, tạo **copy reference** `param` |
| 7 | Line 7 | `param.toString()` | String trong **String Pool** (heap) | Stack lưu reference `str` |
| 8 | Line 8 | `foo()` kết thúc | – | Stack frame của `foo()` bị giải phóng |
| 9 | Line 9 | `main()` kết thúc | GC dọn dẹp | Stack của `main()` bị huỷ |

---

## 4. Điểm khác biệt quan trọng

- **Objects** → luôn nằm trong **Heap**, kể cả khi tạo bên trong method.
- **Primitive variables** (`int`, `boolean`,...) → nằm trong **Stack**.
- **Reference variables** → nằm trong **Stack**, nhưng trỏ tới object ở **Heap**.
- Heap chia thành **Young Generation** và **Old Generation** → Garbage Collector xử lý.
- Stack không cần GC — tự giải phóng khi method kết thúc → **nhanh hơn Heap**.
- Java là **pass-by-value** → khi truyền object vào method, một **copy của reference** được tạo trong stack của method đó.