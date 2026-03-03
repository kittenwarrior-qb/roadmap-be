# Thread Life Cycle trong Java

> Hiểu vòng đời của Thread giúp lập trình đa luồng hiệu quả và tránh các lỗi khó debug.  
> Java **không kiểm soát hoàn toàn** quá trình chuyển trạng thái — điều đó phụ thuộc vào **Thread Scheduler của OS**.

---

## 1. Sơ đồ vòng đời

```
                     start()
  NEW  ─────────────────────────► RUNNABLE
                                     │
                          OS chọn    │    Hết time slice /
                          để chạy    ▼    chờ resource
                                  RUNNING ──────────────► BLOCKED / WAITING
                                     │                         │
                                     │  run() hoàn thành       │ resource sẵn sàng /
                                     ▼                         │ notify() / join() xong
                                   DEAD ◄────────────────────-─┘ (→ RUNNABLE)
```

---

## 2. Các trạng thái chi tiết

| Trạng thái | Khi nào xảy ra | Còn "sống" không? |
|-----------|---------------|:-----------------:|
| **NEW** | Vừa tạo object `new Thread()`, chưa gọi `start()` | ❌ |
| **RUNNABLE** | Đã gọi `start()` — đang chờ OS cấp CPU | ✅ |
| **RUNNING** | OS chọn thread này để thực thi | ✅ |
| **BLOCKED / WAITING** | Chờ tài nguyên, chờ thread khác, hoặc hết time slice | ✅ |
| **DEAD** | `run()` đã kết thúc | ❌ |

---

## 3. Mô tả chi tiết từng trạng thái

### 🔵 NEW
```java
Thread t = new Thread(runnable); // Trạng thái: NEW
// Chưa alive, chưa được OS biết đến
```

### 🟡 RUNNABLE
```java
t.start(); // Chuyển sang RUNNABLE
// Vào thread pool — chờ OS cấp CPU để chạy
// Có thể chạy ngay hoặc phải chờ tuỳ theo OS scheduler
```

### 🟢 RUNNING
- OS chọn thread từ pool → chuyển sang **RUNNING**
- CPU bắt đầu thực thi method `run()`
- Có thể chuyển sang trạng thái khác:
  - → **RUNNABLE**: hết time slice
  - → **BLOCKED/WAITING**: chờ tài nguyên
  - → **DEAD**: `run()` hoàn thành

### 🔴 BLOCKED / WAITING
Xảy ra khi thread cần chờ:

| Tình huống | Cách gây ra | Cách thoát |
|-----------|------------|-----------|
| Chờ thread khác chạy xong | `thread.join()` | Thread kia kết thúc |
| Chờ được notify | `object.wait()` | `object.notify()` / `notifyAll()` |
| Chờ I/O | Đọc/ghi file, network | I/O hoàn thành |
| Chờ lock | `synchronized` block | Lock được giải phóng |
| Sleep | `Thread.sleep(ms)` | Hết thời gian sleep |

Sau khi hết chờ → thread quay lại **RUNNABLE**.

### ⚫ DEAD
```java
// Sau khi run() kết thúc (bình thường hoặc có exception)
// Thread không thể restart — gọi start() lại sẽ ném IllegalThreadStateException
```

---

## 4. Chuyển trạng thái — tóm tắt

```
NEW
 └─ start() ──► RUNNABLE
                  └─ OS scheduler ──► RUNNING
                                        ├─ hết time slice ──────────────► RUNNABLE
                                        ├─ wait()/join()/sleep()/IO ────► BLOCKED/WAITING
                                        │                                    └─ hết chờ ──► RUNNABLE
                                        └─ run() xong ──────────────────► DEAD
```

---

## 5. Lưu ý quan trọng

- **Không gọi `run()` trực tiếp** — sẽ chạy trong thread hiện tại, không tạo thread mới.
- **Không thể restart thread đã DEAD** — phải tạo object `Thread` mới.
- **Thứ tự thực thi không đảm bảo** — OS quyết định thread nào được chạy trước.
- Có thể set `thread.setPriority(1-10)`, nhưng đây chỉ là **gợi ý** cho OS, không đảm bảo.