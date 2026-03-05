# Tổng Quan Về Bean

## Bean Là Gì?

Spring IoC container quản lý một hoặc nhiều bean. Các bean này được tạo ra từ metadata cấu hình mà bạn cung cấp cho container (ví dụ: dưới dạng định nghĩa XML `<bean/>`).

Bên trong container, các định nghĩa bean được biểu diễn dưới dạng các đối tượng `BeanDefinition`, chứa các metadata sau:

- **Tên class đầy đủ**: Thường là class triển khai thực tế của bean được định nghĩa
- **Cấu hình hành vi của bean**: Xác định cách bean hoạt động trong container (scope, lifecycle callbacks, v.v.)
- **Tham chiếu đến các bean khác**: Các bean cần thiết để bean này hoạt động (còn gọi là collaborators hoặc dependencies)
- **Các cài đặt cấu hình khác**: Ví dụ như giới hạn kích thước pool hoặc số lượng kết nối trong bean quản lý connection pool

Các metadata này được chuyển thành một tập hợp các thuộc tính tạo nên mỗi định nghĩa bean:

### Các Thuộc Tính Của Bean Definition

| Thuộc tính | Giải thích |
|------------|------------|
| Class | Khởi tạo Bean |
| Name | Đặt tên Bean |
| Scope | Phạm vi Bean |
| Constructor arguments | Dependency Injection |
| Properties | Dependency Injection |
| Autowiring mode | Autowiring Collaborators |
| Lazy initialization mode | Lazy-initialized Beans |
| Initialization method | Initialization Callbacks |
| Destruction method | Destruction Callbacks |

### Đăng Ký Bean Từ Bên Ngoài Container

Ngoài việc định nghĩa bean thông thường, `ApplicationContext` còn cho phép đăng ký các đối tượng đã tồn tại được tạo bên ngoài container. Điều này thực hiện thông qua:

- Truy cập `BeanFactory` của `ApplicationContext` qua phương thức `getAutowireCapableBeanFactory()`
- Sử dụng các phương thức `registerSingleton(..)` và `registerBeanDefinition(..)` của `DefaultListableBeanFactory`

**Lưu ý quan trọng:**
- Bean metadata và singleton instances cần được đăng ký càng sớm càng tốt
- Đăng ký bean mới tại runtime (đồng thời với việc truy cập factory) không được hỗ trợ chính thức và có thể gây ra lỗi đồng thời hoặc trạng thái không nhất quán

## Ghi Đè Bean (Bean Overriding)

Bean overriding xảy ra khi một bean được đăng ký với identifier đã tồn tại. Mặc dù có thể ghi đè bean, nhưng điều này làm cấu hình khó đọc hơn.

> ⚠️ **Cảnh báo**: Bean overriding sẽ bị deprecated trong các phiên bản tương lai.

### Cách Vô Hiệu Hóa Bean Overriding

Đặt flag `allowBeanDefinitionOverriding` thành `false` trên `ApplicationContext` trước khi refresh. Khi đó, exception sẽ được throw nếu có bean overriding.

### Logging

Mặc định, container ghi log mỗi lần cố gắng ghi đè bean ở mức INFO. Bạn có thể tắt log này bằng cách đặt `allowBeanDefinitionOverriding` thành `true` (không khuyến nghị).

### Java Configuration

Khi sử dụng Java Configuration, phương thức `@Bean` luôn ghi đè bean được scan với cùng tên component, miễn là kiểu trả về của `@Bean` khớp với bean class đó. Container sẽ ưu tiên gọi `@Bean` factory method thay vì constructor của bean class.

**Lưu ý**: Ghi đè bean trong test scenarios là tiện lợi và được hỗ trợ rõ ràng.

## Đặt Tên Bean

### Quy Tắc Cơ Bản

Mỗi bean có một hoặc nhiều identifier, phải là duy nhất trong container. Thông thường bean chỉ có một identifier, các identifier bổ sung được coi là alias.

### Cấu Hình XML

Trong XML-based configuration, sử dụng:
- Thuộc tính `id`: Chỉ định chính xác một id
- Thuộc tính `name`: Chỉ định các alias, phân cách bằng dấu phẩy (,), chấm phẩy (;) hoặc khoảng trắng

**Quy ước đặt tên**: Tên thường là chữ và số (`myBean`, `someService`), nhưng cũng có thể chứa ký tự đặc biệt.

### Tự Động Sinh Tên

Bạn không bắt buộc phải cung cấp `name` hoặc `id`. Nếu không cung cấp, container sẽ tự động sinh tên duy nhất cho bean đó.

