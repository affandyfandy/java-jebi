### 7.1 - Remove duplicated items for any object and any duplicated fields
#### Objective
Remove duplicate items from any list of objects and eliminate duplicated fields within custom objects.

1. Remove Duplicate Items from a List:
   - Convert a list to a set to remove duplicates for simple types.
   - Utilize the Stream API for custom objects to eliminate duplicates based on specific criteria.

2. Remove Duplicated Fields in Objects:

   - Define and identify what constitutes a duplicate field.
Implement a mechanism to filter out these duplicates.


#
### Removing Duplicates from a List
For simple types like String or Integer, converting the list to a Set and back to a List is an efficient way to remove duplicates.
```java
import java.util.*;

public class UniqueItems {
    public static void main(String[] args) {
        List<String> items = Arrays.asList("apple", "banana", "apple", "orange");
        Set<String> uniqueItemsSet = new HashSet<>(items);
        List<String> uniqueItemsList = new ArrayList<>(uniqueItemsSet);
        
        System.out.println(uniqueItemsList); // Output: [banana, orange, apple]
    }
}
```
#
#### Custom Objects Using Stream API
When dealing with custom objects, we can use Java Streams to filter out duplicates. Consider a Person class with name and age fields:
```java
import java.util.*;
import java.util.stream.*;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + '}';
    }
}

public class UniqueItems {
    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Alice", 30)
        );

        List<Person> distinctPersons = persons.stream()
            .distinct()  // Removing duplicates
            .collect(Collectors.toList());

        System.out.println(distinctPersons); // Output: [Person{name='Alice', age=30}, Person{name='Bob', age=25}]
    }
}
```

#
### Removing Duplicated Fields in Objects
When working with objects, we might want to ensure unique fields. For example, ensuring no two Person objects share the same name. This can be accomplished using a Map:

```java
import java.util.*;
import java.util.stream.Collectors;

public class UniqueFields {
    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Alice", 35)
        );

        Map<String, Person> uniqueByName = persons.stream()
            .collect(Collectors.toMap(
                Person::getName,
                person -> person,
                (existing, replacement) -> existing)); // Keep the existing person for duplicate names

        List<Person> uniquePersons = new ArrayList<>(uniqueByName.values());
        System.out.println(uniquePersons); // Output: [Person{name='Alice', age=30}, Person{name='Bob', age=25}]
    }
}
```
In this case, a Map is used to ensure unique name values. The lambda function (existing, replacement) -> existing keeps the first encountered Person.
#
#### Alternative: Using TreeSet with a Custom Comparator
```java
import java.util.*;

public class UniqueFields {
    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Alice", 35)
        );

        Set<Person> uniquePersonsSet = new TreeSet<>(Comparator.comparing(Person::getName));
        uniquePersonsSet.addAll(persons);

        List<Person> uniquePersonsList = new ArrayList<>(uniquePersonsSet);
        System.out.println(uniquePersonsList); // Output: [Person{name='Alice', age=30}, Person{name='Bob', age=25}]
    }
}
```
TreeSet with a comparator ensures that Person objects are unique based on their name.

#
#### Utilizing Generics for Flexibility
To remove duplicate items from a list of any type, a Set can be used for simplicity
```java
import java.util.*;
import java.util.stream.Collectors;

class ListUtility {
    public static <T> List<T> removeDuplicates(List<T> list) {
        return list.stream()
            .distinct() // Eliminate duplicates
            .collect(Collectors.toList());
    }
}

public class GenericExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Alice", "Charlie", "Bob");
        List<String> uniqueNames = ListUtility.removeDuplicates(names);

        System.out.println(uniqueNames); // Output: [Alice, Bob, Charlie]
    }
}
```
For more complex objects, we can remove duplicates based on specific fields using a key extractor function:
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class ListUtility {
    public static <T, K> List<T> removeDuplicatesByField(List<T> list, Function<? super T, ? extends K> keyExtractor) {
        Set<K> seenKeys = new HashSet<>();
        return list.stream()
            .filter(e -> seenKeys.add(keyExtractor.apply(e))) // Use keyExtractor to track seen keys
            .collect(Collectors.toList());
    }
}

