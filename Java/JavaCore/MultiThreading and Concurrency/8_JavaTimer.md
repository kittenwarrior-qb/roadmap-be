# Java Timer & TimerTask

> `Timer` dùng để lên lịch thực thi task — chạy **một lần** hoặc **định kỳ**.  
> `TimerTask` là abstract class implement `Runnable` — extend để định nghĩa task cần chạy.

---

## 1. Các method schedule chính

| Method | Mô tả |
|--------|-------|
| `schedule(task, delay)` | Chạy 1 lần sau `delay` ms |
| `schedule(task, date)` | Chạy 1 lần vào thời điểm `date` |
| `schedule(task, delay, period)` | Chạy định kỳ, bắt đầu sau `delay` ms |
| `scheduleAtFixedRate(task, delay, period)` | Chạy định kỳ với tần suất cố định |
| `cancel()` | Dừng timer, huỷ task đã lên lịch (không dừng task đang chạy) |

---

## 2. Ví dụ: Task định kỳ mỗi 10 giây

```java
// Định nghĩa task
public class MyTimerTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("Task started:  " + new Date());
        completeTask();                             // Giả lập xử lý 20 giây
        System.out.println("Task finished: " + new Date());
    }

    private void completeTask() throws InterruptedException {
        Thread.sleep(20_000); // Task mất 20s để hoàn thành
    }
}
```

```java
// Lên lịch chạy
TimerTask task = new MyTimerTask();
Timer timer = new Timer(true);               // true = chạy như daemon thread

timer.scheduleAtFixedRate(task, 0, 10_000); // Lên lịch mỗi 10 giây
System.out.println("TimerTask started");

Thread.sleep(120_000);                       // Chờ 2 phút
timer.cancel();                              // Huỷ timer
System.out.println("TimerTask cancelled");
```

**Output:**

```
TimerTask started
Task started:  19:16:39
Task finished: 19:16:59  ← 20s — task chạy chậm hơn interval 10s
Task started:  19:16:59  ← Timer chờ task xong rồi mới chạy tiếp, không bỏ qua
Task finished: 19:17:19
Task started:  19:17:19
...
TimerTask cancelled
Task finished: 19:18:39  ← Task đang chạy vẫn tiếp tục đến khi xong
```

---

## 3. Cơ chế hoạt động

```
Timer (scheduleAtFixedRate, period=10s)
│
├─► Task 1 bắt đầu ──[chạy 20s]──► Task 1 xong
│                                        │
│                                        └─► Task 2 bắt đầu (chờ Task 1 xong)
│                                                 │
│                                  timer.cancel() │
│                                        ├────────┘
│                                        └─► Task đang chạy vẫn hoàn thành bình thường
```

> ⚠️ Nếu task mất nhiều thời gian hơn `period`, Timer **tích lũy** task vào queue.  
> Queue sẽ tăng không giới hạn → task luôn trong trạng thái chờ.  
> **Luôn đảm bảo `period > thời gian thực thi task`.**

---

## 4. Đặc điểm quan trọng

| Đặc điểm | Chi tiết |
|---------|---------|
| **Thread-safe** | Nhiều thread có thể chia sẻ 1 `Timer` mà không cần sync |
| **Daemon thread** | `new Timer(true)` → JVM tự tắt Timer khi tất cả user thread kết thúc |
| **`cancel()`** | Huỷ lịch các task chưa chạy, **không** dừng task đang chạy |
| **Chỉ 1 thread** | Tại một thời điểm chỉ có 1 `TimerTask` đang thực thi |
| **Cơ chế nội bộ** | Dùng `wait()` / `notify()` trên `TaskQueue` |

---

## 5. Timer vs ScheduledExecutorService

| Tiêu chí | `Timer` | `ScheduledExecutorService` |
|---------|---------|--------------------------|
| Java version | 1.3+ | 1.5+ |
| Số thread | 1 thread duy nhất | Thread pool (nhiều task song song) |
| Khi task throw Exception | Timer dừng hoàn toàn | Chỉ task đó bị ảnh hưởng |
| Độ chính xác timing | Thấp hơn | Cao hơn |
| Khuyến nghị | Legacy | ✅ Dùng cho dự án mới |

```java
// Thay thế khuyến nghị (Java 5+)
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);
```