# Thread Safety & Synchronization trong Java

> Khi nhiều thread cùng **đọc và ghi** một biến chia sẻ, dữ liệu có thể bị sai lệch.  
> **Synchronization** là cơ chế đảm bảo chỉ một thread thực thi đoạn code quan trọng tại một thời điểm.

---

## 1. Tại sao cần Thread Safety?

`count++` trông có vẻ là 1 bước, nhưng thực tế gồm **3 bước riêng biệt**:

```
1. Đọc giá trị hiện tại của count
2. Tính count + 1
3. Ghi giá trị mới vào count
```

Nếu 2 thread cùng thực hiện đồng thời → có thể đọc cùng giá trị → ghi đè nhau → **kết quả sai**.

```java
// 2 thread, mỗi thread tăng count 4 lần → kỳ vọng: 8
// Thực tế: 6, 7 hoặc 8 (không ổn định)
class ProcessingThread implements Runnable {
    private int count;

    @Override
    public void run() {
        for (int i = 1; i < 5; i++) {
            processSomething(i);
            count++; // ❌ Không atomic — race condition!
        }
    }
}
```

---

## 2. Các cách đảm bảo Thread Safety

| Cách | Cơ chế | Ví dụ |
|------|--------|-------|
| **`synchronized`** | Lock object/class | `synchronized(lock) { ... }` |
| **Atomic classes** | Lock-free, CAS hardware | `AtomicInteger`, `AtomicLong` |
| **`volatile`** | Đọc từ main memory, không cache | `volatile int count` |
| **Locks (java.util.concurrent)** | Linh hoạt hơn `synchronized` | `ReentrantLock` |
| **Thread-safe Collections** | Đã đồng bộ sẵn | `ConcurrentHashMap`, `CopyOnWriteArrayList` |

---

## 3. Synchronized — Cách dùng

### Synchronized method

```java
// Lock trên object instance
public synchronized void doSomething() { ... }

// Lock trên class (static method)
public static synchronized void doSomething() { ... }
```

### Synchronized block (khuyến nghị)

```java
private final Object lock = new Object(); // Dùng private final để tránh bị thay đổi reference

public void doSomething() {
    synchronized (lock) {
        // Chỉ lock phần cần thiết — tối ưu hơn lock cả method
        count++;
    }
}
```

**Fix cho ví dụ ở mục 1:**

```java
private final Object mutex = new Object();

@Override
public void run() {
    for (int i = 1; i < 5; i++) {
        processSomething(i);
        synchronized (mutex) {
            count++; // ✅ Chỉ một thread chạy đoạn này tại một thời điểm
        }
    }
}
```

---

## 4. Ví dụ thực tế: Xử lý mảng chia sẻ

```java
// ❌ Không an toàn — nhiều thread ghi vào cùng index
private void addThreadName(int i, String name) {
    strArr[i] = strArr[i] + ":" + name; // Race condition!
}

// Output lỗi: [1:t2:t3, 2:t1, 3:t3, ...]  ← thiếu tên thread
```

```java
// ✅ An toàn — synchronized block bảo vệ thao tác ghi
private final Object lock = new Object();

private void addThreadName(int i, String name) {
    synchronized (lock) {
        strArr[i] = strArr[i] + ":" + name;
    }
}

// Output đúng: [1:t1:t2:t3, 2:t2:t1:t3, ...]  ← đủ tất cả tên thread
```

---

## 5. Các lưu ý quan trọng

### ✅ Nên làm

```java
// Dùng private final object để lock
private final Object lock = new Object();
synchronized (lock) { ... }

// Lock ở mức nhỏ nhất có thể (block thay vì cả method)
```

### ❌ Không nên làm

```java
// Không dùng String làm lock — String được intern trong pool, có thể xung đột với code khác
synchronized ("myLock") { ... } // ❌

// Không dùng public field làm lock — có thể bị đổi reference từ bên ngoài
public Object lock = new Object(); // ❌
myObject.lock = new Object();      // Attacker thay đổi được!

// Không lock cả method nếu chỉ cần bảo vệ một phần nhỏ
public synchronized void bigMethod() { // ❌ Block tất cả thread quá lâu
    // ... nhiều code không cần sync ...
    count++;
}
```

---

## 6. Tóm tắt

| Tiêu chí | Chi tiết |
|---------|---------|
| **Phạm vi lock** | Method → lock cả object/class; Block → chỉ lock phần cần thiết |
| **Hiệu năng** | Sync đảm bảo tính đúng đắn nhưng **giảm hiệu năng** — chỉ dùng khi cần |
| **Deadlock** | Tránh lock lồng nhau hoặc dùng `tryLock()` từ `ReentrantLock` |
| **Chỉ trong 1 JVM** | `synchronized` không hoạt động trên distributed system |
| **Không dùng được cho** | Constructor và variable declaration |

```
Race condition xảy ra khi:
  Nhiều thread  ──► Cùng đọc/ghi ──► Biến chia sẻ ──► Kết quả sai

Synchronized giải quyết bằng:
  Thread A lấy lock ──► Thực thi ──► Nhả lock
  Thread B chờ      ──────────────────────────► Lấy lock ──► Thực thi
```