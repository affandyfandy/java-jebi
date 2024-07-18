package jebi.hendardi.spring.repository;

import jebi.hendardi.spring.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, String> {}
