package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jebi.hendardi.spring.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {}