**Khi nào cần cung cấp tên?**
- Khi muốn tham chiếu bean qua phần tử `ref`
- Khi sử dụng Service Locator style lookup

**Khi nào không cần tên?**
- Sử dụng inner beans
- Autowiring collaborators

### Quy Ước Đặt Tên Bean

Sử dụng quy ước Java chuẩn cho tên instance field:
- Bắt đầu bằng chữ thường
- Sử dụng camelCase

**Ví dụ**: `accountManager`, `accountService`, `userDao`, `loginController`

**Lợi ích**:
- Cấu hình dễ đọc và dễ hiểu hơn
- Hữu ích khi sử dụng Spring AOP để apply advice cho nhóm bean có tên liên quan

### Component Scanning

Khi scan component trong classpath, Spring tự động sinh tên bean theo quy tắc:
- Lấy tên class đơn giản
- Chuyển ký tự đầu tiên thành chữ thường

**Trường hợp đặc biệt**: Nếu cả ký tự thứ nhất và thứ hai đều viết hoa, giữ nguyên casing gốc.

> Đây là quy tắc của `java.beans.Introspector.decapitalize` mà Spring sử dụng.

## Tạo Alias Cho Bean

### Alias Trong Bean Definition

Trong bean definition, bạn có thể cung cấp nhiều tên cho bean:
- Một tên qua thuộc tính `id`
- Nhiều tên khác qua thuộc tính `name`

Các tên này là alias tương đương, hữu ích khi mỗi component trong ứng dụng muốn tham chiếu đến dependency chung bằng tên riêng của component đó.

### Alias Bên Ngoài Bean Definition

Đôi khi cần tạo alias cho bean được định nghĩa ở nơi khác. Điều này phổ biến trong hệ thống lớn khi cấu hình được chia cho từng subsystem.

Trong XML configuration, sử dụng phần tử `<alias/>`:

```xml
<alias name="fromName" alias="toName"/>
```

Bean có tên `fromName` giờ có thể được tham chiếu bằng tên `toName`.

**Ví dụ thực tế**:
- Subsystem A tham chiếu DataSource bằng tên `subsystemA-dataSource`
- Subsystem B tham chiếu DataSource bằng tên `subsystemB-dataSource`
- Main application tham chiếu DataSource bằng tên `myApp-dataSource`

Để cả ba tên đều tham chiếu đến cùng một object, thêm alias definitions:

```xml
<alias name="myApp-dataSource" alias="subsystemA-dataSource"/>
<alias name="myApp-dataSource" alias="subsystemB-dataSource"/>
```

Giờ mỗi component và main application có thể tham chiếu dataSource qua tên riêng (tạo namespace), nhưng đều trỏ đến cùng một bean.

### Java Configuration

Với Java Configuration, sử dụng annotation `@Bean` để cung cấp alias.

## Khởi Tạo Bean

Bean definition về bản chất là "công thức" để tạo một hoặc nhiều object. Container sử dụng công thức này để tạo (hoặc lấy) object thực tế.

### Cấu Hình XML

Trong XML configuration, chỉ định type (hoặc class) của object cần khởi tạo trong thuộc tính `class` của phần tử `<bean/>`.

Thuộc tính `class` có thể được sử dụng theo hai cách:

1. **Khởi tạo trực tiếp qua constructor**: Container tạo bean bằng cách gọi constructor reflectively (tương tự toán tử `new` trong Java)

2. **Khởi tạo qua static factory method**: Container gọi static factory method trên class để tạo bean. Object trả về có thể cùng class hoặc class khác hoàn toàn.

### Nested Class Names

Khi cấu hình bean definition cho nested class, có thể sử dụng binary name hoặc source name.

**Ví dụ**: Class `SomeThing` trong package `com.example` có static nested class `OtherThing`

Có thể phân cách bằng dấu dollar ($) hoặc dấu chấm (.):
- `com.example.SomeThing$OtherThing`
- `com.example.SomeThing.OtherThing`

### Khởi Tạo Qua Constructor

Khi tạo bean qua constructor, mọi class thông thường đều tương thích với Spring. Class không cần implement interface đặc biệt hay được code theo cách cụ thể. Chỉ cần chỉ định bean class là đủ.

**Lưu ý**: Tùy loại IoC sử dụng, có thể cần default (empty) constructor.

Spring IoC container có thể quản lý hầu hết mọi class:
- Không giới hạn ở JavaBeans chuẩn
- Hỗ trợ cả non-bean-style classes
- Có thể quản lý legacy connection pool không tuân theo JavaBean specification

Hầu hết người dùng Spring ưa thích JavaBeans với:
- Default (no-argument) constructor
- Setters và getters phù hợp cho các properties

