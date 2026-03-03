# Java Collections Framework

> Collections Framework là một trong những phần cốt lõi của Java, cung cấp kiến trúc chuẩn để biểu diễn và thao tác với các tập hợp dữ liệu.

---

## 1. Tổng quan

**Collection** là container nhóm nhiều phần tử thành một đơn vị (ví dụ: danh sách tên, giỏ hàng,...).

Java Collections Framework (từ Java 1.2) gồm 3 phần:

| Thành phần             | Mô tả |
|------------------------|-------|
| **Interfaces**         | Kiểu dữ liệu trừu tượng: `Collection`, `List`, `Set`, `Queue`, `Map` |
| **Implementation Classes** | Các class cụ thể: `ArrayList`, `HashMap`, `HashSet`,... |
| **Algorithms**         | Các method tiện ích: sort, search, shuffle,... |

---

## 2. Các Interfaces chính

### Cây kế thừa (đơn giản hoá)

```
Collection
├── List       → ArrayList, LinkedList
├── Set        → HashSet, TreeSet, LinkedHashSet
└── Queue      → PriorityQueue
    └── Deque  → ArrayDeque, LinkedList

Map (không kế thừa Collection)
└── SortedMap  → TreeMap
```

### Chi tiết từng interface

| Interface        | Mô tả ngắn |
|-----------------|------------|
| `Collection`     | Root interface; có `size()`, `add()`, `remove()`, `iterator()` |
| `Iterator`       | Duyệt phần tử; thay thế `Enumeration`; hỗ trợ xoá khi duyệt |
| `List`           | Ordered, cho phép trùng lặp, truy cập theo index |
| `Set`            | Không cho phép trùng lặp |
| `Queue`          | FIFO; có thêm `insert`, `extract`, `inspect` |
| `Deque`          | Double-ended queue; thêm/xoá được ở cả hai đầu |
| `Map`            | Key-value pairs; key không trùng |
| `ListIterator`   | Duyệt `List` 2 chiều, hỗ trợ chỉnh sửa khi duyệt |
| `SortedSet`      | `Set` sắp xếp tăng dần |
| `SortedMap`      | `Map` sắp xếp theo key tăng dần |

---

## 3. Các Implementation Classes

### List – `ArrayList` vs `LinkedList`

| Tiêu chí            | `ArrayList`               | `LinkedList`               |
|--------------------|---------------------------|----------------------------|
| Cấu trúc           | Resizable array           | Doubly-linked list         |
| Random access      | ✅ O(1)                   | ❌ O(n)                    |
| Insert/Delete đầu  | ❌ Chậm O(n)              | ✅ Nhanh O(1)              |
| Dùng cho           | Read nhiều                | Insert/Delete nhiều        |

```java
List<String> strList = new ArrayList<>();
strList.add(0, "A");        // thêm vào đầu
strList.add(1, "B");        // thêm vào index 1
strList.set(1, "C");        // thay thế tại index 1
strList.remove("A");        // xoá theo value
```

### Set – `HashSet` vs `TreeSet`

| Tiêu chí       | `HashSet`                      | `TreeSet`                        |
|---------------|--------------------------------|----------------------------------|
| Thứ tự        | ❌ Không đảm bảo               | ✅ Tăng dần (natural ordering)   |
| Performance   | O(1) cho add/remove/contains   | O(log n) cho add/remove/contains |
| Null element  | ✅                              | ❌                               |

### Map – `HashMap` vs `TreeMap`

| Tiêu chí        | `HashMap`                  | `TreeMap`                     |
|----------------|----------------------------|-------------------------------|
| Thứ tự key     | ❌                          | ✅ Tăng dần                   |
| Performance    | O(1) get/put               | O(log n) get/put              |
| Null key       | ✅ (1 null key)             | ❌                             |

**Các operation cơ bản của Map:** `put`, `get`, `containsKey`, `containsValue`, `size`, `isEmpty`

### `PriorityQueue`

- Xử lý phần tử theo **độ ưu tiên** (không phải FIFO thuần).
- Cần truyền `Comparator` khi khởi tạo.
- Không cho phép `null`.

---

## 4. Collections Utility Class

`java.util.Collections` cung cấp các static method thao tác trên collection:

### Algorithms

| Method             | Mô tả |
|-------------------|-------|
| `sort(list)`       | Sắp xếp tăng dần theo natural ordering |
| `sort(list, cmp)`  | Sắp xếp theo `Comparator` |
| `shuffle(list)`    | Xáo trộn ngẫu nhiên |
| `binarySearch()`   | Tìm kiếm nhị phân (list phải được sort trước) |
| `frequency()`      | Đếm số lần xuất hiện của một phần tử |
| `disjoint()`       | Kiểm tra 2 collection có phần tử chung không |
| `min()` / `max()`  | Lấy giá trị nhỏ nhất / lớn nhất |

