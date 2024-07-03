# Assignment 1

## 1. What are the advantages and drawbacks of Dependency Injection (DI)?

### Advantages:
1. **Decoupling:** DI promotes loose coupling between classes, making code more modular and easier to manage. It allows for changing or replacing components without modifying the dependent classes. This results in a cleaner, more manageable codebase.
2. **Testability:** DI simplifies unit testing by allowing easy injection of mock dependencies. This facilitates the isolation of components for testing purposes, leading to more effective and reliable tests.
3. **Reusability:** Common services or components can be easily reused in different parts of an application by simply injecting them wherever needed. This reduces redundancy and encourages DRY (Don't Repeat Yourself) principles.
4. **Maintainability:** DI reduces boilerplate code required for creating and managing dependencies, resulting in cleaner and more maintainable code. Changes in dependencies only need updates in a few places, reducing the risk of errors.
5. **Flexibility:** DI allows for switching between different implementations of dependencies without modifying the dependent classes. This makes the system more adaptable to changes and easier to extend.

### Drawbacks:
1. **Complexity:** DI can add complexity to the system, especially for large applications where the flow of dependencies can become hard to track. Understanding the injection process and the lifecycle of components might be challenging for new developers.
2. **Performance Overhead:** DI frameworks can introduce a slight performance overhead due to the dynamic resolution and injection of dependencies at runtime. This might impact the startup time of the application.
3. **Learning Curve:** Developers need to understand concepts like inversion of control, dependency resolution, and how DI frameworks work. This can be difficult for those new to DI or to the specific DI framework being used.
4. **Overhead in Configuration:** Setting up DI frameworks and configuring dependencies requires additional effort. In large projects, this can become cumbersome and time-consuming.

## 2. Converting Bean Declaration from XML to Java Configuration using @Bean

### Original XML Bean Declaration

This XML configuration defines a bean for an `Employee` class with the specified properties.

```xml
<beans>
    <bean id="employee" class="com.example.Employee">
        <constructor-arg name="id" value="1" />
        <constructor-arg name="name" value="Jebi Hendardi" />
        <constructor-arg name="age" value="20" />
        <constructor-arg name="department" value="Developer" />
    </bean>
</beans>
```

### Java Configuration using @Configuration and @Bean (Constructor Injection)

Below is the Java-based configuration equivalent to the above XML. Here, the `Employee` bean is created using constructor injection, which makes the dependencies explicit and immutable after initialization.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
public class AppConfig {

    @Bean
    public Employee employee() {
        return new Employee(1, "Jebi Hendardi", 20, "Developer");
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Employee {
    private int id;
    private String name;
    private int age;
    private String department;
}
```

### Explanation:

- **@Configuration:** Indicates that the class contains one or more `@Bean` methods that will be managed by the Spring container.
- **@Bean:** Marks a method as a bean provider, and the returned object is managed by the Spring container.
- **@Data, @NoArgsConstructor, @AllArgsConstructor (from Lombok):** These annotations generate boilerplate code such as getters, setters, constructors, `toString`, `equals`, and `hashCode` methods, making the `Employee` class concise and easy to read.

### Java Configuration with Setter-Based Dependency Injection

In this approach, dependencies are injected using setter methods. This method allows for the modification of dependencies after the object is created.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
public class AppConfigSetterDI {

    @Bean
    public Employee employee() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Jebi Hendardi");
        employee.setAge(20);
        employee.setDepartment("Developer");
        return employee;
    }
}

@Data
@NoArgsConstructor
class Employee {
    private int id;
    private String name;
    private int age;
    private String department;
}
```

### Explanation:

- **Setter-Based Injection:** Allows dependencies to be changed or injected after the object is created, offering more flexibility but potentially leading to less immutability.

### Java Configuration with Field-Based Dependency Injection

Field-based injection directly injects dependencies into the fields of a class. 

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
public class AppConfigFieldDI {

    @Autowired
    private Employee employee;

    @Bean
    public Employee employee() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Jebi Hendardi");
        employee.setAge(20);
        employee.setDepartment("Developer");
        return employee;
    }
}

@Data
@NoArgsConstructor
class Employee {
    private int id;
    private String name;
    private int age;
    private String department;
}
```

### Explanation:

- **Field-Based Injection:** Injects dependencies directly into fields, which reduces boilerplate but makes testing more difficult because the fields can't be easily set or mocked externally.

