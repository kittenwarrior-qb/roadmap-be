# Java Generics

> Generics được thêm vào **Java 5** để cung cấp **type-checking lúc compile-time**, loại bỏ nguy cơ `ClassCastException` khi làm việc với Collections.

---

## 1. Tại sao cần Generics?

**Trước Generics (không an toàn):**

```java
List list = new ArrayList();
list.add("abc");
list.add(new Integer(5)); // OK — không báo lỗi

for (Object obj : list) {
    String str = (String) obj; // ClassCastException tại runtime!
}
```

**Sau Generics (type-safe):**

```java
List<String> list = new ArrayList<>();
list.add("abc");
// list.add(new Integer(5)); // Compile-time error ✅

for (String str : list) {
    // Không cần cast, không có ClassCastException
}
```

---

## 2. Generic Class

**Không dùng Generics** → phải cast thủ công, dễ lỗi:

```java
public class GenericsTypeOld {
    private Object t;

    public Object get() { return t; }
    public void set(Object t) { this.t = t; }

    public static void main(String[] args) {
        GenericsTypeOld type = new GenericsTypeOld();
        type.set("Pankaj");
        String str = (String) type.get(); // Phải cast, dễ ClassCastException
    }
}
```

**Dùng Generics** → không cần cast, an toàn:

```java
public class GenericsType<T> {
    private T t;

    public T get() { return this.t; }
    public void set(T t1) { this.t = t1; }

    public static void main(String[] args) {
        GenericsType<String> type = new GenericsType<>();
        type.set("Pankaj"); // OK

        GenericsType type1 = new GenericsType(); // Raw type — nên tránh
        type1.set("Pankaj"); // OK
        type1.set(10);       // OK (autoboxing), nhưng không type-safe
    }
}
```

> 💡 Dùng `@SuppressWarnings("rawtypes")` để tắt cảnh báo raw type nếu cần.

---

## 3. Generic Interface

```java
public interface Comparable<T> {
    public int compareTo(T o);
}
```

Có thể dùng nhiều type parameters cùng lúc:

```java
Map<String, List<String>> map = new HashMap<>();
```

---

## 4. Naming Convention

| Ký hiệu | Ý nghĩa |
|---------|---------|
| `E` | Element – dùng trong Collections (`ArrayList`, `Set`,...) |
| `K` | Key – dùng trong `Map` |
| `V` | Value – dùng trong `Map` |
| `N` | Number |
| `T` | Type (type chung) |
| `S`, `U`, `V` | Type thứ 2, 3, 4 |

---

## 5. Generic Method

```java
public class GenericsMethods {

    public static <T> boolean isEqual(GenericsType<T> g1, GenericsType<T> g2) {
        return g1.get().equals(g2.get());
    }

    public static void main(String[] args) {
        GenericsType<String> g1 = new GenericsType<>();
        g1.set("Pankaj");
        GenericsType<String> g2 = new GenericsType<>();
        g2.set("Pankaj");

        // Cách 1: chỉ định type rõ ràng
        boolean result = GenericsMethods.<String>isEqual(g1, g2);

        // Cách 2: để compiler tự infer (type inference)
        result = GenericsMethods.isEqual(g1, g2);
    }
}
```

---

## 6. Bounded Type Parameters

Giới hạn kiểu type có thể truyền vào:

```java
// T phải implement Comparable
public static <T extends Comparable<T>> int compare(T t1, T t2) {
    return t1.compareTo(t2);
}
```

**Multiple bounds:**

```java
<T extends A & B & C>
// A có thể là class hoặc interface
// B, C phải là interface
// Không được có nhiều hơn 1 class
```

---

## 7. Generics và Inheritance

```java
String str = "abc";
Object obj = str;          // OK — String is-a Object

MyClass<String> myClass1 = new MyClass<>();
MyClass<Object> myClass2 = new MyClass<>();
// myClass2 = myClass1;   // Compile error! MyClass<String> ≠ MyClass<Object>
obj = myClass1;            // OK — MyClass<T> kế thừa Object

public static class MyClass<T> {}
```

> ⚠️ `MyClass<String>` **không phải** subtype của `MyClass<Object>`, dù `String` là subtype của `Object`.

**Subtyping hợp lệ:**