### Synchronized Wrappers (Thread-safe)

```java
Collection<T> syncCol   = Collections.synchronizedCollection(c);
List<T>        syncList  = Collections.synchronizedList(list);
Set<T>         syncSet   = Collections.synchronizedSet(s);
Map<K,V>       syncMap   = Collections.synchronizedMap(m);
```

### Unmodifiable Wrappers (Read-only)

```java
Collection<T> unmodCol  = Collections.unmodifiableCollection(c);
List<T>        unmodList = Collections.unmodifiableList(list);
Set<T>         unmodSet  = Collections.unmodifiableSet(s);
Map<K,V>       unmodMap  = Collections.unmodifiableMap(m);
```

---

## 5. Thread-safe Collections (java.util.concurrent)

Khi cần duyệt và chỉnh sửa collection đồng thời (tránh `ConcurrentModificationException`):

| Class                    | Tương đương thread-safe của |
|-------------------------|-----------------------------|
| `CopyOnWriteArrayList`   | `ArrayList`                 |
| `CopyOnWriteArraySet`    | `HashSet`                   |
| `ConcurrentHashMap`      | `HashMap`                   |

---

## 6. Java 8+ Collections API

| Tính năng          | Mô tả |
|-------------------|-------|
| **Stream API**     | Xử lý tuần tự và song song trên collection |
| `forEach()`        | Default method trong `Iterable` để duyệt nhanh |
| **Lambda**         | Rút gọn code khi dùng với Collection API |

---

## 7. Java 10 Collections API

```java
// List.copyOf → tạo unmodifiable list
List<String> actors = new ArrayList<>(List.of("Jack Nicholson", "Marlon Brando"));
List<String> copy = List.copyOf(actors);   // immutable snapshot
// copy.add(...) → UnsupportedOperationException

actors.add("Robert De Niro");
System.out.println(actors); // [Jack Nicholson, Marlon Brando, Robert De Niro]
System.out.println(copy);   // [Jack Nicholson, Marlon Brando]  ← không thay đổi

// Collectors → unmodifiable
List<String> unmod = actors.stream().collect(Collectors.toUnmodifiableList());
```

**Các method mới:** `List.copyOf()`, `Set.copyOf()`, `Map.copyOf()`,  
`Collectors.toUnmodifiableList()`, `toUnmodifiableSet()`, `toUnmodifiableMap()`

---

## 8. Java 11 Collections API

Thêm default method `toArray(IntFunction)` trong `Collection`:

```java
List<String> strList = List.of("Java", "Python", "Android");

String[] arr1 = strList.toArray(size -> new String[size]);
// [Java, Python, Android]

String[] arr2 = strList.toArray(size -> new String[size + 5]);
// [Java, Python, Android, null, null]
```

---

## 9. Bảng tổng kết các Collection Class

| Collection             | Ordered | Random Access | Key-Value | Duplicate | Null | Thread-safe |
|-----------------------|:-------:|:-------------:|:---------:|:---------:|:----:|:-----------:|
| `ArrayList`            | ✅      | ✅            | ❌        | ✅        | ✅   | ❌          |
| `LinkedList`           | ✅      | ❌            | ❌        | ✅        | ✅   | ❌          |
| `HashSet`              | ❌      | ❌            | ❌        | ❌        | ✅   | ❌          |
| `TreeSet`              | ✅      | ❌            | ❌        | ❌        | ❌   | ❌          |
| `HashMap`              | ❌      | ✅            | ✅        | ❌        | ✅   | ❌          |
| `TreeMap`              | ✅      | ✅            | ✅        | ❌        | ❌   | ❌          |
| `Vector`               | ✅      | ✅            | ❌        | ✅        | ✅   | ✅          |
| `Hashtable`            | ❌      | ✅            | ✅        | ❌        | ❌   | ✅          |
| `Stack`                | ✅      | ❌            | ❌        | ✅        | ✅   | ✅          |
| `CopyOnWriteArrayList` | ✅      | ✅            | ❌        | ✅        | ✅   | ✅          |
| `ConcurrentHashMap`    | ❌      | ✅            | ✅        | ❌        | ❌   | ✅          |
| `CopyOnWriteArraySet`  | ❌      | ❌            | ❌        | ❌        | ✅   | ✅          |

---

## 10. Lợi ích của Collections Framework

- **Giảm effort phát triển** – Các collection phổ biến đều có sẵn, tập trung vào business logic.
- **Chất lượng tốt hơn** – Sử dụng class đã được test kỹ thay vì tự implement.
- **Tái sử dụng & tương thích** – API chuẩn, dễ bảo trì và chia sẻ.