public class GenericExample {
    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Alice", 35)
        );

        List<Person> uniquePersons = ListUtility.removeDuplicatesByField(persons, Person::getName);

        System.out.println(uniquePersons); // Output: [Person{name='Alice', age=30}, Person{name='Bob', age=25}]
    }
}
```



### 7.2 - Demo: Using Wildcards With Generics
Wildcards in Java Generics allow for more flexible and reusable code by specifying bounds on the types that can be used. Here's a breakdown of different types of wildcards:
1. **Unbounded Wildcards (?)**: Accepts any type.
2. **Upper Bounded Wildcards (? extends T)**: Accepts a type that is a subtype of T.
3. **Lower Bounded Wildcards (? super T)**: Accepts a type that is a supertype of T.

#
#### Unbounded Wildcards
When a method can operate on any type of List, you can use unbounded wildcards. This is useful for methods that are only interested in the object itself and not its type.

**Example**: Printing the elements of a list of any type
```java
import java.util.*;

public class WildcardExamples {
    // Method to print all elements in a list of any type
    public static void displayList(List<?> list) {
        for (Object item : list) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        List<String> words = Arrays.asList("alpha", "beta", "gamma");

        // Call method with different types of lists
        displayList(numbers);  // Output: 1 2 3 
        displayList(words);    // Output: alpha beta gamma
    }
}
```
List<?> indicates that the method displayList can accept a list of any type. It processes each element as an Object, demonstrating the flexibility of unbounded wildcards.

#
#### Upper Bounded Wildcards (? extends T)
Use upper bounded wildcards when you need to operate on a type or any of its subtypes. This is particularly useful for methods that are read-only or need to work with a type's hierarchy.

**Example**: Summing the elements of a list that contains numbers.
```java
import java.util.*;

public class WildcardExamples {
    // Method to calculate the sum of elements in a list of numbers
    public static double calculateSum(List<? extends Number> list) {
        double total = 0.0;
        for (Number number : list) {
            total += number.doubleValue();
        }
        return total;
    }

    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);

        // Calculate and print the sum for different number lists
        System.out.println(calculateSum(integers)); // Output: 6.0
        System.out.println(calculateSum(doubles));  // Output: 7.5
    }
}
```
List<? extends Number> ensures that the method calculateSum can work with any list of Number or its subclasses (e.g., Integer, Double).

#
#### Lower Bounded Wildcards (? super T)
Lower bounded wildcards are used when you want to write to a collection and need to ensure type compatibility with a specified superclass.

**Example**: Adding elements to a list that can accept integers or any of its supertypes.
```java
import java.util.*;

public class WildcardExamples {
    // Method to add integers to a list of numbers or objects
    public static void addValues(List<? super Integer> list) {
        list.add(10);
        list.add(20);
        list.add(30);
    }

    public static void main(String[] args) {
        List<Number> numbers = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        // Add values to different types of lists
        addValues(numbers);
        addValues(objects);

        System.out.println(numbers); // Output: [10, 20, 30]
        System.out.println(objects); // Output: [10, 20, 30]
    }
}
```
List<? super Integer> allows the addValues method to add Integer values to a list of Number or any superclass of Integer.

#
#### Using Wildcards with Generic Classes
Wildcards can also be used in generic classes to allow greater flexibility in type parameters.

**Example**: Printing the content of a generic Box object.
```java
class Box<T> {
    private T item;

    public Box(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}

public class WildcardExamples {
    // Method to print the contents of a Box of any type
    public static void displayBox(Box<?> box) {
        System.out.println("Box contains: " + box.getItem());
    }

    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<>(42);
        Box<String> stringBox = new Box<>("Hello World");

