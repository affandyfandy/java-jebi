#
### 6.1 - Parallel Streams
Parallel streams in Java provide a way to perform parallel processing on collections. They split the data into multiple parts and process them concurrently across different CPU cores. This can significantly reduce the processing time for large datasets or computationally intensive operations.

Parallel streams can be beneficial in the following scenarios:

- Large Datasets: They provide performance gains when working with large collections.
- Independent Tasks: Ideal for tasks that do not rely on the results of other tasks and can be executed independently.
- Computationally Intensive Operations: Useful for operations that require significant CPU time, such as complex calculations.
- Sufficient Hardware Resources: Effective on systems with multiple cores. Systems with fewer cores may not see significant benefits.

Using parallel streams requires careful consideration of several factors:

- Thread Safety: Operations must be stateless and thread-safe to avoid race conditions.
- Splitting Overhead: The cost of splitting data and merging results may outweigh the benefits for small datasets.
- Order Sensitivity: Parallel processing can lead to different execution orders, which can be problematic if the order of results matters.
- System Load: High system load and resource contention may occur with many parallel tasks. Monitoring system performance is essential.

#
#### How to Use Parallel Streams
Using parallel streams in Java is straightforward. You can convert a stream to a parallel stream using the parallelStream() method on a collection or the parallel() method on a sequential stream.

#### Example: Summing Elements in Parallel
```java
import java.util.stream.LongStream;

public class ParallelSumExample {
    public static void main(String[] args) {
        long sum = LongStream.range(1, 500_001)
                             .parallel()
                             .sum();

        System.out.println("Sum: " + sum);
        // Expected Output: Sum: 125000250000
    }
}
```


#### Example: Filtering in Parallel
```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParallelFilterExample {
    public static void main(String[] args) {
        // New list of names
        List<String> names = Arrays.asList("John", "Jane", "Jake", "Julie", "James", "Jane");

        // Stream to get distinct names with more than 3 characters
        List<String> distinctNames = names.parallelStream()
                                          .distinct()
                                          .filter(name -> name.length() > 3)
                                          .collect(Collectors.toList());

        // Print the result
        distinctNames.forEach(System.out::println);
        // Expected Output: John Jane Jake Julie James
    }
}

```
### Example: Reducing Data in Parallel
```java
import java.util.Arrays;
import java.util.List;

public class ParallelReduceExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

        int sum = numbers.parallelStream()
                         .reduce(0, Integer::sum);

        System.out.println("Sum: " + sum);
        // Output: Sum: 165
    }
}
```

#
### 6.2 - Remove Duplicate Elements from a List of Strings Using Streams
Removing duplicates from a list involves transforming the list into a new one containing only unique elements. Java Streams facilitate this through the distinct() method, which returns a stream consisting of distinct elements.

`Implementation Details`
- Create the List: Begin with a list of strings that may contain duplicates.
- Convert to Stream: Convert this list into a stream.
- Apply distinct(): Use the distinct() method to filter out duplicate elements.
- Collect Results: Collect the unique elements into a new list using the collect() method.
```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RemoveDuplicates {
    public static void main(String[] args) {
        List<String> fruits = Arrays.asList("Apple", "banana", "APPLE", "Cherry", "banana", "Date");

        List<String> uniqueFruits = fruits.stream()
                                          .map(String::toLowerCase)
                                          .distinct()
                                          .collect(Collectors.toList());

        uniqueFruits.forEach(System.out::println);
    }
}
```

#
### 6.3 - Remove Lines with Duplicate Data by Key Field
To remove lines with duplicated data based on a key field from a file input (CSV or TXT) and write the results to a new file using Java streams and IO operations, we can break down the process into several clear steps. 

#### Step 1 : Read the Input File:
- Use Java's IO classes to read from the input file (BufferedReader for text-based files like TXT and CSVReader for CSV files).

#
#### Step 2 : Identify the Key Field
- Depending on whether the input file is a TXT or CSV file, identify the key field position (for TXT files) or name (for CSV files). The key field is crucial as it determines uniqueness.


