# Lock API

## Tổng quan
`synchronized` keyword có một số hạn chế, dẫn đến việc Java 1.5 giới thiệu Lock API trong package `java.util.concurrent.locks` để cải thiện cơ chế locking.

## Các interface và class chính

### 1. Lock
Interface cơ bản của Lock API, cung cấp đầy đủ tính năng của `synchronized` với thêm nhiều khả năng:
- `lock()` - acquire lock
- `unlock()` - release lock  
- `tryLock()` - chờ lock trong khoảng thời gian nhất định
- `newCondition()` - tạo Condition object

### 2. Condition
Tương tự Object wait-notify model nhưng có thể tạo nhiều wait set khác nhau:
- `await()` - tương tự `wait()`
- `signal()`, `signalAll()` - tương tự `notify()`, `notifyAll()`

### 3. ReadWriteLock
Chứa cặp lock liên kết:
- Read lock: nhiều thread có thể giữ đồng thời
- Write lock: độc quyền (exclusive)

### 4. ReentrantLock
Implementation phổ biến nhất của Lock interface. Hỗ trợ tính năng reentrant - thread đã giữ lock có thể vào các synchronized block khác yêu cầu cùng lock đó.

**Ví dụ Reentrant:**
```java
public class Test {
    public synchronized void foo() {
        // do something
        bar(); // có thể gọi vì đã giữ lock
    }
    
    public synchronized void bar() {
        // do some more
    }
}
```
Thread vào `foo()` đã giữ lock trên Test object, nên có thể tiếp tục gọi `bar()`.

## Ví dụ sử dụng ReentrantLock

**Resource class:**
```java
public class Resource {
    public void doSomething() {
        // DB read, write etc - cần thread-safe
    }
    
    public void doLogging() {
        // logging - không cần thread-safe
    }
}
```

**Cách 1: Sử dụng synchronized**
```java
public class SynchronizedLockExample implements Runnable {
    private Resource resource;
    
    public SynchronizedLockExample(Resource r) {
        this.resource = r;
    }
    
    @Override
    public void run() {
        synchronized (resource) {
            resource.doSomething();
        }
        resource.doLogging();
    }
}
```

**Cách 2: Sử dụng ReentrantLock**
```java
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrencyLockExample implements Runnable {
    private Resource resource;
    private Lock lock;
    
    public ConcurrencyLockExample(Resource r) {
        this.resource = r;
        this.lock = new ReentrantLock();
    }
    
    @Override
    public void run() {
        try {
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                resource.doSomething();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // luôn release lock
        }
        resource.doLogging();
    }
}
```

**Lưu ý:**
- `tryLock()` giới hạn thời gian chờ, tránh chờ vô hạn
- Dùng try-finally để đảm bảo unlock ngay cả khi có exception

## Lock vs synchronized

| Tiêu chí | Lock API | synchronized |
|----------|----------|--------------|
| **Timeout** | Có `tryLock()` với thời gian chờ xác định | Có thể chờ vô hạn |
| **Code** | Phức tạp hơn, bắt buộc try-finally | Sạch và dễ maintain hơn |
| **Phạm vi** | Có thể lock ở method này, unlock ở method khác | Chỉ trong 1 method/block |
| **Fairness** | Có thể set fairness = true (thread chờ lâu nhất được ưu tiên) | Không hỗ trợ |
| **Conditions** | Tạo nhiều Condition khác nhau cho các thread | Chỉ có 1 wait set |
| **Visibility** | Nhiều options và control hơn | Hạn chế hơn |

## Khi nào dùng Lock?
- Cần timeout cho việc acquire lock
- Cần fairness trong xử lý thread
- Cần nhiều condition variables
- Cần lock/unlock ở các method khác nhau
- Cần interrupt thread đang chờ lock

## Khi nào dùng synchronized?
- Code đơn giản, không cần tính năng nâng cao
- Lock/unlock trong cùng 1 scope
- Ưu tiên code sạch và dễ maintain
