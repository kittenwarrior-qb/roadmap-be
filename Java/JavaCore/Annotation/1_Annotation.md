# Java Annotations

> Annotation cung cấp **metadata** về code, không ảnh hưởng trực tiếp đến logic chương trình.  
> Được giới thiệu từ **Java 1.5**, nay được dùng rộng rãi trong Hibernate, Jersey, Spring.

---

## 1. Tổng quan

Annotation được nhúng vào chương trình và có thể được:
- Xử lý bởi **compiler** (compile-time)
- Đọc tại **runtime** thông qua Reflection API

**Ví dụ thực tế:** Trong Jersey, annotation `@PATH` được gắn vào method; khi runtime, Jersey đọc nó để xác định method nào xử lý URI nào.

---

## 2. Custom Annotation

Khai báo custom annotation giống interface, nhưng dùng `@interface`:

```java
package com.journaldev.annotations;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodInfo {
    String author() default "Pankaj";
    String date();
    int revision() default 1;
    String comments();
}
```

**Quy tắc khi định nghĩa annotation:**

- Method **không có tham số**
- Kiểu trả về chỉ được là: `primitives`, `String`, `Enum`, `Annotation`, hoặc **array** của các loại trên
- Có thể có **default value**
- Có thể gắn **meta-annotation** lên annotation

---

## 3. Meta-Annotations

| Annotation      | Mô tả |
|----------------|-------|
| `@Documented`  | Đưa annotation vào Javadoc |
| `@Target`      | Xác định nơi áp dụng: `TYPE`, `METHOD`, `FIELD`, `CONSTRUCTOR`,... |
| `@Inherited`   | Tự động kế thừa annotation từ superclass |
| `@Retention`   | Xác định thời gian tồn tại: `SOURCE`, `CLASS`, `RUNTIME` |
| `@Repeatable`  | Cho phép dùng annotation nhiều lần trên cùng một phần tử |

---

## 4. Built-in Annotations

| Annotation           | Mô tả |
|---------------------|-------|
| `@Override`          | Báo compiler rằng method đang override superclass → lỗi nếu superclass thay đổi |
| `@Deprecated`        | Đánh dấu method/class lỗi thời, nên ghi rõ lý do trong Javadoc |
| `@SuppressWarnings`  | Tắt cảnh báo cụ thể của compiler (e.g. raw types); Retention = `SOURCE` |
| `@FunctionalInterface` | Đánh dấu interface là functional interface (Java 8+) |
| `@SafeVarargs`       | Xác nhận method/constructor không thực hiện thao tác không an toàn trên varargs |

---

## 5. Ví dụ sử dụng Annotation

```java
package com.journaldev.annotations;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class AnnotationExample {

    public static void main(String[] args) {}

    @Override
    @MethodInfo(author = "Pankaj", comments = "Main method", date = "Nov 17 2012", revision = 1)
    public String toString() {
        return "Overriden toString method";
    }

    @Deprecated
    @MethodInfo(comments = "deprecated method", date = "Nov 17 2012")
    public static void oldMethod() {
        System.out.println("old method, don't use it.");
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    @MethodInfo(author = "Pankaj", comments = "Main method", date = "Nov 17 2012", revision = 10)
    public static void genericsTest() throws FileNotFoundException {
        List l = new ArrayList();
        l.add("abc");
        oldMethod();
    }
}
```

---

## 6. Parsing Annotation với Reflection

> ⚠️ Retention Policy phải là **RUNTIME** thì mới đọc được annotation tại runtime.

