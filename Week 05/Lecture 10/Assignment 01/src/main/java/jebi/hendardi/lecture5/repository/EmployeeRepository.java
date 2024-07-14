package jebi.hendardi.lecture5.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import jebi.hendardi.lecture5.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    void deleteEmployeeById(UUID id);
    List<Employee> findEmployeesByDepartmentIgnoreCase(String department);
}
