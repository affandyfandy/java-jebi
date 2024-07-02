### 5.1 - Compare ArrayList vs LinkedList and HashSet vs TreeSet vs LinkedHashSet

### Compare ArrayList vs LinkedList

`Order`

- Both ArrayList and LinkedList maintain the order of insertion, meaning elements are stored and retrieved in the sequence they were added.

`Null Elements`

- Both structures allow null elements.

`Performance`

- ArrayList provides constant time for random access due to its underlying dynamic array. However, inserting or deleting elements in the middle requires shifting elements, making these operations O(n) in the worst case.
- LinkedList allows faster insertions and deletions, especially in the middle of the list, because it involves merely updating node pointers, which is O(1). However, it has slower access times due to the need to traverse the list from the beginning or end.

`Memory Usage`

- ArrayList is generally more memory-efficient, but it may need to resize the underlying array, leading to temporary additional memory usage.
- LinkedList consumes more memory due to storage overhead for each element (i.e., two pointers for each node).

`Synchronization`

- Both are not synchronized inherently. To use them in a concurrent environment, they must be synchronized externally using Collections.synchronizedList.

`Iterator Behavior`

- Both use fail-fast iterators, meaning that if the list is structurally modified after the iterator is created (except through the iterator's own methods), a ConcurrentModificationException is thrown.

`Use Cases`

- Use ArrayList when frequent access to elements is needed.
- Use LinkedList when there are frequent insertions or deletions.

#
### Compare HashSet vs TreeSet vs LinkedHashSet
HashSet, TreeSet, and LinkedHashSet are implementations of the Set interface in Java, which is used to store unique elements. Each has different characteristics regarding ordering, performance, and behavior.


|Feature|HashSet|TreeSet|LinkedHashSet|
|---|---|---|---|
|Order|	Unordered|	Ordered (natural order or custom comparator)|	Maintains insertion order|
|Null Elements|	Allows one null element	|Does not allow null elements	|Allows one null element
|Performance|	Fast access, insertion, and removal (O(1))	|Slower access, insertion, and removal (O(log n))	|Intermediate performance, faster than TreeSet but slower than HashSet
|Synchronization|	Not synchronized	|Not synchronized	|Not synchronized
|Iterator Type	|Fail-fast	|Fail-fast	|Fail-fast|

#
#### Explanation:
`Order`

- HashSet does not maintain any order for its elements.
- TreeSet maintains elements in a sorted order, either natural ordering or through a custom comparator.
- LinkedHashSet maintains the order of elements as they are inserted, which can be useful when a predictable iteration order is needed.

`Null Elements`

- HashSet and LinkedHashSet allow one null element, as duplicates are not permitted in a set.
- TreeSet does not allow null elements because nulls cannot be compared to other objects, which is necessary for maintaining order.

`Performance`

- HashSet offers constant time performance (O(1)) for basic operations like add, remove, and contains, assuming a good hash function.
- TreeSet provides logarithmic time performance (O(log n)) for the same operations because it is implemented as a Red-Black tree.
- LinkedHashSet has slightly lower performance compared to HashSet due to the additional overhead of maintaining a linked list for insertion order, but it is generally faster than TreeSet.

`Synchronization`

- None of these sets are synchronized by default. For thread-safe operations, they should be wrapped using Collections.synchronizedSet.

`Iterator Behavior`

- All three sets use fail-fast iterators. This means that any structural modification to the set after creating the iterator (other than through the iterator's own methods) will result in a ConcurrentModificationException.

`Use Cases`

- Use HashSet when you need to store unique elements with fast access and don't care about order.
- Use TreeSet when you need a sorted set of unique elements.
- Use LinkedHashSet when you need a set with fast access but also need to maintain the insertion order.
#
### 5.2 - Write a Java program to retrieve an element (at a specified index) from a given array list.

```java
import java.util.ArrayList;

public class RetrieveElementFromArrayList {
    public static void main(String[] args) {
        // Creating an ArrayList of Strings
        ArrayList<String> arrayList = new ArrayList<>();

        // Adding elements to the ArrayList
        arrayList.add("Java");
        arrayList.add("Python");
        arrayList.add("C++");
        arrayList.add("JavaScript");
        arrayList.add("Ruby");

        // Index of the element to retrieve
        int index = 2; // Retrieves the element at index 2 (which is "C++")

        // Retrieving the element
        if (index >= 0 && index < arrayList.size()) {
            String element = arrayList.get(index);
            System.out.println("Element at index " + index + ": " + element);
        } else {
            System.out.println("Invalid index. Index should be between 0 and " + (arrayList.size() - 1));
        }
    }
}
```

`Explanation :`

Initially, an ArrayList of Strings named arrayList is created and populated with elements such as "Java", "Python", "C++", "JavaScript", and "Ruby". The program specifies an index, in this case 2, which corresponds to the third element in the ArrayList ("C++" due to zero-based indexing). 

Using the get method of ArrayList, the program retrieves the element at the specified index. Prior to retrieving, the program performs a bounds check to ensure the index falls within the valid range (0 to arrayList.size() - 1). 

If the index is valid, it prints the retrieved element; otherwise, it outputs an error message indicating an invalid index. This example demonstrates basic ArrayList operations in Java, including adding elements, accessing elements by index, and handling index out-of-bounds scenarios.

#
### 5.3 - Remove lines which is duplicated data by 1 key field

Input: file data (.csv or .txt) and position key field for txt or key field name for csv
Output: write to new file with no duplication by key field

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RemoveDuplicates {

    public static void main(String[] args) {
        String inputFile = "data.csv";
        String outputFile = "output.csv";
        String keyField = "id"; 

        try {
            removeDuplicates(inputFile, outputFile, keyField);
            System.out.println("Duplicates removed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeDuplicates(String inputFile, String outputFile, String keyField) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        FileWriter writer = new FileWriter(outputFile);

        Map<String, String> uniqueEntries = new HashMap<>();
        String line;

        // Read header and write to output file
        if ((line = reader.readLine()) != null) {
            writer.write(line + System.lineSeparator());
        }

        // Process each line of the input file
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            if (fields.length > 0) {
                String key = fields[0]; 

                // Check if this key is already present (to remove duplicates)
                if (!uniqueEntries.containsKey(key)) {
                    uniqueEntries.put(key, line);
                    writer.write(line + System.lineSeparator());
                }
            }
        }

        reader.close();
        writer.close();
    }
}
```




#
### 5.4 - Get a shallow copy of a HashMap instance
To create a shallow copy of a HashMap in Java, we can use either the copy constructor or the putAll() method.

**Example**: demonstrating a slightly different approach while achieving the same shallow copy result:

```java
import java.util.HashMap;

public class ShallowCopyHashMap {
    public static void main(String[] args) {
        // Original HashMap
        HashMap<Integer, Employee> originalMap = new HashMap<>();
        originalMap.put(1, new Employee("Nite", 1001));
        originalMap.put(2, new Employee("Light", 1002));
        originalMap.put(3, new Employee("Emilia", 1003));

        // Creating a shallow copy using putAll() method
        HashMap<Integer, Employee> copyMap = new HashMap<>();
        for (Integer key : originalMap.keySet()) {
            copyMap.put(key, originalMap.get(key));
        }

        // Adding an element to the original map
        originalMap.put(4, new Employee("David", 1004));

        // Displaying both maps
        System.out.println("Original HashMap: " + originalMap);
        System.out.println("Shallow Copy HashMap: " + copyMap);
    }

    // Example Employee class for demonstration
    static class Employee {
        String name;
        int id;

        Employee(String name, int id) {
            this.name = name;
            this.id = id;
        }

        @Override
        public String toString() {
            return "Employee{name='" + name + "', id=" + id + '}';
        }
    }
}
```
- We create a HashMap named originalMap that associates Integer keys with Employee objects.
- Three Employee objects are added to originalMap with IDs 1, 2, and 3. Each Employee has a name and an ID.
- HashMap named copyMap to store a shallow copy of originalMap.
- Using a loop, we iterate through each key (Integer) in originalMap.
- For each key, we retrieve the corresponding Employee object from originalMap (originalMap.get(key)) and add it to copyMap using copyMap.put(key, originalMap.get(key)).
- This effectively copies the references to Employee objects from originalMap to copyMap, ensuring both maps point to the same Employee objects.
- After creating the shallow copy (copyMap), we add a new Employee object ("David") with ID 1004 to originalMap using originalMap.put(4, new Employee("David", 1004));.
- The output shows that originalMap contains four Employee entries, including "David", while copyMap only contains the original three entries ("Nite", "Light", "Emilia").
- This demonstrates that modifying originalMap after creating copyMap does not affect the contents of copyMap, confirming that it's a shallow copy.

Modifications made to the originalMap after creating the shallow copy (copyMap) do not affect copyMap, illustrating the concept of shallow copying where references to objects are copied rather than creating new objects.
#
### 5.5 - Convert List to Map 
Converting a List to a Map is a common operation in Java, especially when you have a list of objects where each object has a unique identifier (key) that you want to use as keys in the map

#### Example: Convert List to Map in Java
Suppose we have a list of Employee objects, and each Employee has an ID (Integer) that we want to use as the key in the map.

```java
import java.util.*;

public class ListToMapExample {
    public static void main(String[] args) {
        // Creating a list of Employee objects
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(1001, "Alice"));
        employees.add(new Employee(1002, "Bob"));
        employees.add(new Employee(1003, "Charlie"));

        // Converting List to Map
        Map<Integer, Employee> employeeMap = convertListToMap(employees);

        // Displaying the resulting Map
        System.out.println("Map created from List:");
        employeeMap.forEach((key, value) -> System.out.println(key + ": " + value));
    }

    // Method to convert List<Employee> to Map<Integer, Employee>
    public static Map<Integer, Employee> convertListToMap(List<Employee> list) {
        Map<Integer, Employee> map = new HashMap<>();
        for (Employee employee : list) {
            map.put(employee.getId(), employee);
        }
        return map;
    }

    // Employee class definition
    static class Employee {
        private int id;
        private String name;

        public Employee(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name='" + name + "'}";
        }
    }
}
```
This program convert a List of Employee objects into a Map<Integer, Employee>, where each Employee's ID serves as the key in the map. The convertListToMap method iterates over each Employee in the list and populates a new HashMap with id as keys and Employee objects as values.
#
### 5.6 - Demo CopyOnWriteArrayList when modify item
CopyOnWriteArrayList is a specialized concurrent List implementation in Java that provides thread-safe iteration without the need for external synchronization. It achieves thread-safety by making a fresh copy of the underlying array whenever modification operations (add, set, remove, etc.) are performed. This copy-on-write strategy ensures that the original array (snapshot) remains unchanged during iteration, hence the name.
#### Example
```java
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListExample {
    public static void main(String[] args) {
        // Creating a CopyOnWriteArrayList
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

        // Adding elements to the list
        list.add("Alice");
        list.add("Bob");
        list.add("Charlie");

        // Creating a thread to modify the list concurrently
        Thread thread = new Thread(() -> {
            System.out.println("Thread starts modifying the list...");
            try {
                Thread.sleep(1000); // Simulating some work
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add("David");
            System.out.println("Modification completed.");
        });
        thread.start();

        // Iterating over the list
        System.out.println("Iterating over the CopyOnWriteArrayList:");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.println(element);
            // Adding an element while iterating (will not throw ConcurrentModificationException)
            if (element.equals("Bob")) {
                list.add("Eve");
            }
        }

        // Waiting for the thread to finish
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Displaying the final contents of the list
        System.out.println("Final contents of CopyOnWriteArrayList: " + list);
    }
}
```
`Creating CopyOnWriteArrayList`

- We create a CopyOnWriteArrayList<String> named list, which will hold strings.
Adding Elements:

- Strings "Alice", "Bob", and "Charlie" are added to list using list.add().

`Concurrent Modification`
- We create a new thread that attempts to add "David" to the list after a short delay (Thread.sleep(1000)).
- This simulates a concurrent modification scenario.

`Iterating Over the List`

- Start iterating over list using an Iterator.
- During iteration, we print each element (Alice, Bob, Charlie).
- As we iterate, we also add "Eve" to the list when the element "Bob" is encountered.

`Thread Completion`

- We wait for the modification thread to complete its operation (thread.join()).

`Final Contents of List`

- After all operations, including concurrent modifications and iteration, we print the final contents of list.
  
CopyOnWriteArrayList provides thread-safe iteration without throwing ConcurrentModificationException. It achieves this by copying the entire array whenever modifications are made, ensuring that ongoing iterations operate on a consistent snapshot. Modifications made during iteration are visible in subsequent iterations due to the copy-on-write nature.
#
### 5.7 - Demo ConcurrencyHashMap
ConcurrentHashMap is a highly efficient thread-safe implementation of the Map interface in Java. It allows concurrent access to different parts of the map, ensuring thread-safety without blocking entire map operations. This makes it suitable for scenarios where multiple threads need to read and write to the map simultaneously.

#### Example
```java
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapExample {
    public static void main(String[] args) {
        // Creating a ConcurrentHashMap with Integer keys and String values
        ConcurrentHashMap<Integer, String> map = new ConcurrentHashMap<>();

        // Adding elements to the map
        map.put(1, "Alice");
        map.put(2, "Bob");
        map.put(3, "Charlie");

        // Displaying the initial map contents
        System.out.println("Initial ConcurrentHashMap: " + map);

        // Creating multiple threads to modify and read from the map concurrently
        Thread writerThread = new Thread(() -> {
            map.put(4, "David");
            System.out.println("Writer thread added David");
        });

        Thread readerThread = new Thread(() -> {
            for (Integer key : map.keySet()) {
                System.out.println("Reader thread - Key: " + key + ", Value: " + map.get(key));
            }
        });

        // Starting the threads
        writerThread.start();
        readerThread.start();

        // Waiting for threads to complete
        try {
            writerThread.join();
            readerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Displaying the final contents of the map
        System.out.println("Final ConcurrentHashMap: " + map);
    }
}
```
#### Sample Output
```
Initial ConcurrentHashMap: {1=Alice, 2=Bob, 3=Charlie}
Reader thread - Key: 1, Value: Alice
Reader thread - Key: 2, Value: Bob
Reader thread - Key: 3, Value: Charlie
Writer thread added David
Reader thread - Key: 4, Value: David
Final ConcurrentHashMap: {1=Alice, 2=Bob, 3=Charlie, 4=David}
```
- The initial map contents are displayed.
- The reader thread iterates over the map and prints each key-value pair it reads.
- The writer thread adds "4 -> David" to the map and prints its message.
- The reader thread, after the writer thread completes, iterates again and shows the updated map.
- Finally, the main thread prints the final contents of the map.
#
- ConcurrentHashMap allows concurrent access to different parts of the map, providing thread-safety without blocking operations.
- It supports high concurrency for both read and write operations.
- Modifications (adds, removes, updates) are thread-safe and do not require external synchronization.
- Iteration over the map is also safe and reflects the most recent state of the map.
- ConcurrentHashMap is particularly useful in multithreaded applications where multiple threads need to access and modify a shared map concurrently.


#
### 5.8 - Analysis and Implementation of equals() and hashCode() in Java
In Java, the equals() and hashCode() methods are fundamental for object comparison and collection manipulation. These methods are essential when creating custom objects, especially if the objects will be stored in collections like HashSet or used as keys in HashMap. This report delves into the purpose, contract, and best practices for overriding these methods and illustrates practical applications with code examples.

#### The equals() Method
`Purpose`
The equals() method in Java is used to compare two objects for logical equality. By default, the equals() method in the Object class checks if two references point to the same object, which is reference equality. However, for many applications, logical equality—comparison of actual object data—is required.

`Contract for equals()`
The equals() method must adhere to several key properties to function correctly:

- Reflexive: For any non-null reference value x, x.equals(x) must return true.
- Symmetric: For any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
- Transitive: For any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
- Consistent: For any non-null reference values x and y, multiple invocations of x.equals(y) should consistently return true or false, provided no information used in equals comparisons is modified.
- Non-nullity: For any non-null reference value x, x.equals(null) must return false.

Implementation Example
Consider the Employee class. We aim to compare Employee objects based on their id field to ensure logical equality:

```java
public class Employee {
    private int id;
    private String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return id == employee.id; // Logical equality based on 'id' field
    }
}
```
In this example, two Employee objects are considered equal if their id values are the same, adhering to the logical equality principle.

#### The hashCode() Method
`Purpose`
The hashCode() method provides an integer hash code that represents the object. This hash code is used by hash-based collections such as HashMap and HashSet to efficiently store and retrieve objects. The hashCode() method must be overridden whenever the equals() method is overridden to ensure consistent behavior.

`Contract for hashCode()`
The hashCode() method must fulfill the following requirements:

- Consistency: The hash code must consistently return the same value if no object information used in equals comparisons is modified.
- Equal Objects: If two objects are equal according to the equals() method, their hashCode() method must return the same integer.
- Unequal Objects: It is not mandatory, but ideally, different objects should produce different hash codes to minimize hash collisions.

Implementation Example
Continuing with the Employee class, we override hashCode() to return a hash based on the id field:

```java
import java.util.Objects;

public class Employee {
    private int id;
    private String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hash based on 'id' field
    }
}
```

The Objects.hash(id) method generates a hash code using the id field, ensuring that equal Employee objects have the same hash code.

#
### 5.9 - Recognizing Duplicate Employees by ID in a HashSet
Necessity for equals() and hashCode()
To store Employee objects in a HashSet and identify duplicates based on id, both equals() and hashCode() must be overridden. The HashSet uses hashCode() to determine the bucket for storage and equals() to check for equality among objects in the same bucket.

```java
import java.util.HashSet;
import java.util.Set;