```java
package com.journaldev.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationParsing {

    public static void main(String[] args) {
        try {
            Class<?> clazz = AnnotationParsing.class.getClassLoader()
                    .loadClass("com.journaldev.annotations.AnnotationExample");

            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(MethodInfo.class)) {
                    try {
                        for (Annotation anno : method.getDeclaredAnnotations()) {
                            System.out.println("Annotation in Method '" + method + "' : " + anno);
                        }
                        MethodInfo methodAnno = method.getAnnotation(MethodInfo.class);
                        if (methodAnno.revision() == 1) {
                            System.out.println("Method with revision no 1 = " + method);
                        }
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

**Output:**

```
Annotation in Method 'public java.lang.String ...AnnotationExample.toString()' :
  @...MethodInfo(author=Pankaj, revision=1, comments=Main method, date=Nov 17 2012)
Method with revision no 1 = public java.lang.String ...AnnotationExample.toString()

Annotation in Method 'public static void ...AnnotationExample.oldMethod()' :
  @java.lang.Deprecated()
Annotation in Method 'public static void ...AnnotationExample.oldMethod()' :
  @...MethodInfo(author=Pankaj, revision=1, comments=deprecated method, date=Nov 17 2012)
Method with revision no 1 = public static void ...AnnotationExample.oldMethod()
```

---

## 7. Lombok Annotations

> **Lombok** là thư viện giúp giảm boilerplate code bằng cách tự động sinh getter, setter, constructor, builder,...

### 7.1. Các Annotation phổ biến

| Annotation | Mô tả |
|-----------|-------|
| `@Getter` | Tự động tạo getter cho tất cả field: `getId()`, `getName()`, `getVenue()`... |
| `@Setter` | Tự động tạo setter cho tất cả field: `setId()`, `setName()`, `setVenue()`... |
| `@NoArgsConstructor` | Tự động tạo constructor không tham số: `new Event()` |
| `@AllArgsConstructor` | Tự động tạo constructor đủ tất cả field: `new Event(id, name, venue, ...)` |
| `@Builder` | Tự động tạo Builder pattern để tạo object theo kiểu chain |

### 7.2. Ví dụ sử dụng Lombok

**Không dùng Lombok:**

```java
public class Event {
    private Long id;
    private String name;
    private String venue;
    
    // Constructor không tham số
    public Event() {}
    
    // Constructor đầy đủ
    public Event(Long id, String name, String venue) {
        this.id = id;
        this.name = name;
        this.venue = venue;
    }
    
    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getVenue() { return venue; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setVenue(String venue) { this.venue = venue; }
}
```

**Dùng Lombok:**

```java
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    private Long id;
    private String name;
    private String venue;
}
```

### 7.3. Sử dụng Builder Pattern

```java
// Tạo object bằng Builder
Event event = Event.builder()
    .id(1L)
    .name("Spring Concert")
    .venue("National Stadium")
    .build();

// Hoặc dùng constructor
Event event2 = new Event(1L, "Spring Concert", "National Stadium");

// Hoặc dùng setter
Event event3 = new Event();
event3.setId(1L);
event3.setName("Spring Concert");
event3.setVenue("National Stadium");
```

### 7.4. Các Annotation Lombok khác

| Annotation | Mô tả |
|-----------|-------|
| `@Data` | Kết hợp `@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`, `@RequiredArgsConstructor` |
| `@ToString` | Tự động tạo method `toString()` |
| `@EqualsAndHashCode` | Tự động tạo `equals()` và `hashCode()` |
| `@RequiredArgsConstructor` | Tạo constructor cho các field `final` hoặc `@NonNull` |
| `@Value` | Tạo immutable class (tất cả field là `final`, chỉ có getter) |
| `@Slf4j` | Tự động tạo logger: `private static final Logger log = ...` |

---

## 8. Tổng kết

| Loại              | Mô tả ngắn |
|------------------|------------|
| Custom annotation | Dùng `@interface`, có thể có default value |
| Meta-annotation  | Gắn lên annotation để cấu hình hành vi |
| Built-in         | `@Override`, `@Deprecated`, `@SuppressWarnings`,... |
| Lombok           | `@Getter`, `@Setter`, `@Builder`,... giảm boilerplate code |
| Parsing          | Dùng **Reflection API**, cần `RetentionPolicy.RUNTIME` |