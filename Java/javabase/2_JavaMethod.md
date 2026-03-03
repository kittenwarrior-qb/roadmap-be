
## 2. Các phương thức trong Java

Phương thức main là điểm khởi đầu bắt buộc của mọi ứng dụng Java độc lập, nơi JVM (Java Virtual Machine) bắt đầu thực thi chương trình. Cú pháp `public static void main(String[] args)` là bắt buộc và không thể thay đổi.

**Các điểm chính:**

- `public static void main(String[] args)` là điểm vào bắt buộc mà JVM yêu cầu để thực thi ứng dụng
- `static` cho phép JVM gọi phương thức trên class mà không cần tạo đối tượng
- `public` đảm bảo phương thức có thể truy cập từ JVM bên ngoài
- `String[] args` nhận tham số dòng lệnh từ bên ngoài khi chạy chương trình
- Luôn kiểm tra `args.length` trước khi truy cập phần tử để tránh lỗi runtime
- Phương thức main nên đóng vai trò điều phối, ủy thác logic phức tạp cho các phương thức/đối tượng khác
- Có thể thay đổi tên tham số hoặc đổi vị trí `public` và `static`, nhưng không thay đổi ý nghĩa
- Không áp dụng cho môi trường container như web hoặc ứng dụng Android

### Cú pháp phương thức main

Phương thức main là điểm vào để thực thi chương trình Java. Nó có thể chứa code để thực thi hoặc gọi các phương thức khác. Phương thức main có thể đặt trong bất kỳ class nào, nhưng thường đặt trong class có tên `Main` hoặc `Test`.

**Ví dụ:**

```java
public class Test {
    public static void main(String[] args){
        System.out.println("Hello, World!");
    }
}
```

**Cú pháp chuẩn:**

```java
public static void main(String[] args){
    // code của bạn
}
```

Bạn chỉ có thể thay đổi tên của tham số String array (ví dụ: `args` thành `myStringArgs`). Cú pháp tham số có thể viết là: `String... args` hoặc `String args[]`.

### Giải thích các thành phần

#### public

Access modifier phải là `public` để JVM có thể truy cập và thực thi phương thức. Nếu thiếu `public`, JVM không thể tìm thấy phương thức main.

**Ví dụ lỗi:**

```java
public class Test {
    static void main(String[] args){  // Thiếu public
        System.out.println("Hello, World!");
    }
}
```

**Lỗi khi chạy:**
```
Error: Main method not found in class Test, please define the `main` method as:
   public static void main(String[] args)
```

#### static

Khi chương trình Java khởi động, chưa có object nào của class được tạo ra. Phương thức main phải là `static` để JVM có thể load class vào memory và gọi main mà không cần tạo instance của class trước.

**Ví dụ lỗi:**

```java
public class Test {
    public void main(String[] args){  // Thiếu static
        System.out.println("Hello, World!");
    }
}
```

**Lỗi khi chạy:**
```
Error: Main method is not static in class Test, please define the `main` method as:
   public static void main(String[] args)
```

#### void

Mọi phương thức Java phải khai báo kiểu trả về. Phương thức main có kiểu trả về là `void` vì nó không trả về gì. Khi main kết thúc, chương trình Java cũng kết thúc, nên không cần trả về object.

**Ví dụ lỗi:**

```java
public class Test {
    public static void main(String[] args){
        return 0;  // Lỗi: void không được return giá trị
    }
}
```

**Lỗi khi compile:**
```
Test.java:5: error: incompatible types: unexpected return value
    return 0;
           ^
```

#### main

Phương thức luôn phải có tên là `main`. Khi chương trình Java khởi động, nó luôn tìm kiếm phương thức có tên `main`.

**Ví dụ lỗi:**

```java
public class Test {
    public static void myMain(String[] args){  // Sai tên
        System.out.println("Hello, World!");
    }
}
```

