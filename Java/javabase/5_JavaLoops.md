# Java Loops - Tóm Tắt

## Giới Thiệu
Loops (vòng lặp) là cấu trúc để thực thi một đoạn code nhiều lần. Có 3 loại loops chính trong Java: **while**, **do-while**, và **for**.

---

## 1. While Loop

**Đặc điểm:**
- Kiểm tra điều kiện TRƯỚC khi thực thi code block
- Nếu điều kiện false ngay từ đầu → không chạy lần nào
- Phù hợp khi không biết trước số lần lặp

**Cú pháp:**
```java
while (điều_kiện) {
    // code block
}
```

**Ví dụ:**
```java
int x = 3;
while (x > 0) {
    System.out.println("x is " + x--);
}
// Output: x is 3, x is 2, x is 1
```

---

## 2. Do-While Loop

**Đặc điểm:**
- Thực thi code block TRƯỚC, kiểm tra điều kiện SAU
- Luôn chạy ít nhất 1 lần (dù điều kiện false)
- Ít phổ biến hơn while loop
- Hữu ích khi cần chạy code ít nhất 1 lần (ví dụ: menu, nhập liệu)

**Cú pháp:**
```java
do {
    // code block
} while (điều_kiện);
```

**Ví dụ:**
```java
int x = 3;
do {
    System.out.println("x is " + x--);
} while (x < 0);
// Output: x is 3 (chạy 1 lần dù điều kiện false)
```

---

## 3. For Loop

**Đặc điểm:**
- Gộp 3 phần: khởi tạo, điều kiện, cập nhật vào 1 dòng
- Code gọn gàng, dễ đọc hơn
- Thích hợp khi biết trước số lần lặp
- Phổ biến nhất khi làm việc với số đếm

**Cú pháp:**
```java
for (khởi_tạo; điều_kiện; cập_nhật) {
    // code block
}
```

**Ví dụ:**
```java
for (int x = 3; x > 0; x--) {
    System.out.println("x is " + x);
}
// Output: x is 3, x is 2, x is 1
```

---

## 4. For-Each Loop (Enhanced For)

**Đặc điểm:**
- Dùng để duyệt qua collection/array
- Không cần theo dõi index
- Cú pháp đơn giản nhất
- Là chuẩn khi làm việc với collections

**Cú pháp:**
```java
for (kiểu_dữ_liệu biến : collection) {
    // code block
}
```

**Ví dụ:**
```java
int[] numbers = {0, 1, 2};
for (int x : numbers) {
    System.out.println(x);
}
// Output: 0, 1, 2
```

---

## So Sánh 3 Loại Loops Chính

| Tiêu chí | While | Do-While | For |
|----------|-------|----------|-----|
| **Kiểm tra điều kiện** | Trước khi chạy | Sau khi chạy | Trước khi chạy |
| **Số lần chạy tối thiểu** | 0 lần | 1 lần | 0 lần |
| **Khi nào dùng** | Không biết số lần lặp | Cần chạy ít nhất 1 lần | Biết trước số lần lặp |
| **Độ phổ biến** | Cao | Thấp | Rất cao |
| **Cú pháp** | Đơn giản | Đơn giản | Phức tạp hơn nhưng gọn |

---

## Ví Dụ So Sánh Cùng Bài Toán

**Bài toán:** In số từ 1 đến 5

**While:**
```java
int i = 1;
while (i <= 5) {
    System.out.println(i);
    i++;
}
```

**Do-While:**
```java
int i = 1;
do {
    System.out.println(i);
    i++;
} while (i <= 5);
```

**For:**
```java
for (int i = 1; i <= 5; i++) {
    System.out.println(i);
}
```

**Kết quả giống nhau:** 1, 2, 3, 4, 5

---

## Infinite Loop (Vòng Lặp Vô Hạn)

**Cố ý tạo:**
```java
while (true) {
    System.out.println("Chạy mãi mãi");
}
```

**Lỗi không cố ý (thiếu cập nhật biến):**
```java
int x = 3;
while (x > 0) {
    System.out.println("x is " + x); // Quên x--
}
```

**Lưu ý:** Tránh infinite loop vì có thể làm crash chương trình. Luôn đảm bảo có điều kiện thoát!

---

## Kết Luận

- **For loop:** Dùng khi biết số lần lặp (đếm từ 1-10, duyệt array với index)
- **While loop:** Dùng khi không biết số lần lặp (đọc file đến hết, chờ input)
- **Do-while loop:** Dùng khi cần chạy ít nhất 1 lần (menu, validation)
- **For-each loop:** Dùng khi duyệt collection/array (đơn giản nhất)