        // Display the contents of different boxes
        displayBox(integerBox);  // Output: Box contains: 42
        displayBox(stringBox);   // Output: Box contains: Hello World
    }
}
```
In this scenario, Box<?> indicates that the displayBox method can accept a Box containing any type of item, enhancing flexibility in dealing with generic types.
#
#### Summary
Wildcards in Java Generics allow for the creation of more flexible and reusable methods and classes by providing different levels of type constraints. This enables developers to write methods that can operate on a variety of data types while maintaining type safety and consistency.

#
### 7.3 - Operations on a List of Objects
#### Task Overview
- **Sorting a List by Any Field** : Sort a list by a specific field using comparators and streams.
- **Finding the Item with the Maximum Value** : Identify the item with the highest value in a particular field.
- **Combining Both Operations** : Sort the list and find the maximum value in one go.
- **Using Generics for Flexibility** : Create generic methods to handle these operations for any type of list and field.


#
#### Sorting a List by Any Field
To sort a list by a specific field, we use the Comparator interface, which provides methods for creating custom comparison logic.

**Example**: Sorting a list of Employee objects by name and salary.
```java
import java.util.*;

class Employee {
    String name;
    int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Employee{name='" + name + "', salary=" + salary + '}';
    }
}

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000)
        );

        // Sorting by salary
        employees.sort(Comparator.comparingInt(Employee::getSalary));
        System.out.println("Sorted by salary: " + employees);
        // Output: [Employee{name='Alice', salary=5000}, Employee{name='Charlie', salary=6000}, Employee{name='Bob', salary=7000}]

        // Sorting by name
        employees.sort(Comparator.comparing(Employee::getName));
        System.out.println("Sorted by name: " + employees);
        // Output: [Employee{name='Alice', salary=5000}, Employee{name='Bob', salary=7000}, Employee{name='Charlie', salary=6000}]
    }
}
```
Comparator.comparingInt and Comparator.comparing are used to create comparators for integer and string fields respectively, enabling sorting by different fields.

#
#### Sorting by Multiple Fields
When sorting by multiple criteria, comparator chaining is a powerful technique. It allows you to sort by a primary field and then by secondary fields if the primary fields are equal.

**Example**: Sorting by salary first, then by name.
```java
import java.util.*;

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000),
            new Employee("Bob", 5000)
        );

        // Sorting by salary, then by name
        employees.sort(Comparator.comparingInt(Employee::getSalary)
                                  .thenComparing(Employee::getName));
        System.out.println("Sorted by salary and then by name: " + employees);
        // Output: [Employee{name='Alice', salary=5000}, Employee{name='Bob', salary=5000}, Employee{name='Charlie', salary=6000}, Employee{name='Bob', salary=7000}]
    }
}
```
In this example, thenComparing is used to add a secondary sorting criterion after the primary sort by salary.

#
#### Sorting Using Stream API
The Stream API provides a more functional approach to sorting. You can sort streams using the sorted method with a comparator.

**Example**: Sorting a list by salary using the Stream API.
```java
import java.util.*;
import java.util.stream.*;

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000)
        );

        List<Employee> sortedEmployees = employees.stream()
                                                  .sorted(Comparator.comparingInt(Employee::getSalary))
                                                  .collect(Collectors.toList());

        System.out.println("Sorted by salary using Stream: " + sortedEmployees);
        // Output: [Employee{name='Alice', salary=5000}, Employee{name='Charlie', salary=6000}, Employee{name='Bob', salary=7000}]
    }
}
```
Using streams, we can create a sorted list without modifying the original list, ensuring immutability and functional programming principles.

#
###  Finding the Item with the Maximum Value
The Collections.max method can be used to find the maximum value in a list based on a specified comparator.

**Example**: Finding the employee with the highest salary.
```java
import java.util.*;

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000)
        );

        Employee maxSalaryEmployee = Collections.max(employees, Comparator.comparingInt(Employee::getSalary));
        System.out.println("Employee with max salary: " + maxSalaryEmployee);
        // Output: Employee with max salary: Employee{name='Bob', salary=7000}
    }
}
```
Collections.max uses the provided comparator to determine which employee has the maximum salary.

#
#### Finding the Maximum Using Stream API
The Stream API provides a concise way to find the maximum element based on a comparator.

**Example**: Finding the employee with the highest salary using streams.
```java
import java.util.*;
import java.util.stream.*;

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000)
        );

        Optional<Employee> maxSalaryEmployee = employees.stream()
                                                        .max(Comparator.comparingInt(Employee::getSalary));

        maxSalaryEmployee.ifPresent(e -> System.out.println("Employee with max salary: " + e));
        // Output: Employee with max salary: Employee{name='Bob', salary=7000}
    }
}
```
The max method on a stream, combined with a comparator, allows you to easily find the maximum element in a functional and elegant manner.


#
### Combining Both Operations
We can combine sorting and finding the maximum value in one seamless operation
```java
import java.util.*;

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000)
        );

        // Sorting by salary
        employees.sort(Comparator.comparingInt(Employee::getSalary));
        System.out.println("Sorted by salary: " + employees);

        // Finding the employee with the maximum salary
        Employee maxSalaryEmployee = employees.stream()
                                              .max(Comparator.comparingInt(Employee::getSalary))
                                              .orElseThrow(() -> new NoSuchElementException("No employee found"));

        System.out.println("Employee with max salary: " + maxSalaryEmployee);
    }
}
```

#
### Using Generics for Flexibility
Generic methods allow you to write more flexible and reusable code. You can create generic methods that can sort a list or find the maximum value based on any specified field.
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class ListUtils {
    public static <T, U extends Comparable<? super U>> List<T> sortByField(List<T> list, Function<? super T, ? extends U> keyExtractor) {
        return list.stream()
                   .sorted(Comparator.comparing(keyExtractor))
                   .collect(Collectors.toList());
    }

    public static <T, U extends Comparable<? super U>> Optional<T> findMaxByField(List<T> list, Function<? super T, ? extends U> keyExtractor) {
        return list.stream()
                   .max(Comparator.comparing(keyExtractor));
    }
}

public class EmployeeOperations {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000)
        );

        // Sorting by salary using generics
        List<Employee> sortedBySalary = ListUtils.sortByField(employees, Employee::getSalary);
        System.out.println("Sorted by salary using generics: " + sortedBySalary);
        // Output: [Employee{name='Alice', salary=5000}, Employee{name='Charlie', salary=6000}, Employee{name='Bob', salary=7000}]

        // Finding the maximum salary using generics
        Optional<Employee> maxSalaryEmployee = ListUtils.findMaxByField(employees, Employee::getSalary);
        maxSalaryEmployee.ifPresent(e -> System.out.println("Employee with max salary: " + e));
        // Output: Employee with max salary: Employee{name='Bob', salary=7000}
    }
}
```
By using generics, the sortByField and findMaxByField methods can handle lists of any type and sort or find the maximum based on any specified field, making the code more reusable and adaptable.
#
### 7.4 - Convert list any object to map with any key field
#### Task Overview
- **Converting a List to a Map Using Streams** : Convert a list of objects into a map using a specified field as the key.
- **Handling Duplicate Keys** : Address scenarios where keys might not be unique.
- **Using Custom Keys** : Use custom key combinations for map keys.
- **Using Generics for Flexibility** : Create a generic method to handle various types and key fields.