**Lỗi khi chạy:**
```
Error: Main method not found in class Test, please define the `main` method as:
   public static void main(String[] args)
```

#### String[] args

Phương thức main nhận một tham số duy nhất kiểu String array. Mỗi string trong array là một command line argument. Bạn có thể sử dụng command line arguments để truyền thông tin vào chương trình khi chạy.

**Ví dụ:**

```java
public class Test {
    public static void main(String[] args){
        for(String s : args){
            System.out.println(s);
        }
    }
}
```

**Chạy với arguments:**

```bash
javac Test.java 
java Test 1 2 3 "Testing the main method"
```

**Kết quả:**
```
1
2
3
Testing the main method
```

### Tại sao cú pháp phương thức main phải chính xác?

JVM hoạt động theo một giao thức chuẩn hóa nghiêm ngặt để khởi chạy ứng dụng. JVM được lập trình để tìm kiếm một phương thức có chữ ký chính xác: `public static void main(String[] args)`. Nếu thiếu chữ ký này, chương trình sẽ không chạy được.

**Hậu quả khi thay đổi chữ ký:**

- **Thiếu `public`**: JVM không thể truy cập phương thức từ bên ngoài class. Lỗi: "Main method not found" hoặc "is not public"
- **Thiếu `static`**: Phương thức trở thành instance method, cần object để gọi. Nhưng JVM không tạo object khi khởi động. Lỗi: "Main method is not static"
- **Thay đổi `void`**: JVM tìm kiếm phương thức trả về `void`. Thay đổi kiểu trả về tạo ra một phương thức hoàn toàn khác
- **Đổi tên hoặc tham số**: JVM chỉ tìm phương thức tên `main` (phân biệt hoa thường) với tham số `String[]`

### Các biến thể hợp lệ

Một số thành phần có thể thay đổi mà không ảnh hưởng đến thực thi:

**1. Tên tham số:**

```java
// Chuẩn
public static void main(String[] args) { }

// Hợp lệ
public static void main(String[] commandLineArgs) { }
```

**2. Cú pháp tham số:**

```java
// Chuẩn (khuyến nghị)
public static void main(String[] args) { }

// Hợp lệ - C-style
public static void main(String args[]) { }

// Hợp lệ - Varargs
public static void main(String... args) { }
```

**3. Thứ tự access modifier:**

```java
// Chuẩn (khuyến nghị)
public static void main(String[] args) { }

// Hợp lệ nhưng không khuyến nghị
static public void main(String[] args) { }
```

### Sử dụng Command-Line Arguments

**Ví dụ thực tế:**

```java
public class GreetUser {
    public static void main(String[] args) {
        String userName = args[0];
        System.out.println("Hello, " + userName + "! Welcome to the program.");
    }
}
```

**Chạy chương trình:**

```bash
javac GreetUser.java
java GreetUser Alice
```

**Kết quả:**
```
Hello, Alice! Welcome to the program.
```

### Các lỗi thường gặp

**1. Main method not found**

```
Error: Main method not found in class YourClassName
```

**Nguyên nhân:**
- Lỗi chính tả tên phương thức (ví dụ: `maim` thay vì `main`)
- Viết hoa sai (ví dụ: `Main` thay vì `main`)
- Sai kiểu trả về (không phải `void`)
- Sai kiểu tham số (không phải `String[]`)

**Cách khắc phục:** Kiểm tra lại chữ ký phải chính xác: `public static void main(String[] args)`

**2. Main method is not static**

```
Error: Main method is not static in class YourClassName
```

**Nguyên nhân:** Thiếu từ khóa `static`

**Cách khắc phục:** Thêm `static` giữa `public` và `void`

**3. ArrayIndexOutOfBoundsException**

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
```

**Nguyên nhân:** Truy cập `args[0]` nhưng không có argument nào được truyền vào

**Cách khắc phục:** Luôn kiểm tra `args.length` trước khi truy cập:

```java
public static void main(String[] args) {
    if (args.length > 0) {
        System.out.println("Hello, " + args[0]);
    } else {
        System.out.println("Please provide a name.");
    }
}
```

**4. Incompatible types: unexpected return value**

```
error: incompatible types: unexpected return value
    return 0;