#
#### Step 3 : Process Lines to Remove Duplicates:
Use Java streams to process each line from the input file:
- Split each line to extract the key field.
- Use a Set to track keys that have already been encountered.
- Filter out lines where the key has already been seen (indicating a duplicate).

#
#### Step 4 : Write Unique Lines to a New File:

- Use Java's IO classes (BufferedWriter for text-based files) to write the filtered lines to a new output file.

#
#### Step 5 : Close Resources Safely
- Always close resources (BufferedReader, BufferedWriter, etc.) using try-with-resources to ensure proper resource management and to avoid memory leaks.

```java
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class RemoveDuplicatesFromFile {

    public static void main(String[] args) {
        String inputFilePath = "input.csv";
        String outputFilePath = "output.csv"; 
        String keyFieldName = "id"; 

        Set<String> seenKeys = new HashSet<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFilePath));
             BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath))) {

            // Read and process each line from input file
            reader.lines()
                  .filter(line -> {
                      String[] fields = line.split(","); // Assuming CSV format
                      String key = fields[0]; // Assuming the first field is the key field

                      // Check if key has been seen already
                      if (!seenKeys.contains(key)) {
                          seenKeys.add(key);
                          return true; // Include this line in output
                      } else {
                          return false; // Skip this line (duplicate)
                      }
                  })
                  .forEach(line -> {
                      try {
                          writer.write(line);
                          writer.newLine();
                      } catch (IOException e) {
                          System.err.println("Error writing line to output file: " + e.getMessage());
                      }
                  });

            System.out.println("Duplicates removed and output written to " + outputFilePath);

        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
        }
    }
}
```

#### Processing with Streams
- reader.lines() reads lines from the input file as a stream of strings.
- .filter() processes each line, splitting it into fields (fields array) based on the CSV format assumption.
- Checks if the key field (fields[0]) has been seen before (seenKeys.contains(key)).
- If not seen (!seenKeys.contains(key)), adds the key to seenKeys and writes the line to the output file.
- .forEach() writes each filtered line to the output file using writer.write(line).

#
#### How It Works
- **Reading**: The BufferedReader reads lines from the input file.
- **Filtering**: The lines are filtered based on whether their key field (fields[0]) has been encountered before.
- **Writing**: Valid (unique) lines are written to the output file using BufferedWriter.
- **Resource Management**: The try-with-resources ensures that all resources (reader and writer) are closed properly after use.


#
### 6.4 - Count the Number of Strings Starting with a Specific Letter Using Streams

`Steps`
1. Create the Input List: Start with a list of strings. In this case, the input list is ["Red", "Green", "Blue", "Pink", "Brown"].

2. Define the Target Letter: Identify the specific letter you want to check against. In this example, it's "G".

3. Use Java Streams:
   - Convert the list to a stream using stream() method.
   - Use the filter() method to apply a condition to each element of the stream. In this case, the condition checks if each string starts with the target letter.
   - Use the count() terminal operation to count the number of elements that satisfy the condition.

4. Output the Result: 
   - Print or use the count to get the number of strings that start with the specific letter.

#
```java
import java.util.Arrays;
import java.util.List;

public class CountStringsStartingWithSpecificLetter {

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("Red", "Green", "Blue", "Pink", "Brown");
        String targetLetter = "G";

        long count = strings.stream()
                            .filter(s -> s.startsWith(targetLetter))
                            .count();

        System.out.println("Number of strings starting with '" + targetLetter + "': " + count);
    }
}
```

#### Explanation:
- **Input List**: strings is a list containing "Red", "Green", "Blue", "Pink", and "Brown".
- **Target Letter**: targetLetter is "G".
- **Java Streams Usage**:
  - .stream() converts the strings list into a stream of strings.
  - .filter(s -> s.startsWith(targetLetter)) filters the stream to include only those strings (s) that start with the targetLetter ("G" in this case).
  - .count() calculates the number of elements in the filtered stream.