public class EmployeeHashSet {
    public static void main(String[] args) {
        Set<Employee> employees = new HashSet<>();

        Employee e1 = new Employee(1, "Alice");
        Employee e2 = new Employee(2, "Bob");
        Employee e3 = new Employee(1, "Charlie"); // Duplicate ID as e1

        employees.add(e1);
        employees.add(e2);
        employees.add(e3); // e3 won't be added as it is considered a duplicate of e1

        System.out.println("HashSet size: " + employees.size()); // Output: 2
        employees.forEach(System.out::println); // Prints: Alice and Bob
    }
}
```
In this implementation, adding e3 does not increase the set size because it is considered equal to e1.

#
### 5.10 - Creating a Map with Composite Key (Department, EmployeeID)
`Concept of Composite Key`
To use a combination of department and employeeID as a key in a Map, a composite key class is necessary. This class should properly override equals() and hashCode() to ensure each key is unique and the Map handles it correctly.
```java
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EmployeeComposite {
    public static void main(String[] args) {
        Map<EmployeeKey, Employee> employeeMap = new HashMap<>();

        EmployeeKey key1 = new EmployeeKey("HR", 101);
        Employee e1 = new Employee(101, "Alice");

        EmployeeKey key2 = new EmployeeKey("Finance", 102);
        Employee e2 = new Employee(102, "Bob");

        EmployeeKey key3 = new EmployeeKey("HR", 103);
        Employee e3 = new Employee(103, "Charlie");

        employeeMap.put(key1, e1);
        employeeMap.put(key2, e2);
        employeeMap.put(key3, e3);

        System.out.println("Employees in HR department:");
        employeeMap.entrySet().stream()
                .filter(entry -> "HR".equals(entry.getKey().getDepartment()))
                .forEach(entry -> System.out.println(entry.getValue()));
        
        EmployeeKey searchKey = new EmployeeKey("Finance", 102);
        Employee foundEmployee = employeeMap.get(searchKey);
        System.out.println("Employee found: " + foundEmployee); // Output: Bob
    }
}

