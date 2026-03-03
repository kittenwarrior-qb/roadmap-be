# Các lệnh cơ bản

### Biên dịch và chạy chương trình Java

```bash
javac JavaHelloWorldProgram.java
java JavaHelloWorldProgram
```

**Kết quả:**
```
Hello World
```

> Lệnh `javac` sẽ tạo ra file `JavaHelloWorldProgram.class`

### Java 11 trở lên

Với Java 11+, bạn có thể chạy trực tiếp mà không cần biên dịch riêng:

```bash
java JavaHelloWorldProgram.java
```

## Các điểm quan trọng về chương trình Java

- Một file mã nguồn Java có thể chứa nhiều class nhưng chỉ được phép có **một public class duy nhất**.
- Tên file mã nguồn Java phải **trùng với tên public class**. Đó là lý do tại sao tên file chương trình của chúng ta là `JavaHelloWorldProgram.java`

 **Lưu ý quan trọng:** Nếu bạn cố gắng khai báo 2 public class trong cùng một file, Java sẽ **báo lỗi ngay khi biên dịch** (compile-time error), không phải lúc chạy. Lỗi sẽ là:
 ```
 error: class <ClassName> is public, should be declared in a file named <ClassName>.java
 ```
 Ví dụ: Nếu trong file `JavaHelloWorldProgram.java` bạn có:
 ```java
 public class JavaHelloWorldProgram { }
 public class AnotherClass { }
 ```
 Compiler sẽ báo lỗi: `error: class AnotherClass is public, should be declared in a file named AnotherClass.java`
 
 **Quy tắc:** Mỗi file `.java` chỉ được có tối đa 1 public class, và tên file phải trùng với tên public class đó. Các class khác trong cùng file phải là non-public (không có từ khóa `public`).
- Khi biên dịch code, nó sẽ tạo ra bytecode và lưu với định dạng `Class_Name.class`. Nếu bạn kiểm tra thư mục nơi chúng ta biên dịch file java, bạn sẽ thấy một file mới được tạo ra là `JavaHelloWorldProgram.class`
- Khi thực thi file class, chúng ta **không cần cung cấp tên file đầy đủ**. Chúng ta chỉ cần sử dụng tên public class.
- Khi chạy chương trình bằng lệnh `java`, nó sẽ load class vào JVM và tìm kiếm phương thức `main` trong class đó rồi chạy nó. Cú pháp của hàm main phải giống như được chỉ định trong chương trình, nếu không nó sẽ không chạy và ném ra exception: `Exception in thread "main" java.lang.NoSuchMethodError: main`
