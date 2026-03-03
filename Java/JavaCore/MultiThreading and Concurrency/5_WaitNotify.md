# wait(), notify(), notifyAll() trong Java

> 3 method này thuộc class `Object` — dùng để **phối hợp giữa các thread** qua cơ chế monitor lock.  
> Phải được gọi bên trong `synchronized` block, nếu không sẽ ném `IllegalMonitorStateException`.

---

## 1. Tổng quan 3 methods

| Method | Hành vi |
|--------|---------|
| `wait()` | Thread **nhường lock** và chờ vô hạn cho đến khi được `notify()` / `notifyAll()` |
| `wait(long millis)` | Chờ tối đa `millis` ms, sau đó tự wakeup |
| `notify()` | Đánh thức **một** thread đang chờ (OS chọn thread nào) |
| `notifyAll()` | Đánh thức **tất cả** thread đang chờ trên object đó |

> 💡 Khác với `Thread.sleep()`, `wait()` **giải phóng lock** trong khi chờ, cho phép thread khác vào `synchronized` block.

---

## 2. Cơ chế hoạt động

```
Thread A (Waiter)                    Thread B (Notifier)
─────────────────                    ───────────────────
synchronized(obj) {                  synchronized(obj) {
    obj.wait();  ──── nhả lock ────►     // Thread B lấy lock
    // Blocked...                        obj.setMsg("done");
    // Chờ được notify                   obj.notify(); ────► Thread A wakeup
}                                    }
// Tiếp tục xử lý
```

---

## 3. Ví dụ: Producer – Consumer

### Class `Message` (shared object)

```java
public class Message {
    private String msg;

    public Message(String str) { this.msg = str; }
    public String getMsg()     { return msg; }
    public void setMsg(String str) { this.msg = str; }
}
```

### Class `Waiter` (Consumer — chờ được thông báo)

```java
public class Waiter implements Runnable {
    private Message msg;

    public Waiter(Message m) { this.msg = m; }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        synchronized (msg) {                       // Lấy monitor của msg
            try {
                System.out.println(name + " đang chờ...");
                msg.wait();                        // Nhả lock và chờ
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(name + " được notify, xử lý: " + msg.getMsg());
        }
    }
}
```

### Class `Notifier` (Producer — xử lý rồi thông báo)

```java
public class Notifier implements Runnable {
    private Message msg;

    public Notifier(Message msg) { this.msg = msg; }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);                    // Giả lập xử lý
            synchronized (msg) {                   // Lấy monitor của msg
                msg.setMsg("Notifier work done");
                msg.notify();                      // Đánh thức 1 thread
                // msg.notifyAll();               // Đánh thức tất cả thread
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

### Class `WaitNotifyTest` (main)

```java
public class WaitNotifyTest {
    public static void main(String[] args) {
        Message msg = new Message("process it");

        new Thread(new Waiter(msg), "waiter").start();
        new Thread(new Waiter(msg), "waiter1").start();
        new Thread(new Notifier(msg), "notifier").start();

        System.out.println("All threads started");
    }
}
```

---

## 4. So sánh output: `notify()` vs `notifyAll()`

### Dùng `notify()` — Chương trình bị treo!

```
waiter  đang chờ...
waiter1 đang chờ...
All threads started
notifier started
waiter  được notify, xử lý: Notifier work done
⛔ waiter1 vẫn còn chờ mãi → chương trình không kết thúc
```

### Dùng `notifyAll()` — Chương trình kết thúc bình thường

```
waiter  đang chờ...
waiter1 đang chờ...
All threads started
notifier started
waiter1 được notify, xử lý: Notifier work done
waiter  được notify, xử lý: Notifier work done
✅ Cả 2 thread kết thúc → chương trình thoát
```

---

## 5. So sánh `wait()` vs `sleep()`

| Tiêu chí | `wait()` | `sleep()` |
|---------|---------|----------|
| Thuộc class | `Object` | `Thread` |
| Phải trong `synchronized`? | ✅ Bắt buộc | ❌ Không cần |
| Giải phóng lock? | ✅ Có | ❌ Không |
| Wakeup khi nào | `notify()` / `notifyAll()` / timeout | Sau khoảng thời gian chỉ định |
| Dùng để | Phối hợp giữa các thread | Delay đơn giản |

---

## 6. Tóm tắt

```
// Luôn dùng trong synchronized block
synchronized (sharedObject) {
    while (!condition) {         // Dùng while, không dùng if (tránh spurious wakeup)
        sharedObject.wait();
    }
    // Xử lý khi condition đúng
}

// Bên notifier
synchronized (sharedObject) {
    // Thay đổi state
    sharedObject.notifyAll();   // Ưu tiên notifyAll() thay vì notify()
}
```

> ✅ **Best practice:** Dùng `while` thay vì `if` khi gọi `wait()` để tránh **spurious wakeup**  
> ✅ Ưu tiên `notifyAll()` trừ khi chắc chắn chỉ có 1 thread cần được đánh thức