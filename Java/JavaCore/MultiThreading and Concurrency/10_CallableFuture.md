# Java Callable & Future

> **`Callable`** giống `Runnable` nhưng có thể **trả về giá trị** và **throw Exception**.  
> **`Future`** đại diện cho kết quả của một task bất đồng bộ — dùng để lấy giá trị khi task hoàn thành.

---

## 1. Runnable vs Callable

| Tiêu chí | `Runnable` | `Callable<T>` |
|---------|-----------|--------------|
| Interface method | `void run()` | `T call() throws Exception` |
| Trả về giá trị | ❌ | ✅ |
| Throw checked exception | ❌ | ✅ |
| Submit bằng | `execute()` | `submit()` → trả về `Future<T>` |
| Có từ | Java 1.0 | Java 5 |

---

## 2. Future API

```java
Future<T> future = executor.submit(callable);

future.get();                          // Chờ và lấy kết quả (block vô hạn)
future.get(2, TimeUnit.SECONDS);       // Chờ tối đa 2s → ném TimeoutException
future.isDone();                       // Task đã xong chưa? (true/false)
future.isCancelled();                  // Task đã bị cancel chưa?
future.cancel(mayInterruptIfRunning);  // Huỷ task
```

---

## 3. Ví dụ: 100 task song song, lấy kết quả qua Future

```java
// Định nghĩa Callable — trả về tên thread đang chạy task
public class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(1000);
        return Thread.currentThread().getName(); // Giá trị trả về
    }
}
```

```java
ExecutorService executor = Executors.newFixedThreadPool(10); // Pool 10 thread
List<Future<String>> futures = new ArrayList<>();

// Submit 100 task, lưu Future để lấy kết quả sau
for (int i = 0; i < 100; i++) {
    Future<String> future = executor.submit(new MyCallable());
    futures.add(future);
}

// Lấy kết quả — get() sẽ block cho đến khi task xong
for (Future<String> future : futures) {
    System.out.println(new Date() + " :: " + future.get());
}

executor.shutdown();
```

**Output (mỗi dòng delay ~1s do `get()` chờ task):**

```
Mon Dec 31 20:40:15 :: pool-1-thread-1
Mon Dec 31 20:40:16 :: pool-1-thread-2
Mon Dec 31 20:40:16 :: pool-1-thread-3
...  ← 10 thread chạy song song, 100 task chia nhau theo pool
```

---

## 4. Tóm tắt flow

```
Callable<T>
    └─► executor.submit()  ──►  Future<T>  ──►  future.get()  ──►  T (kết quả)
                                                      │
                                            Block đến khi task xong
                                            (hoặc timeout / cancel)
```

> 💡 Dùng `FutureTask` nếu cần override behaviour của `Future` (ví dụ: custom timeout mặc định).