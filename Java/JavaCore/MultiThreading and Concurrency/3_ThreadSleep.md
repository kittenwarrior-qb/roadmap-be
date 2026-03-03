# Thread.sleep() trong Java

> `Thread.sleep()` tạm dừng thread hiện tại trong một khoảng thời gian chỉ định.  
> Chỉ ảnh hưởng đến **thread đang gọi**, không ảnh hưởng các thread khác.

---

## 1. Cú pháp

```java
Thread.sleep(long millis);                  // Nghỉ theo milliseconds
Thread.sleep(long millis, int nanos);       // Milliseconds + nanoseconds (0–999999)
```

- `millis` không được âm → ném `IllegalArgumentException`
- Có thể bị gián đoạn bởi thread khác → ném `InterruptedException`

---

## 2. Cách hoạt động

Khi `Thread.sleep()` được gọi:
1. Thread chuyển sang trạng thái **`TIMED_WAITING`**
2. Không tiêu tốn CPU trong thời gian ngủ
3. Sau khi hết thời gian → chuyển về **`RUNNABLE`**, chờ OS cấp CPU

> ⚠️ **Thời gian thực tế có thể lớn hơn** thời gian chỉ định do giới hạn độ chính xác của system timer (thường 1–15ms).

---

## 3. Điểm quan trọng cần nhớ

| Đặc điểm | Chi tiết |
|---------|---------|
| **Chỉ ảnh hưởng thread hiện tại** | Không sleep các thread khác |
| **Giữ lock** | `sleep()` **KHÔNG** giải phóng `synchronized` lock đang giữ |
| **Có thể bị ngắt** | Thread khác gọi `interrupt()` → `InterruptedException` |
| **Độ chính xác thấp** | Phụ thuộc OS scheduler, không đảm bảo đúng chính xác |

---

## 4. Ví dụ cơ bản

```java
public static void main(String[] args) throws InterruptedException {
    long start = System.currentTimeMillis();

    Thread.sleep(2000); // Ngủ 2 giây

    System.out.println("Đã ngủ: " + (System.currentTimeMillis() - start) + "ms");
    // Output: Đã ngủ: 2005ms  ← không chính xác 2000ms
}
```

---

## 5. Xử lý `InterruptedException` đúng cách

```java
Thread worker = new Thread(() -> {
    try {
        for (int i = 0; i < 10; i++) {
            System.out.println("Working... " + i);
            Thread.sleep(1000);
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // ✅ Restore interrupted status
        System.out.println("Thread bị ngắt, đang dọn dẹp...");
        cleanup();
    }
});

worker.start();
Thread.sleep(3000);
worker.interrupt(); // Ngắt thread sau 3 giây
```

> 💡 **Luôn gọi `Thread.currentThread().interrupt()`** sau khi bắt `InterruptedException`  
> để báo hiệu cho caller biết thread đã bị interrupt.

---

## 6. Use Cases thực tế

### Rate Limiting (giới hạn tốc độ)

```java
// Adaptive sleep dựa trên response time
private static long calculateSleepTime(long responseTime) {
    if (responseTime > 150) return 2000;      // Chậm → nghỉ lâu
    else if (responseTime < 100) return 500;  // Nhanh → nghỉ ít
    else return 1000;                         // Bình thường
}
```

### Exponential Backoff (retry với delay tăng dần)

```java
int MAX_RETRIES = 5;
long INITIAL_DELAY = 1000;

for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
    try {
        callExternalService(); // Thử gọi API
        break; // Thành công → thoát
    } catch (Exception e) {
        long delay = (long) (INITIAL_DELAY * Math.pow(2, attempt));
        // Attempt 0: 1s | 1: 2s | 2: 4s | 3: 8s | 4: 16s
        System.out.println("Thử lại sau " + delay + "ms");
        Thread.sleep(delay);
    }
}
```

### Circuit Breaker Pattern

```java
if (failureCount >= MAX_FAILURES) {
    long backoff = BACKOFF_DELAY * (1L << Math.min(failureCount - MAX_FAILURES, 5));
    System.out.println("Circuit breaker: chờ " + backoff + "ms");
    Thread.sleep(backoff);
}
```

---

## 7. So sánh `sleep()` với các phương pháp khác

| Phương pháp | Giải phóng lock | Độ chính xác | Có thể interrupt | Dùng khi |
|------------|:---------------:|:------------:|:----------------:|---------|
| `Thread.sleep()` | ❌ | Thấp (1-15ms) | ✅ | Delay đơn giản |
| `Object.wait()` | ✅ | Cao | ✅ | Chờ điều kiện (trong `synchronized`) |
| `LockSupport.parkNanos()` | ❌ | Cao (nanoseconds) | ✅ | Delay độ chính xác cao |
| `ScheduledExecutorService` | N/A | Cao | ✅ | Task định kỳ, production code |

---

## 8. Modern Alternatives

### `ScheduledExecutorService` — Khuyến nghị cho production

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

// Chạy sau 2 giây
scheduler.schedule(() -> System.out.println("Delayed task"), 2, TimeUnit.SECONDS);

// Chạy định kỳ mỗi 1 giây
scheduler.scheduleAtFixedRate(() -> System.out.println("Periodic"), 0, 1, TimeUnit.SECONDS);

Thread.sleep(10000); // Chờ 10s rồi shutdown
scheduler.shutdown();
```

### `LockSupport.parkNanos()` — Độ chính xác cao

```java
long start = System.nanoTime();
LockSupport.parkNanos(1_000_000_000L); // 1 giây theo nanosecond
System.out.println("Actual: " + (System.nanoTime() - start) + "ns");
```

---

## 9. Common Pitfalls — Lỗi thường gặp

### ❌ Sleep trong `synchronized` block (giữ lock)

```java
// BAD: Các thread khác bị block hoàn toàn
synchronized (lock) {
    Thread.sleep(1000); // Chiếm lock trong khi ngủ!
}

// GOOD: Sleep bên ngoài synchronized
synchronized (lock) {
    // Xử lý nhanh, giải phóng lock sớm
}
Thread.sleep(1000); // Sleep sau khi đã thả lock
```

### ❌ Sleep trên UI thread

```java
// BAD: Đóng băng UI 5 giây
public void onClick() {
    Thread.sleep(5000); // UI freeze!
}

// GOOD: Dùng background thread
public void onClick() {
    CompletableFuture.runAsync(() -> {
        Thread.sleep(5000); // Chạy ngầm, không chặn UI
        // updateUI(); // Cập nhật UI trên main thread
    });
}
```

### ❌ Dùng cho timing chính xác

```java
// BAD: Không đảm bảo đúng 1000ms
Thread.sleep(1000);

// GOOD: Dùng ScheduledExecutorService
scheduler.schedule(task, 1000, TimeUnit.MILLISECONDS);
```

---

## 10. Tóm tắt — Khi nào dùng gì?

| Tình huống | Dùng |
|-----------|------|
| Delay đơn giản / testing | `Thread.sleep()` |
| Task định kỳ / delay chính xác | `ScheduledExecutorService` |
| Chờ điều kiện trong `synchronized` | `Object.wait()` / `notify()` |
| Delay nanosecond (framework) | `LockSupport.parkNanos()` |
| Async delay (Java 8+) | `CompletableFuture` + `delayedExecutor()` |