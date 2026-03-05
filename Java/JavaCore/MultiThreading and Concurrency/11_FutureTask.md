# Java FutureTask

> `FutureTask` là **class cụ thể** implement cả `Future` và `Runnable`.  
> Dùng khi cần **tùy chỉnh behaviour** của `Future` (ví dụ: override `get()` với default timeout).

---

## 1. FutureTask là gì?

```
Callable<T>  ──►  FutureTask<T>  ──►  ExecutorService.execute()
                      │
                      ├── isDone()       → kiểm tra trạng thái
                      ├── get()          → lấy kết quả (block)
                      ├── get(timeout)   → lấy kết quả với timeout
                      └── cancel()       → huỷ task
```

**Khác với `Future` (interface):**

| | `Future<T>` | `FutureTask<T>` |
|-|------------|----------------|
| Loại | Interface | Concrete class |
| Tạo từ | `executor.submit(callable)` | `new FutureTask<>(callable)` |
| Submit bằng | `executor.submit()` | `executor.execute()` (vì implement `Runnable`) |
| Override được | ❌ | ✅ override `done()`, `get()`,... |

---

## 2. Ví dụ minh hoạ

```java
// Callable với thời gian chờ khác nhau
public class MyCallable implements Callable<String> {
    private long waitTime;

    public MyCallable(int millis) { this.waitTime = millis; }

    @Override
    public String call() throws Exception {
        Thread.sleep(waitTime);
        return Thread.currentThread().getName();
    }
}
```

```java
// Tạo FutureTask từ Callable
FutureTask<String> futureTask1 = new FutureTask<>(new MyCallable(1000)); // xong sau 1s
FutureTask<String> futureTask2 = new FutureTask<>(new MyCallable(2000)); // xong sau 2s

ExecutorService executor = Executors.newFixedThreadPool(2);
executor.execute(futureTask1);  // execute() thay vì submit()
executor.execute(futureTask2);

while (true) {
    if (futureTask1.isDone() && futureTask2.isDone()) {
        System.out.println("Done");
        executor.shutdown();
        return;
    }

    // Chờ futureTask1 xong (block vô hạn)
    if (!futureTask1.isDone()) {
        System.out.println("FutureTask1 = " + futureTask1.get());
    }

    // Chờ futureTask2 tối đa 200ms mỗi lần kiểm tra
    try {
        String result = futureTask2.get(200L, TimeUnit.MILLISECONDS);
        if (result != null) {
            System.out.println("FutureTask2 = " + result);
        }
    } catch (TimeoutException e) {
        System.out.println("Waiting for FutureTask2...");
    }
}
```

**Output:**

```
FutureTask1 = pool-1-thread-1         ← Sau ~1s (futureTask1 xong)
Waiting for FutureTask2...            ← Timeout 200ms, chưa xong
Waiting for FutureTask2...
Waiting for FutureTask2...
Waiting for FutureTask2...
Waiting for FutureTask2...
FutureTask2 = pool-1-thread-2         ← Sau ~2s (futureTask2 xong)
Done
```

---

## 3. Khi nào dùng FutureTask?

| Tình huống | Dùng |
|-----------|------|
| Chỉ cần lấy kết quả từ Callable | `Future<T>` (từ `executor.submit()`) |
| Cần submit như Runnable (không qua submit) | `FutureTask<T>` + `execute()` |
| Muốn override `done()` để callback khi task xong | `FutureTask<T>` (override method) |
| Cần dùng cùng task như cả Runnable và Future | `FutureTask<T>` |

---

## 4. Tóm tắt so sánh Callable / Future / FutureTask

```
Callable<T>  ─── định nghĩa task trả về giá trị
    │
    ├─► executor.submit() ──────────────► Future<T>     (chỉ lấy kết quả)
    │
    └─► new FutureTask<>(callable) ────► FutureTask<T>  (linh hoạt hơn, override được)
                                              │
                                         executor.execute()
```