#
### Converting a List to a Map Using Streams
The Stream API in Java provides a straightforward way to transform a list into a map. This allows you to specify a key for the map based on an object field.

**Example**: Convert a list of Employee objects into a map with the employee’s name as the key.

```java
import java.util.*;
import java.util.stream.Collectors;

class Employee {
    String name;
    int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Employee{name='" + name + "', salary=" + salary + '}';
    }
}

public class ListToMap {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000)
        );

        Map<String, Employee> employeeMap = employees.stream()
            .collect(Collectors.toMap(Employee::getName, e -> e));

        System.out.println(employeeMap);
        // Output: {Alice=Employee{name='Alice', salary=5000}, Bob=Employee{name='Bob', salary=7000}, Charlie=Employee{name='Charlie', salary=6000}}
    }
}
```
Collectors.toMap is used to transform the list into a map where the keys are employee names and the values are the corresponding Employee objects.

#
#### Handling Duplicate Keys

When converting a list to a map, there might be cases where keys are not unique. The Collectors.toMap method provides a way to handle such situations using a merge function.

**Example**: Convert a list to a map where duplicate names are resolved by keeping the last occurrence.
```java
import java.util.*;
import java.util.stream.Collectors;

public class ListToMap {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000),
            new Employee("Alice", 8000) // Duplicate name
        );

        Map<String, Employee> employeeMap = employees.stream()
            .collect(Collectors.toMap(
                Employee::getName,
                e -> e,
                (existing, replacement) -> replacement // Last occurrence wins
            ));

        System.out.println(employeeMap);
        // Output: {Alice=Employee{name='Alice', salary=8000}, Bob=Employee{name='Bob', salary=7000}, Charlie=Employee{name='Charlie', salary=6000}}
    }
}
```
In this scenario, the mergeFunction (existing, replacement) -> replacement ensures that the last occurrence of a duplicate key is retained.

