# Process và Thread trong Java

> **Process** và **Thread** là 2 đơn vị thực thi cơ bản.  
> Java Multithreading cho phép nhiều tác vụ chạy **đồng thời** trong cùng một chương trình.

---

## 1. Process vs Thread

| Tiêu chí | Process | Thread |
|---------|---------|--------|
| Định nghĩa | Môi trường thực thi độc lập (= một chương trình) | "Lightweight process" — chạy bên trong process |
| Tài nguyên | Có bộ nhớ riêng | **Chia sẻ** tài nguyên của process cha |
| Tạo và huỷ | Tốn kém hơn | Nhanh và nhẹ hơn |
| Giao tiếp | Phức tạp (IPC) | Dễ hơn (shared memory) |
| Context switch | Chi phí cao | Chi phí thấp hơn |

**Ví dụ thực tế:**
- Java Runtime Environment (JRE) chạy như một **process**
- Mỗi ứng dụng Java có ít nhất **một thread** — `main thread`
- Các thread nền (background): GC, memory management, signal processing,...

> 💡 **Multithreading** = 2+ thread chạy đồng thời trong một chương trình.  
> Trên CPU single-core, OS dùng **time slicing** để chia thời gian xử lý giữa các thread.

---

## 2. Tạo Thread trong Java

Java có **2 cách** tạo thread:

| Cách | Khi nào dùng |
|------|-------------|
| Implement `Runnable` | ✅ **Khuyến nghị** — class vẫn có thể extends class khác |
| Extends `Thread` | Khi cần override thêm method của `Thread` |

---

### Cách 1: Implement `Runnable`

```java
public class HeavyWorkRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("START - " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
            doDBProcessing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("END - " + Thread.currentThread().getName());
    }

    private void doDBProcessing() throws InterruptedException {
        Thread.sleep(5000);
    }
}
```

**Cách dùng:** Truyền vào constructor của `Thread`, rồi gọi `start()`:

```java
Thread t1 = new Thread(new HeavyWorkRunnable(), "t1");
t1.start(); // Chạy run() trong thread riêng
```

---

### Cách 2: Extends `Thread`

```java
public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println("START - " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
            doDBProcessing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("END - " + Thread.currentThread().getName());
    }

    private void doDBProcessing() throws InterruptedException {
        Thread.sleep(5000);
    }
}
```

**Cách dùng:** Tạo object và gọi `start()` trực tiếp:

```java
Thread t3 = new MyThread("t3");
t3.start();
```

---

## 3. Ví dụ chạy nhiều Thread

```java
public class ThreadRunExample {
    public static void main(String[] args) {
        // Cách 1: Runnable
        Thread t1 = new Thread(new HeavyWorkRunnable(), "t1");
        Thread t2 = new Thread(new HeavyWorkRunnable(), "t2");
        t1.start();
        t2.start();

        // Cách 2: extends Thread
        Thread t3 = new MyThread("t3");
        Thread t4 = new MyThread("t4");
        t3.start();
        t4.start();
    }
}
```

**Output (thứ tự có thể khác mỗi lần chạy):**

```
Starting Runnable threads
Runnable Threads has been started
Doing heavy processing - START t1
Doing heavy processing - START t2
Starting MyThreads
MyThread - START t3
MyThreads has been started
MyThread - START t4
Doing heavy processing - END t2
MyThread - END t4
MyThread - END t3
Doing heavy processing - END t1
```

> ⚠️ **Thứ tự thread KHÔNG đảm bảo** — do OS quyết định dựa trên time slicing.  
> Có thể set priority (`thread.setPriority()`), nhưng cũng không đảm bảo 100%.

---

## 4. Tóm tắt

```
Thread t = new Thread(runnable, "tên-thread");
t.start();  // Gọi run() trong thread mới — KHÔNG gọi run() trực tiếp!
```

| Làm | Không làm |
|-----|----------|
| `t.start()` | ~~`t.run()`~~ — chạy trong thread hiện tại, không tạo thread mới |
| Implement `Runnable` | Extend `Thread` khi không cần thiết |
| Đặt tên thread rõ ràng | Để thread vô danh — khó debug |