```java
// ArrayList<String> là subtype của List<String> → subtype của Collection<String>
interface MyList<E, T> extends List<E> {}

// MyList<String, Object> và MyList<String, Integer> đều là subtype của List<String>
```

---

## 8. Wildcards (`?`)

`?` đại diện cho **kiểu không xác định**. Có 3 loại:

### Upper Bounded – `<? extends T>` (chỉ đọc)

Cho phép truyền `T` hoặc **subtype** của `T`:

```java
// ❌ Không nhận List<Integer> hay List<Double>
public static double sum(List<Number> list) { ... }

// ✅ Nhận mọi subtype của Number (Integer, Double, Float,...)
public static double sum(List<? extends Number> list) {
    double sum = 0;
    for (Number n : list) sum += n.doubleValue();
    return sum;
}
```

> ⚠️ Với upper bounded list: **không được thêm phần tử** (trừ `null`).

### Unbounded – `<?>`

Tương đương `<? extends Object>`, dùng khi không quan tâm đến kiểu:

```java
public static void printData(List<?> list) {
    for (Object obj : list) System.out.print(obj + " ");
}
```

### Lower Bounded – `<? super T>` (cho phép thêm)

Cho phép truyền `T` hoặc **supertype** của `T`, và **được phép thêm phần tử**:

```java
public static void addIntegers(List<? super Integer> list) {
    list.add(50); // OK
}
```

**Subtyping với Wildcard:**

```java
List<? extends Integer> intList = new ArrayList<>();
List<? extends Number>  numList = intList; // OK — hợp lệ
```

### Tóm tắt Wildcards

| Wildcard | Đọc | Thêm | Dùng khi |
|---------|:---:|:----:|---------|
| `<? extends T>` | ✅ | ❌ | Chỉ cần đọc dữ liệu |
| `<?>` | ✅ | ❌ | Không quan tâm kiểu |
| `<? super T>` | ⚠️ (Object) | ✅ | Cần ghi/thêm dữ liệu |

---

## 9. Type Erasure

Generics chỉ tồn tại ở **compile-time**. JVM xoá thông tin generic (type erasure) và thay bằng cast khi cần → **không tốn overhead lúc runtime**.

**Trước type erasure:**

```java
public class Test<T extends Comparable<T>> {
    private T data;
    private Test<T> next;

    public T getData() { return this.data; }
}
```

**Sau type erasure (bytecode):**

```java
public class Test {
    private Comparable data;
    private Test next;

    public Comparable getData() { return data; }
}
```

---

## 10. Các lỗi thường gặp

### ❌ Dùng Raw Type

```java
List list = new ArrayList(); // Không type-safe
list.add("Hello");
list.add(123);               // Không bị báo lỗi
```

✅ Thay bằng:

```java
List<String> list = new ArrayList<>();
```

### ❌ Dùng sai Wildcard

```java
// Không thể add vào list với upper bounded wildcard
public void addItem(List<? extends Number> list) {
    // list.add(10); // Compile error
}
```

✅ Dùng lower bounded nếu cần thêm:

```java
public void addItem(List<? super Integer> list) {
    list.add(10); // OK
}
```

### ❌ Các hạn chế cần nhớ

```java
class Box<T> {
    // T obj = new T();      // Illegal — không thể tạo instance của T
    // static T instance;    // Illegal — không có static member kiểu T
}

// List<int> list = new ArrayList<>();  // Illegal — không dùng primitive
List<Integer> list = new ArrayList<>(); // Dùng wrapper class
```

---

## 11. Tổng kết

| Tính năng | Mô tả |
|----------|-------|
| **Generic Class** | `class Box<T>` — parameterize toàn bộ class |
| **Generic Interface** | `interface Comparable<T>` — áp dụng cho interface |
| **Generic Method** | `<T> void method(T t)` — chỉ parameterize method |
| **Bounded Type** | `<T extends Number>` — giới hạn kiểu được phép |
| **Wildcard** | `?`, `? extends T`, `? super T` — tăng tính linh hoạt |
| **Type Erasure** | Thông tin generic bị xoá lúc runtime, không overhead |

**Lợi ích:**
- ✅ **Type Safety** – phát hiện lỗi tại compile-time
- ✅ **Tái sử dụng** – một class/method hoạt động với nhiều kiểu
- ✅ **Readable** – không cần cast thủ công