#
#### Using Custom Keys
We can use custom fields or combinations of fields as the map key. For this, create a custom key class and implement the necessary methods for equality and hashing.

#### Step 1: Create a custom key class EmployeeKey.
```java
import java.util.Objects;

class EmployeeKey {
    String name;
    int salary;

    public EmployeeKey(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeKey that = (EmployeeKey) o;
        return salary == that.salary && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, salary);
    }

    @Override
    public String toString() {
        return "EmployeeKey{name='" + name + "', salary=" + salary + '}';
    }
}
```
#
#### Step 2: Use the custom key class in converting the list to a map.
```java
import java.util.*;
import java.util.stream.Collectors;

public class ListToMap {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000)
        );

        Map<EmployeeKey, Employee> employeeMap = employees.stream()
            .collect(Collectors.toMap(
                e -> new EmployeeKey(e.getName(), e.getSalary()),
                e -> e
            ));

        System.out.println(employeeMap);
        // Output: {EmployeeKey{name='Alice', salary=5000}=Employee{name='Alice', salary=5000}, EmployeeKey{name='Bob', salary=7000}=Employee{name='Bob', salary=7000}, EmployeeKey{name='Charlie', salary=6000}=Employee{name='Charlie', salary=6000}}
    }
}
```
In this example, the custom EmployeeKey combines the name and salary fields to create a unique key for each map entry.

#
### Using Generics for Flexibility
To create flexible and reusable code, we can write a generic method that converts a list to a map using any specified key field. This method will handle various object types and key fields.

#### Step 1 : Define a generic utility class ListUtils.
```java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class ListUtils {
    public static <T, K> Map<K, T> convertListToMap(List<T> list, Function<? super T, ? extends K> keyExtractor) {
        return list.stream()
            .filter(e -> keyExtractor.apply(e) != null) // Exclude null keys
            .collect(Collectors.toMap(
                keyExtractor,
                Function.identity(),
                (existing, replacement) -> replacement // Replace existing with new in case of duplicates
            ));
    }
}
```

#
#### Step 2: Use the generic method to convert a list of Employee objects to a map.
```java
import java.util.*;

public class ListToMap {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000),
            new Employee("Bob", 8000) // Duplicate name
        );

        // Convert list to map using name as the key
        Map<String, Employee> employeeMapByName = ListUtils.convertListToMap(
            employees,
            Employee::getName
        );

        System.out.println(employeeMapByName);
        // Output: {Bob=Employee{name='Bob', salary=8000}, Charlie=Employee{name='Charlie', salary=6000}}

        // Convert list to map using custom key (name and salary)
        Map<EmployeeKey, Employee> employeeMapByCustomKey = ListUtils.convertListToMap(
            employees,
            e -> new EmployeeKey(e.getName(), e.getSalary())
        );

        System.out.println(employeeMapByCustomKey);
        // Output: {EmployeeKey{name='Bob', salary=8000}=Employee{name='Bob', salary=8000}, EmployeeKey{name='Charlie', salary=6000}=Employee{name='Charlie', salary=6000}, EmployeeKey{name='Bob', salary=7000}=Employee{name='Bob', salary=7000}}
    }
}
```
The convertListToMap method is versatile and can be applied to any list and key field, making it a valuable tool for various scenarios.
#
### 7.5 - Design Class generic for paging data (any Object)
Paging data is essential for handling large datasets, allowing users to navigate through data without overwhelming them with too much information at once. This task involves creating a generic paging solution that is versatile enough to handle any type of object.

