# Overriding vs Overloading trong Java

> **Overriding** và **Overloading** là hai cách triển khai **Polymorphism** (đa hình) trong Java — một trong những nguyên tắc cơ bản của OOP.

---

## 1. So sánh nhanh

| Tiêu chí              | Overriding                          | Overloading                        |
|----------------------|-------------------------------------|------------------------------------|
| Loại polymorphism    | Runtime polymorphism                | Compile-time polymorphism          |
| Xác định lúc nào     | Runtime (dựa vào kiểu object)       | Compile time                       |
| Xảy ra ở đâu         | Giữa superclass và subclass         | Trong cùng một class               |
| Signature            | Giống nhau (tên + tham số)          | Cùng tên, khác tham số             |
| Lỗi phát hiện khi    | Runtime                             | Compile time                       |

---

## 2. Định nghĩa

- **Overriding** – Subclass định nghĩa lại method có **cùng signature** (tên + tham số) với superclass.
- **Overloading** – Cùng một class có nhiều method **cùng tên nhưng khác tham số**.

---

## 3. Ví dụ minh hoạ

```java
package com.journaldev.examples;

import java.util.Arrays;

public class Processor {

    // Overloading: 3 method cùng tên, khác tham số
    public void process(int i, int j) {
        System.out.printf("Processing two integers: %d, %d", i, j);
    }

    public void process(int[] ints) {
        System.out.println("Adding integer array: " + Arrays.toString(ints));
    }

    public void process(Object[] objs) {
        System.out.println("Adding object array: " + Arrays.toString(objs));
    }
}

class MathProcessor extends Processor {

    // Overriding: ghi đè method từ Processor
    @Override
    public void process(int i, int j) {
        System.out.println("Sum of integers is " + (i + j));
    }

    @Override
    public void process(int[] ints) {
        int sum = 0;
        for (int i : ints) sum += i;
        System.out.println("Sum of integer array elements is " + sum);
    }
}
```

---

## 4. Phân tích chi tiết

### Overriding

`MathProcessor` ghi đè 2 method từ `Processor`:

```java
// Superclass
public void process(int i, int j) { ... }

// Subclass — @Override
public void process(int i, int j) { ... }
```

```java
// Superclass
public void process(int[] ints) { ... }

// Subclass — @Override
public void process(int[] ints) { ... }
```

> 💡 Luôn dùng `@Override` để compiler báo lỗi nếu signature không khớp với superclass.

### Overloading

`Processor` định nghĩa 3 method cùng tên `process` với tham số khác nhau:

```java
public void process(int i, int j)   { ... }  // nhận 2 int
public void process(int[] ints)     { ... }  // nhận int array
public void process(Object[] objs)  { ... }  // nhận Object array
```

---

## 5. Tổng kết

| Khái niệm    | Khi nào dùng |
|-------------|--------------|
| **Overriding** | Muốn subclass có **hành vi riêng** thay thế superclass |
| **Overloading** | Muốn cùng một method xử lý **nhiều kiểu tham số** khác nhau |