class EmployeeKey {
    private final String department;
    private final int employeeID;

    public EmployeeKey(String department, int employeeID) {
        this.department = department;
        this.employeeID = employeeID;
    }

    public String getDepartment() {
        return department;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EmployeeKey that = (EmployeeKey) obj;
        return employeeID == that.employeeID && Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(department, employeeID);
    }
}
```
This code demonstrates how to use a composite key in a Map to ensure each Employee can be uniquely identified by both their department and employeeID.

#
### 5.11 - Analyzing and Fixing Code for ConcurrentModificationException
`Issue with Modification During Iteration`
Modifying an ArrayList during iteration using a for-each loop results in a ConcurrentModificationException. This exception occurs because the collection's structure changes while an iterator is being used.

`Fixed Implementation`
Using an Iterator directly provides a safe way to remove elements during iteration:
```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void demo() {
        List<String> data = new ArrayList<>();
        data.add("Joe");
        data.add("Helen");
        data.add("Test");
        data.add("Test");
        data.add("Rien");
        data.add("Ruby");

        Iterator<String> iterator = data.iterator();
        while (iterator.hasNext()) {
            String d = iterator.next();
            if (d.equals("Test")) {
                iterator.remove(); // Safe removal
            }
        }

        System.out.println(data); // Output: [Joe, Helen, Rien, Ruby]
    }

    public static void main(String[] args) {
        demo();
    }
}
```
This approach avoids ConcurrentModificationException by using the Iterator's remove() method, which ensures thread-safe modification.

#### Handling Concurrent Access in Java
`Problem with Concurrent Access`
Concurrent modification of collections like ArrayList can lead to race conditions and unpredictable behavior. Using concurrent collections such as CopyOnWriteArrayList or synchronization mechanisms ensures safe access.

`Concurrent Access Example`
Using CopyOnWriteArrayList for thread-safe operations:
```java
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    public static void demo() {
        List<String> data = new CopyOnWriteArrayList<>();
        data.add("Joe");
        data.add("Helen");
        data.add("Test");
        data.add("Test");
        data.add("Rien");
        data.add("Ruby");

        for (String d : data) {
            if (d.equals("Test")) {
                data.remove(d); // Safe removal in concurrent environment
            }
        }

        System.out.println(data); // Output: [Joe, Helen, Rien, Ruby]
    }

    public static void main(String[] args) {
        demo();
    }
}
```
This ensures that the list is safely modified even in a concurrent environment, eliminating the risk of ConcurrentModificationException.


#
### 5.12 - What happen multiple threads to access and modify a shared collection concurrently
`Key Issues with Concurrent Access and Modification`

**1. ConcurrentModificationException**
   
    ConcurrentModificationException occurs when one thread modifies a collection while another thread is iterating over it, and the modification is not synchronized properly. This exception is thrown to indicate that the collection has been structurally modified in an unexpected way. Structural modifications are changes that affect the size of the collection, such as adding or removing elements.
**2. Data Inconsistency**
   
    When multiple threads modify a shared collection without proper synchronization, data inconsistencies can occur. This happens because the threads can read and write values in an interleaved manner, leading to unpredictable results.

    Example Scenario
    Imagine two threads attempting to add different elements to the same ArrayList at the same time. The internal state of the list might become inconsistent, leading to lost updates or duplicate entries.

**3. Race Conditions**
   
    Race conditions occur when the outcome of a program depends on the relative timing of events, such as the order in which threads access shared resources. If one thread reads a collection while another modifies it, the final state of the collection might depend on the precise timing of these operations, leading to unpredictable behavior.

**4. Deadlocks and Livelocks**
   
    If thread synchronization is not handled correctly, it can lead to deadlocks, where two or more threads are stuck waiting for each other to release resources, or livelocks, where threads keep changing state in response to each other without making any progress.
