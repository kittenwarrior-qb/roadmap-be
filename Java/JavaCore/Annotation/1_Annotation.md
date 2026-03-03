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

## 7. Tổng kết

| Loại              | Mô tả ngắn |
|------------------|------------|
| Custom annotation | Dùng `@interface`, có thể có default value |
| Meta-annotation  | Gắn lên annotation để cấu hình hành vi |
| Built-in         | `@Override`, `@Deprecated`, `@SuppressWarnings`,... |
| Parsing          | Dùng **Reflection API**, cần `RetentionPolicy.RUNTIME` |