- **Output**: Prints the count of strings that start with "G".
#
#### How It Works:
- **Stream Creation**: The strings.stream() method creates a stream of strings from the strings list.
- **Filtering**: The .filter(s -> s.startsWith(targetLetter)) method checks each string (s) in the stream to see if it starts with "G".
- **Counting**: The .count() method counts how many strings in the filtered stream satisfy the condition (start with "G").

#
### 6.5 - Sorting and Finding Data in a List of Students Using Java Streams
```java
class Employee {
    private int id;
    private String name;
    private int age;
    private double salary;

    // Constructor
    public Employee(int id, String name, int age, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    // toString method for easier output
    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', age=" + age + ", salary=" + salary + "}";
    }
}
```
#
#### Step 1 - Sort Employees by Name Alphabetically (Ascending)
- **Convert the List to a Stream**: Use stream() to create a stream from the list of employees.
- **Sort the Stream**: Use sorted() with a Comparator that compares the name field of Employee objects.
- **Collect the Sorted Stream**: Use collect(Collectors.toList()) to gather the sorted elements back into a list.

```java
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeOperations {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(1, "Alice", 30, 70000),
            new Employee(2, "Bob", 25, 60000),
            new Employee(3, "Charlie", 28, 75000),
            new Employee(4, "Dave", 35, 80000)
        );

        List<Employee> sortedByName = employees.stream()
            .sorted(Comparator.comparing(Employee::getName))
            .collect(Collectors.toList());

        sortedByName.forEach(System.out::println);
    }
}
```

Output :
```
Employee{id=1, name='Alice', age=30, salary=70000.0}
Employee{id=2, name='Bob', age=25, salary=60000.0}
Employee{id=3, name='Charlie', age=28, salary=75000.0}
Employee{id=4, name='Dave', age=35, salary=80000.0}
```

#
#### Step 2 - Find the Employee with the Maximum Salary
- **Convert the List to a Stream**: Use stream() to create a stream.
- **Find the Maximum**: Use max() with a Comparator that compares the salary field of Employee objects.
- **Handle Optional Result**: Since max() returns an Optional, handle it with ifPresent() to print the result if present.

```java
import java.util.*;

public class EmployeeOperations {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(1, "Alice", 30, 70000),
            new Employee(2, "Bob", 25, 60000),
            new Employee(3, "Charlie", 28, 75000),
            new Employee(4, "Dave", 35, 80000)
        );

        Optional<Employee> maxSalaryEmployee = employees.stream()
            .max(Comparator.comparingDouble(Employee::getSalary));

        maxSalaryEmployee.ifPresent(System.out::println);
    }
}
```
Output
```
Employee{id=4, name='Dave', age=35, salary=80000.0}
```

#
#### Step 3 - Check if Any Employee Names Match a Specific Keyword

- **Convert the List to a Stream**: Use stream() to create a stream.
- **Match with a Condition**: Use anyMatch() to check if any element satisfies a given predicate (condition).
- **Define the Condition**: The condition checks if the name of the employee contains the keyword (e.g., "li" for names like "Alice").

```java
import java.util.*;

public class EmployeeOperations {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(1, "Alice", 30, 70000),
            new Employee(2, "Bob", 25, 60000),
            new Employee(3, "Charlie", 28, 75000),
            new Employee(4, "Dave", 35, 80000)
        );

        String keyword = "li"; // Keyword to match

        boolean hasMatch = employees.stream()
            .anyMatch(e -> e.getName().toLowerCase().contains(keyword.toLowerCase()));

        System.out.println("Any employee name contains '" + keyword + "': " + hasMatch);
    }
}
```

Output
```
Any employee name contains 'li': true
```

#
#### How It Works?
1. Sorting Names:

   - The stream() method creates a stream from the list of employees.
   - The sorted() method with Comparator.comparing(Employee::getName) sorts the stream based on the name field in ascending order.
   - The collect(Collectors.toList()) method collects the sorted elements into a new list.
  
