# Java PriorityQueue

> `PriorityQueue` là hàng đợi xử lý phần tử theo **độ ưu tiên** thay vì FIFO.  
> Được giới thiệu từ **Java 1.5**, cải tiến thêm trong Java 8.

---

## 1. Tổng quan

- Triển khai nội bộ bằng cấu trúc **Priority Heap**.
- Là **unbounded queue** (không giới hạn kích thước).
- Mặc định sắp xếp theo **natural ordering** (tăng dần) → phần tử nhỏ nhất ở đầu.
- Không cho phép giá trị `null`.
- **Không thread-safe** → dùng `PriorityBlockingQueue` nếu cần đồng bộ.

---

## 2. Constructors

| Constructor | Mô tả |
|------------|-------|
| `PriorityQueue()` | Capacity mặc định = 11, natural ordering |
| `PriorityQueue(int initialCapacity)` | Chỉ định capacity ban đầu |
| `PriorityQueue(int initialCapacity, Comparator c)` | Chỉ định capacity + Comparator |
| `PriorityQueue(Collection c)` | Tạo từ một Collection |
| `PriorityQueue(PriorityQueue c)` | Tạo từ một PriorityQueue khác |
| `PriorityQueue(SortedSet c)` | Tạo từ một SortedSet |

---

## 3. Ví dụ cơ bản

```java
// Natural ordering (tăng dần, task1 → task5)
PriorityQueue<String> tasks = new PriorityQueue<>();
tasks.add("task1");
tasks.add("task4");
tasks.add("task3");
tasks.add("task2");
tasks.add("task5");

// Reverse ordering (giảm dần, task5 → task1)
PriorityQueue<String> reverseTasks = new PriorityQueue<>(Comparator.reverseOrder());
reverseTasks.add("task1");
reverseTasks.add("task4");
reverseTasks.add("task3");
reverseTasks.add("task2");
reverseTasks.add("task5");
```

---

## 4. Các Methods quan trọng

### Thêm phần tử

| Method | Mô tả |
|--------|-------|
| `add(E e)` | Thêm phần tử; ném exception nếu thất bại |
| `offer(E e)` | Thêm phần tử; trả về `false` nếu thất bại (không ném exception) |

### Lấy & xoá phần tử

| Method | Lấy đầu hàng | Xoá đầu hàng | Khi rỗng |
|--------|:------------:|:------------:|:--------:|
| `peek()` | ✅ | ❌ | Trả về `null` |
| `poll()` | ✅ | ✅ | Trả về `null` |

```java
System.out.println(tasks.peek());         // task1 (không xoá)
System.out.println(reverseTasks.peek());  // task5

System.out.println(tasks.poll());         // task1 (xoá khỏi queue)
System.out.println(reverseTasks.poll());  // task5

System.out.println(tasks.peek());         // task2 (task1 đã bị xoá)
System.out.println(reverseTasks.peek());  // task4
```

### Các method khác

| Method | Mô tả |
|--------|-------|
| `contains(Object o)` | Kiểm tra phần tử có tồn tại không |
| `remove(Object o)` | Xoá một phần tử cụ thể (nếu trùng, xoá một cái) |
| `size()` | Số phần tử trong queue |
| `clear()` | Xoá tất cả phần tử |
| `toArray()` | Trả về mảng chứa tất cả phần tử |
| `iterator()` | Trả về iterator (không đảm bảo thứ tự ưu tiên) |
| `comparator()` | Trả về `Comparator` đang dùng, hoặc `null` nếu dùng natural ordering |

```java
System.out.println(tasks.comparator());        // null
System.out.println(reverseTasks.comparator()); // java.util.Collections$ReverseComparator@...

System.out.println(tasks.contains("task3")); // true
```

---

## 5. Time Complexity

| Thao tác | Độ phức tạp |
|---------|-------------|
| `add()` / `offer()` (enqueue) | **O(log n)** |
| `poll()` / `remove()` (dequeue) | **O(log n)** |
| `peek()` (retrieval) | **O(1)** |
| `remove(Object)` / `contains(Object)` | **O(n)** |

---

## 6. Thread Safety

`PriorityQueue` **không thread-safe**.  
→ Dùng **`PriorityBlockingQueue`** nếu cần truy cập đồng thời từ nhiều luồng.

```java
PriorityBlockingQueue<String> safeTasks = new PriorityBlockingQueue<>();
```