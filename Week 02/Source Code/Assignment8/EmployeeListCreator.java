import java.util.ArrayList;
import java.util.List;

public class EmployeeListCreator {
    public static List<Employee> createEmployeeList() {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Alice", 101, 50000));
        employees.add(new Employee("Bob", 102, 60000));
        employees.add(new Employee("Charlie", 103, 70000));
        return employees;
    }
}