#### Implementation Steps
- **Define the Page Class**: This class will encapsulate the data and metadata for a single page.
- **Create the PagingUtils Class**: This class will provide utility methods for extracting a specific page from a list of items and performing optional sorting or filtering.
- **Develop the PaginationDemo Class**: This class will demonstrate how to use the paging functionality with a sample dataset.


#
### Designing the Page Class
The Page class will store the current page's content and metadata such as the current page number, total items, and total pages.
```java
import java.util.List;

public class Page<T> {
    private List<T> content;
    private int size;
    private int pageNumber;
    private int totalPages;
    private int totalItems;

    public Page(List<T> content, int size, int pageNumber, int totalItems) {
        this.content = content;
        this.size = size;
        this.pageNumber = pageNumber;
        this.totalItems = totalItems;
        this.totalPages = (int) Math.ceil((double) totalItems / size);
    }

    public List<T> getContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }

    @Override
    public String toString() {
        return "Page{" +
                "content=" + content +
                ", size=" + size +
                ", pageNumber=" + pageNumber +
                ", totalPages=" + totalPages +
                ", totalItems=" + totalItems +
                '}';
    }
}
```

#
#### Implementing the PagingUtils Class
The PagingUtils class will contain a method to extract a specific page from a list of items. It will also demonstrate how to sort or filter items before paging.
```java
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PagingUtils {
    public static <T> Page<T> getPage(List<T> items, int pageNumber, int size) {
        // Example of sorting by a custom field, assuming T has a method like getSalary (cast to Employee for this example)
        items = items.stream()
                .sorted(Comparator.comparingInt(item -> ((Employee) item).getSalary())) 
                .collect(Collectors.toList());

        int totalItems = items.size();
        int fromIndex = (pageNumber - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<T> pageContent = items.subList(fromIndex, toIndex);
        return new Page<>(pageContent, size, pageNumber, totalItems);
    }
}
```

#
#### Developing the PaginationDemo Class
This class will demonstrate how to use the PagingUtils class to paginate a list of Employee objects.
```java
import java.util.Arrays;
import java.util.List;

public class PaginationDemo {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", 5000),
            new Employee("Bob", 7000),
            new Employee("Charlie", 6000),
            new Employee("David", 8000),
            new Employee("Eve", 9000)
        );

        int pageNumber = 1;
        int size = 2;

        Page<Employee> page = PagingUtils.getPage(employees, pageNumber, size);
        System.out.println(page);

        // Example of navigating to the next page
        pageNumber = 2;
        Page<Employee> nextPage = PagingUtils.getPage(employees, pageNumber, size);
        System.out.println(nextPage);
    }
}
```

Output 
-----------
This output shows that the list has been paginated into two items per page and sorted by salary. Adjust the page number and size to navigate through different pages.


#
In real-world applications, sorting and filtering are often performed before pagination to ensure that users see a relevant and ordered subset of the data. You might sort by different fields or apply filters based on user inputs or other criteria.

Here’s an example of a modified PagingUtils method that includes sorting by a specific field:
```java
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PagingUtils {
    public static <T> Page<T> getPage(List<T> items, int pageNumber, int size, Comparator<T> comparator) {
        // Sorting the list by the given comparator
        items = items.stream()
                .sorted(comparator)
                .collect(Collectors.toList());

        int totalItems = items.size();
        int fromIndex = (pageNumber - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<T> pageContent = items.subList(fromIndex, toIndex);
        return new Page<>(pageContent, size, pageNumber, totalItems);
    }
}
```
We can call this method with a custom comparator to sort the data as needed.