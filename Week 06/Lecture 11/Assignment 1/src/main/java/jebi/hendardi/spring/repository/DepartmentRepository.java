package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jebi.hendardi.spring.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, String> {}