```

**Nguyên nhân:** Cố gắng return giá trị từ phương thức `void`

**Cách khắc phục:** Xóa câu lệnh return hoặc dùng `System.exit(0)` để thoát chương trình

### Best Practices

**1. Giữ phương thức main gọn gàng**

Phương thức main nên đóng vai trò điều phối, không chứa logic phức tạp:

```java
public class Application {
    public static void main(String[] args) {
        // Chỉ khởi tạo và gọi
        Application app = new Application();
        app.run(args);
    }
    
    private void run(String[] args) {
        // Logic thực sự ở đây
    }
}
```

**2. Xử lý command-line arguments cẩn thận**

```java
public static void main(String[] args) {
    if (args.length < 2) {
        System.out.println("Usage: java Program <name> <age>");
        System.exit(1);
        return;
    }
    
    try {
        String name = args[0];
        int age = Integer.parseInt(args[1]);
        // Xử lý tiếp
    } catch (NumberFormatException e) {
        System.out.println("Age must be a number");
        System.exit(1);
    }
}
```

**3. Sử dụng System.exit() để báo trạng thái**

```java
public static void main(String[] args) {
    try {
        // Logic chương trình
        System.exit(0);  // Thành công
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        System.exit(1);  // Lỗi
    }
}
```

**4. Sử dụng class riêng cho entry point**

Với ứng dụng lớn, tạo class riêng cho main:

```java
// Main.java hoặc Application.java
public class Main {
    public static void main(String[] args) {
        // Entry point
    }
}
```

### Câu hỏi thường gặp (FAQ)

**1. public static void main nghĩa là gì?**

- `public`: Cho phép JVM truy cập từ bên ngoài
- `static`: Thuộc về class, không cần tạo object
- `void`: Không trả về giá trị
- `main`: Tên phương thức mà JVM tìm kiếm
- `String[] args`: Mảng tham số dòng lệnh

**2. Tại sao main phải là static?**

JVM cần gọi main trước khi có bất kỳ object nào được tạo. `static` cho phép gọi phương thức trực tiếp từ class mà không cần object.

**3. String[] args dùng để làm gì?**

Nhận tham số từ command line khi chạy chương trình:

```bash
java MyProgram "John Doe" 99
```

Trong code:
- `args[0]` = "John Doe"
- `args[1]` = "99"

**4. Chương trình không có main sẽ như thế nào?**

Chương trình sẽ compile thành công nhưng không chạy được. JVM sẽ báo lỗi:

```
Error: Main method not found in class MyProgram
```

**5. Một class có thể có nhiều phương thức main không?**

Có, nhờ method overloading. Nhưng JVM chỉ nhận diện `public static void main(String[] args)` làm entry point:

```java
public class MultipleMains {
    // Entry point thực sự
    public static void main(String[] args) {
        System.out.println("Entry point");
        main(5);  // Gọi main khác
    }

    // Overloaded method
    public static void main(int number) {
        System.out.println("Number: " + number);
    }
}
```

**6. Có thể thay đổi chữ ký main không?**

**Được phép thay đổi:**
- Tên tham số: `args` → `myParams`
- Cú pháp array: `String args[]` hoặc `String... args`
- Thứ tự modifier: `static public`

**Không được thay đổi:**
- `public` (không thể là private, protected)
- `static` (không thể là instance method)
- `void` (phải không trả về giá trị)
- `main` (không thể đổi tên)

**7. Tại sao main phải là public chứ không phải private?**

JVM là một tiến trình bên ngoài class. Nếu main là `private`, JVM không thể truy cập để khởi chạy chương trình. `public` đảm bảo JVM có thể gọi phương thức từ bên ngoài.
