# Java IO – Tạo, Đọc, Ghi, Copy, Move File

> Java cung cấp 2 API chính để thao tác file:  
> - **`java.io.File`** – API cũ (Java 1.0+)  
> - **`java.nio.file.Files`** + `Path` – API mới, hiện đại hơn (Java 7+)

---

## 1. Tạo File

### Cách 1: `File.createNewFile()`

```java
String sep = System.getProperty("file.separator");

// Đường dẫn tuyệt đối
File file = new File(sep + "Users" + sep + "pankaj" + sep + "file.txt");
file.createNewFile(); // true nếu tạo thành công, false nếu đã tồn tại

// Chỉ tên file (tạo tại thư mục project root)
new File("file.txt").createNewFile();

// Relative path (thư mục tmp phải tồn tại sẵn)
new File("tmp" + sep + "file.txt").createNewFile();
```

> ⚠️ Ném `IOException` nếu thư mục cha chưa tồn tại.

### Cách 2: `FileOutputStream` (tạo và ghi cùng lúc)

```java
FileOutputStream fos = new FileOutputStream("name.txt");
fos.write("Hello".getBytes());
fos.flush();
fos.close();
```

### Cách 3: `Files.write()` – NIO (khuyến nghị)

```java
Files.write(Paths.get("name.txt"), "Hello".getBytes());
// Tự động đóng resource, không cần try-finally
```

---

## 2. `java.nio.file` – Path & Files

### Tạo `Path`

```java
Path path1 = Paths.get("/tmp/file.txt");           // UNIX
Path path2 = Paths.get("D:/data/file.txt");        // Windows
Path path3 = Paths.get("D:", "data", "file.txt");  // Tách từng phần
```

### Chuyển đổi `File` ↔ `Path`

```java
File file   = new File("D:/data/file.txt");
Path path   = file.toPath();
File file2  = path.toFile();
```

---

## 3. Các thao tác phổ biến với `Files`

### Tạo file / thư mục

```java
Files.createFile(Paths.get("D:/data/file.txt"));
// FileAlreadyExistsException nếu file đã tồn tại

Files.createDirectory(Paths.get("D:/pankaj"));          // 1 cấp
Files.createDirectories(Paths.get("D:/pankaj/java/nio")); // Nhiều cấp, không lỗi nếu đã tồn tại
```

### Đọc file

```java
Path path = Paths.get("D:/data/file.txt");

byte[]        bytes   = Files.readAllBytes(path);       // Đọc toàn bộ bytes
List<String>  lines   = Files.readAllLines(path);       // Đọc từng dòng
```

### Ghi file

```java
Files.write(Paths.get("D:/data/test.txt"), "Hello World".getBytes());
// Mặc định: CREATE + TRUNCATE_EXISTING + WRITE
```

### Copy file

```java
Files.copy(
    Paths.get("D:/data/source.txt"),
    Paths.get("D:/data/target.txt"),
    StandardCopyOption.REPLACE_EXISTING  // Ghi đè nếu tồn tại
);
```

### Move / Rename file

```java
Files.move(
    Paths.get("D:/data/source.txt"),
    Paths.get("D:/data/target.txt"),
    StandardCopyOption.REPLACE_EXISTING
    // StandardCopyOption.ATOMIC_MOVE — move nguyên tử, bỏ qua các option khác
);
```

### Xoá file

```java
Files.delete(path);         // Ném NoSuchFileException nếu không tồn tại
Files.deleteIfExists(path); // Trả về false nếu không tồn tại, không ném exception
```

---

## 4. Duyệt thư mục – `walkFileTree`

```java
Files.walkFileTree(Paths.get("D:/pankaj"), new FileVisitor<Path>() {

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        System.out.println("Entering dir: " + dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        System.out.println("File: " + file);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        System.out.println("Leaving dir: " + dir);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }
});
```

> Duyệt **đệ quy** toàn bộ thư mục — hữu ích khi cần xử lý hàng loạt file (rename, delete, scan,...).

---

## 5. Bảng tổng hợp `Files` Methods

| Thao tác | Method | Ghi chú |
|---------|--------|---------|
| Tạo file | `createFile(path)` | Lỗi nếu đã tồn tại |
| Tạo thư mục | `createDirectory(path)` | 1 cấp |
| Tạo thư mục đa cấp | `createDirectories(path)` | Không lỗi nếu đã tồn tại |
| Đọc bytes | `readAllBytes(path)` | Trả về `byte[]` |
| Đọc lines | `readAllLines(path)` | Trả về `List<String>` |
| Ghi file | `write(path, bytes)` | Tạo mới hoặc ghi đè |
| Copy | `copy(src, dst, options)` | `REPLACE_EXISTING` |
| Move | `move(src, dst, options)` | `REPLACE_EXISTING`, `ATOMIC_MOVE` |
| Xoá | `delete(path)` | Ném exception nếu không có |
| Xoá an toàn | `deleteIfExists(path)` | Trả về `boolean` |
| Kiểm tra tồn tại | `exists(path)` | `true` / `false` |
| Duyệt thư mục | `walkFileTree(path, visitor)` | Đệ quy toàn bộ cây thư mục |
| Temp file | `createTempFile(prefix, suffix)` | Trong thư mục temp mặc định |
| Temp dir | `createTempDirectory(prefix)` | Trong thư mục temp mặc định |

---

## 6. So sánh `java.io` vs `java.nio`

| Tiêu chí | `java.io.File` | `java.nio.file.Files` |
|---------|---------------|-----------------------|
| Java version | 1.0+ | 1.7+ |
| Quản lý resource | Thủ công (đóng stream) | Tự động |
| Exception rõ ràng | ❌ Chỉ `IOException` | ✅ `NoSuchFileException`, `FileAlreadyExistsException`,... |
| Hiệu năng | Thấp hơn | Cao hơn (non-blocking I/O) |
| Khuyến nghị | Legacy code | ✅ Dùng cho dự án mới |