**Cú pháp XML**:

```xml
<bean id="exampleBean" class="examples.ExampleBean"/>
<bean name="anotherExample" class="examples.ExampleBeanTwo"/>
```

**Lưu ý về constructor arguments**:
- Container có thể chọn constructor phù hợp trong số nhiều overloaded constructors
- Khuyến nghị giữ constructor signatures đơn giản nhất có thể để tránh nhập nhằng

### Khởi Tạo Qua Static Factory Method

Khi định nghĩa bean tạo qua static factory method:
- Thuộc tính `class`: Chỉ định class chứa static factory method
- Thuộc tính `factory-method`: Chỉ định tên của factory method

Phương thức này được gọi (với optional arguments) và trả về object, sau đó được xử lý như thể được tạo qua constructor.

**Use case**: Gọi static factories trong legacy code.

**Ví dụ**: Bean definition chỉ định bean sẽ được tạo bằng cách gọi factory method. Definition không chỉ định type của returned object, mà chỉ định class chứa factory method.

```xml
<bean id="clientService"
    class="examples.ClientService"
    factory-method="createInstance"/>
```

**Class tương ứng**:

```java
public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}

    public static ClientService createInstance() {
        return clientService;
    }
}
```

**Lưu ý về factory method arguments**:
- Container có thể chọn method phù hợp trong số nhiều overloaded methods cùng tên
- Khuyến nghị giữ factory method signatures đơn giản để tránh nhập nhằng

**Trường hợp phổ biến gặp vấn đề**: Mockito với nhiều overloads của method `mock`. Chọn variant cụ thể nhất có thể:

```xml
<bean id="clientService" class="org.mockito.Mockito" factory-method="mock">
    <constructor-arg type="java.lang.Class" value="examples.ClientService"/>
    <constructor-arg type="java.lang.String" value="clientService"/>
</bean>
```

### Khởi Tạo Qua Instance Factory Method

Tương tự static factory method, nhưng gọi non-static method của bean đã tồn tại trong container để tạo bean mới.

**Cách sử dụng**:
- Để trống thuộc tính `class`
- Thuộc tính `factory-bean`: Chỉ định tên bean chứa instance method
- Thuộc tính `factory-method`: Chỉ định tên factory method

**Ví dụ**:

```xml
<!-- Factory bean chứa method createClientServiceInstance() -->
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject các dependencies cần thiết cho locator bean -->
</bean>

<!-- Bean được tạo thông qua factory bean -->
<bean id="clientService"
    factory-bean="serviceLocator"
    factory-method="createClientServiceInstance"/>
```

**Class tương ứng**:

```java
public class DefaultServiceLocator {
    private static ClientService clientService = new ClientServiceImpl();

    public ClientService createClientServiceInstance() {
        return clientService;
    }
}
```

### Nhiều Factory Methods

Một factory class có thể chứa nhiều factory method:

```xml
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject các dependencies cần thiết -->
</bean>

<bean id="clientService"
    factory-bean="serviceLocator"
    factory-method="createClientServiceInstance"/>

<bean id="accountService"
    factory-bean="serviceLocator"
    factory-method="createAccountServiceInstance"/>
```

**Class tương ứng**:

```java
public class DefaultServiceLocator {
    private static ClientService clientService = new ClientServiceImpl();
    private static AccountService accountService = new AccountServiceImpl();

    public ClientService createClientServiceInstance() {
        return clientService;
    }

    public AccountService createAccountServiceInstance() {
        return accountService;
    }
}
```

Cách tiếp cận này cho thấy factory bean có thể được quản lý và cấu hình thông qua dependency injection (DI).

### Phân Biệt "factory bean" và "FactoryBean"

- **"factory bean"**: Bean được cấu hình trong Spring container và tạo objects thông qua instance hoặc static factory method
- **"FactoryBean"** (chú ý viết hoa): Class implementation cụ thể của Spring

## Xác Định Runtime Type Của Bean

Runtime type của một bean cụ thể không đơn giản để xác định. Class được chỉ định trong bean metadata chỉ là tham chiếu class ban đầu, có thể kết hợp với:
- Declared factory method
- `FactoryBean` class (dẫn đến runtime type khác)
- Instance-level factory method (được resolve qua `factory-bean` name)
- AOP proxying (có thể wrap bean instance với interface-based proxy)

**Cách khuyến nghị**: Sử dụng `BeanFactory.getType()` với bean name cụ thể. Phương thức này xem xét tất cả các trường hợp trên và trả về type của object mà `BeanFactory.getBean()` sẽ trả về cho cùng bean name đó.
