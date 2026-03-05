# Java Thread Pool & Executor

> **Thread Pool** quản lý tập các worker thread tái sử dụng — tránh tốn chi phí tạo/huỷ thread liên tục.  
> Các task được đưa vào **queue**, worker thread nhận và thực thi lần lượt.

---

## 1. Tại sao dùng Thread Pool?

```
Không có Thread Pool:          Có Thread Pool:
Task 1 → Tạo Thread → Chạy    Task 1 ──┐
Task 2 → Tạo Thread → Chạy    Task 2 ──┤──► Worker Thread 1 (tái sử dụng)
Task 3 → Tạo Thread → Chạy    Task 3 ──┤──► Worker Thread 2 (tái sử dụng)
         ↑ Tốn kém!            Task 4 ──┘──► Queue (chờ)
```

---

## 2. Cách nhanh nhất: `Executors` factory

```java
ExecutorService executor = Executors.newFixedThreadPool(5); // 5 thread cố định

for (int i = 0; i < 10; i++) {
    executor.execute(new WorkerThread("task-" + i)); // Submit 10 task
}

executor.shutdown();                      // Không nhận task mới
while (!executor.isTerminated()) { }      // Chờ tất cả task xong
System.out.println("Finished all threads");
```

**Kết quả:** 5 task đầu chạy ngay, 5 task còn lại chờ trong queue cho đến khi có thread rảnh.

### Các loại Executor phổ biến

| Factory method | Mô tả |
|---------------|-------|
| `newFixedThreadPool(n)` | Pool cố định `n` thread |
| `newCachedThreadPool()` | Tạo thread mới khi cần, tái sử dụng thread rảnh |
| `newSingleThreadExecutor()` | 1 thread duy nhất, task chạy tuần tự |
| `newScheduledThreadPool(n)` | Chạy task theo lịch / định kỳ |

---

## 3. Kiểm soát nâng cao: `ThreadPoolExecutor`

```java
ThreadPoolExecutor pool = new ThreadPoolExecutor(
    2,                              // corePoolSize    — số thread luôn sống
    4,                              // maximumPoolSize — tối đa khi queue đầy
    10, TimeUnit.SECONDS,           // keepAliveTime   — thread idle bị huỷ sau 10s
    new ArrayBlockingQueue<>(2),    // workQueue       — queue chứa tối đa 2 task chờ
    Executors.defaultThreadFactory(),
    new RejectedExecutionHandlerImpl() // Xử lý task bị từ chối
);
```

### Cách ThreadPoolExecutor xử lý task

```
Số task submitted → 10

core (2 thread)     ──► Chạy task 0, 1
queue (max 2)       ──► Giữ task 2, 3 (chờ)
thêm thread đến max ──► Chạy task 4, 5 (tạo thêm thread 3, 4)
vượt max + queue    ──► task 6, 7, 8, 9 bị REJECT
```

### Custom `RejectedExecutionHandler`

```java
public class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println(r.toString() + " is rejected");
        // Có thể: log, lưu vào DB, retry sau...
    }
}
```

---

## 4. Monitor trạng thái pool

```java
// In ra thông tin pool định kỳ
System.out.printf("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d%n",
    executor.getPoolSize(),          // Số thread hiện có
    executor.getCorePoolSize(),      // Core pool size
    executor.getActiveCount(),       // Thread đang chạy
    executor.getCompletedTaskCount(),// Task đã xong
    executor.getTaskCount()          // Tổng task (cả chờ + đang chạy + đã xong)
);
```

**Output minh hoạ:**

```
cmd6 is rejected
cmd7 is rejected
[monitor] [4/2] Active: 4, Completed: 0, Task: 6   ← 4 thread đang chạy
[monitor] [4/2] Active: 2, Completed: 4, Task: 6   ← 4 xong, 2 đang chạy tiếp
[monitor] [4/2] Active: 0, Completed: 6, Task: 6   ← Hoàn tất
[monitor] [0/2] Active: 0, Completed: 6, isTerminated: true
```

---

## 5. Vòng đời ExecutorService

```java
executor.execute(task);       // Submit task (không có return value)
executor.submit(task);        // Submit task (trả về Future)
executor.shutdown();          // Không nhận task mới, chờ task cũ xong
executor.shutdownNow();       // Cố gắng dừng ngay (interrupt threads)
executor.isShutdown();        // Đã gọi shutdown() chưa?
executor.isTerminated();      // Tất cả task đã hoàn thành chưa?
executor.awaitTermination(timeout, unit); // Chờ tối đa timeout
```

---

## 6. Tóm tắt — Khi nào dùng gì?

| Tình huống | Dùng |
|-----------|------|
| Task song song đồng đều | `Executors.newFixedThreadPool(n)` |
| Nhiều task ngắn, burst cao | `Executors.newCachedThreadPool()` |
| Task chạy tuần tự | `Executors.newSingleThreadExecutor()` |
| Task định kỳ / delay | `Executors.newScheduledThreadPool(n)` |
| Cần kiểm soát queue, reject policy | `ThreadPoolExecutor` tùy chỉnh |

> ⚠️ Luôn gọi `shutdown()` sau khi dùng xong để giải phóng tài nguyên.  
> ⚠️ Tránh `while (!isTerminated()) {}` trong production — dùng `awaitTermination()` thay thế.