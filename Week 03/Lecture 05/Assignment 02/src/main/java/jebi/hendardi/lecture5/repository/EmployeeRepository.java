package jebi.hendardi.lecture5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jebi.hendardi.lecture5.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {  // Mengganti Long dengan String
    void deleteEmployeeByEmployeeID(String employeeID);  // Mengganti ID dengan employeeID
    List<Employee> findEmployeesByDepartmentIgnoreCase(String department);
}
