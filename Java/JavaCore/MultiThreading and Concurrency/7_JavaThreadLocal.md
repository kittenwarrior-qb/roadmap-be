# Java ThreadLocal

> `ThreadLocal` cho phép mỗi thread có **bản sao riêng** của một biến — không cần `synchronized`.  
> Dùng khi muốn **tránh chia sẻ state** giữa các thread thay vì đồng bộ hoá nó.

---

## 1. ThreadLocal vs Synchronized

| Tiêu chí | `synchronized` | `ThreadLocal` |
|---------|---------------|--------------|
| Cách tiếp cận | Chỉ 1 thread dùng biến tại một thời điểm | Mỗi thread có **bản sao riêng** |
| Hiệu năng | Thấp hơn (blocking) | Cao hơn (không lock) |
| Dùng khi | Cần chia sẻ và đồng bộ dữ liệu | Mỗi thread cần state độc lập |
| Ví dụ | Counter dùng chung | `SimpleDateFormat`, DB connection per thread |

---

## 2. API chính

```java
ThreadLocal<T> local = new ThreadLocal<>();

local.get();           // Lấy giá trị của thread hiện tại (null nếu chưa set)
local.set(value);      // Set giá trị cho thread hiện tại
local.remove();        // Xoá giá trị — quan trọng khi dùng với thread pool!
```

---

## 3. Khai báo với `initialValue`

```java
// Cách cũ (Java 7 trở về)
private static final ThreadLocal<SimpleDateFormat> formatter =
    new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd HHmm");
        }
    };

// Cách mới — Java 8+ (lambda, gọn hơn)
private static final ThreadLocal<SimpleDateFormat> formatter =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd HHmm"));
```

> 💡 `SimpleDateFormat` **không thread-safe** — đây là use case điển hình cho `ThreadLocal`:  
> mỗi thread có instance `SimpleDateFormat` riêng, không cần synchronized.

---

## 4. Ví dụ minh hoạ

```java
public class ThreadLocalExample implements Runnable {

    private static final ThreadLocal<SimpleDateFormat> formatter =
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd HHmm"));

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalExample obj = new ThreadLocalExample();
        for (int i = 0; i < 5; i++) {
            new Thread(obj, "Thread-" + i).start();
            Thread.sleep(500);
        }
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        // Mỗi thread lấy bản sao initialValue của riêng mình
        System.out.println(name + " | default: " + formatter.get().toPattern());

        // Thay đổi chỉ ảnh hưởng thread này, không ảnh hưởng thread khác
        formatter.set(new SimpleDateFormat("dd/MM/yyyy"));
        System.out.println(name + " | updated: " + formatter.get().toPattern());
    }
}
```

**Output (minh hoạ):**

```
Thread-0 | default: yyyyMMdd HHmm
Thread-1 | default: yyyyMMdd HHmm   ← Thread-1 vẫn thấy giá trị default
Thread-0 | updated: dd/MM/yyyy      ← Chỉ Thread-0 bị đổi
Thread-2 | default: yyyyMMdd HHmm   ← Thread-2 không bị ảnh hưởng
Thread-1 | updated: dd/MM/yyyy
...
```

---

## 5. Lưu ý quan trọng

### ⚠️ Luôn `remove()` khi dùng với Thread Pool

```java
// Thread pool tái sử dụng thread → giá trị ThreadLocal CŨ còn sót lại
try {
    formatter.set(new SimpleDateFormat("dd/MM/yyyy"));
    // ... xử lý ...
} finally {
    formatter.remove(); // ✅ Bắt buộc khi dùng với ExecutorService / Thread Pool
}
```

> Nếu không `remove()`, thread tiếp theo lấy thread cũ từ pool sẽ thấy giá trị **của request trước** → bug khó phát hiện!

---

## 6. Tóm tắt

```
Biến thường:     Thread A ──┐
                             ├──► Shared variable (cần synchronized)
                 Thread B ──┘

ThreadLocal:     Thread A ──► Bản sao A (độc lập)
                 Thread B ──► Bản sao B (độc lập)
                 Thread C ──► Bản sao C (độc lập)
```

| Khi nào dùng `ThreadLocal` | Ví dụ |
|--------------------------|-------|
| Object không thread-safe cần dùng trong nhiều thread | `SimpleDateFormat`, `Random` |
| Truyền context xuyên suốt call stack không qua tham số | User session, Transaction ID |
| DB connection per thread | JDBC connection pool |