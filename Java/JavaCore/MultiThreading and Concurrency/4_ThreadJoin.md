# Thread.join() trong Java

> `Thread.join()` khiến thread hiện tại **chờ** cho đến khi thread được chỉ định kết thúc.  
> Dùng để đảm bảo thứ tự thực thi giữa các thread.

---

## 1. Các overload của `join()`

| Method | Hành vi |
|--------|---------|
| `join()` | Chờ **vô hạn** cho đến khi thread đích kết thúc |
| `join(long millis)` | Chờ tối đa `millis` ms hoặc đến khi thread chết — tùy điều kiện nào đến trước |
| `join(long millis, int nanos)` | Chờ tối đa `millis` ms + `nanos` nanoseconds |

> ⚠️ Tất cả đều ném `InterruptedException` nếu thread bị interrupt trong lúc chờ.  
> ⚠️ Thời gian chờ thực tế phụ thuộc OS — không đảm bảo chính xác tuyệt đối.

---

## 2. Ví dụ minh hoạ

**Mục tiêu:** `t3` chỉ bắt đầu khi `t1` đã chết; `main` là thread cuối cùng kết thúc.

```java
public class ThreadJoinExample {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new MyRunnable(), "t1");
        Thread t2 = new Thread(new MyRunnable(), "t2");
        Thread t3 = new Thread(new MyRunnable(), "t3");

        t1.start();

        // Chờ t1 tối đa 2 giây rồi bắt đầu t2
        t1.join(2000);
        t2.start();

        // Chờ t1 chết hẳn rồi mới bắt đầu t3
        t1.join();
        t3.start();

        // Chờ tất cả thread kết thúc trước khi thoát main
        t2.join();
        t3.join();

        System.out.println("All threads are dead, exiting main thread");
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread started: " + Thread.currentThread().getName());
        try {
            Thread.sleep(4000); // Giả lập xử lý 4 giây
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Thread ended: " + Thread.currentThread().getName());
    }
}
```

**Output:**

```
Thread started: t1
Thread started: t2       ← t2 bắt đầu sau 2s (join timeout)
Thread ended: t1         ← t1 kết thúc sau 4s
Thread started: t3       ← t3 bắt đầu khi t1 chết
Thread ended: t2
Thread ended: t3
All threads are dead, exiting main thread  ← main luôn cuối cùng
```

---

## 3. Phân tích luồng thực thi

```
main  ──► start t1 ──► join(t1, 2000ms) ──► start t2 ──► join(t1) ──► start t3 ──► join(t2,t3) ──► DONE
                              ↓                                   ↓
                       (chờ t1 tối đa 2s)              (chờ t1 chết hẳn)
t1    ─────────────────────────────────────────────── [4s] ──► END
t2                   ──────────────────────────────────────────────── [4s] ──► END
t3                                                           ─────────────────── [4s] ──► END
```

---

## 4. Tóm tắt

| Dùng khi | Cách dùng |
|---------|----------|
| Muốn thread B chạy **sau khi** thread A kết thúc | `threadA.join()` trước `threadB.start()` |
| Muốn main **không thoát** cho đến khi tất cả thread xong | `t1.join(); t2.join(); t3.join();` |
| Muốn chờ tối đa N giây, sau đó tiếp tục dù thread chưa xong | `threadA.join(N)` |

> 💡 `join()` đặt thread hiện tại vào trạng thái **`WAITING`** (hoặc `TIMED_WAITING` nếu có timeout).