2. Finding Maximum Salary:

   - The max() method with Comparator.comparingDouble(Employee::getSalary) finds the employee with the highest salary.
   - Since max() returns an Optional<Employee>, ifPresent(System.out::println) is used to print the employee if one is found.
  
3. Matching Names with Keyword:
   - The anyMatch() method checks if any employee's name contains the given keyword.
   - The keyword is converted to lowercase to make the search case-insensitive, and contains() is used to check for the presence of the keyword in the employee's name.


#
### 6.6 - Converting a List of Employees to a Map with ID as Key Using Java Streams
To convert a list of Employee objects into a Map where the employee ID is the key and the Employee object is the value, we can use Java streams along with the Collectors.toMap() method. This allows you to create a map from the list efficiently and in a concise manner.

#### Steps 
1. Define the Employee Class: Ensure your Employee class has the necessary fields and methods, particularly a getId() method to retrieve the employee's ID, which will be used as the key in the map.

2. Create the List of Employees: Start with a list of Employee objects that you want to convert into a map.

3. Convert to Map Using Streams:
   - Use the stream() method to convert the list into a stream.
   - Use the collect() method with Collectors.toMap() to convert the stream into a map.
   - Specify the key and value for the map in the toMap() method.

4. Handle Possible Duplicate Keys: 
   - If there is a possibility of duplicate keys (IDs), handle them appropriately by specifying a merge function in the toMap() method.

```java
import java.util.*;
import java.util.stream.Collectors;

class Employee {
    private int id;
    private String name;
    private int age;
    private double salary;

    // Constructor
    public Employee(int id, String name, int age, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    // toString method for easier output
    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', age=" + age + ", salary=" + salary + "}";
    }
}

public class EmployeeMapExample {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(1, "Alice", 30, 70000),
            new Employee(2, "Bob", 25, 60000),
            new Employee(3, "Charlie", 28, 75000),
            new Employee(4, "Dave", 35, 80000)
        );

        Map<Integer, Employee> employeeMap = employees.stream()
            .collect(Collectors.toMap(
                Employee::getId, // Key Mapper
                employee -> employee // Value Mapper
            ));

        // Print the map to verify the result
        employeeMap.forEach((id, employee) -> System.out.println("ID: " + id + ", Employee: " + employee));
    }
}
```
#### Explanation
1. **Employee Class**: The Employee class has an id, name, age, and salary along with appropriate constructors, getters, and a toString() method for easier output.

2. **List of Employees**: The employees list is created with four Employee objects, each having a unique ID.

3. **Converting to Map**:

   - employees.stream() creates a stream from the list of employees.
   - collect(Collectors.toMap(Employee::getId, employee -> employee)) collects the stream elements into a map. The key is obtained using Employee::getId, and the value is the employee object itself.
   - This results in a Map<Integer, Employee> where each key is the employee ID, and the value is the corresponding Employee object.

4. **Printing the Map**: The forEach method of the map is used to print each entry, displaying the ID and the associated Employee object.

#
#### Handling Duplicate Keys:
If there is a possibility of having duplicate keys (i.e., employees with the same ID), we need to handle this scenario by providing a merge function. Here's modify code to handle duplicates:

```java
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeMapExample {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
            new Employee(1, "Alice", 30, 70000),
            new Employee(2, "Bob", 25, 60000),
            new Employee(3, "Charlie", 28, 75000),
            new Employee(1, "Dave", 35, 80000) // Duplicate ID
        );

        Map<Integer, Employee> employeeMap = employees.stream()
            .collect(Collectors.toMap(
                Employee::getId, // Key Mapper
                employee -> employee, // Value Mapper
                (existing, replacement) -> existing // Merge function in case of duplicates
            ));

        // Print the map to verify the result
        employeeMap.forEach((id, employee) -> System.out.println("ID: " + id + ", Employee: " + employee));
    }
}
```
#### Explanation of Merge Function:
- The toMap() method now includes a third parameter (existing, replacement) -> existing, which is a merge function that specifies what to do if duplicate keys are found.
- In this code, the merge function retains the existing value and ignores the replacement, effectively discarding the second Employee with the duplicate ID.