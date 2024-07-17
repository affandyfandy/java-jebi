package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jebi.hendardi.spring.entity.DeptEmp;
import jebi.hendardi.spring.entity.DeptEmpId;

public interface DeptEmpRepository extends JpaRepository<DeptEmp, DeptEmpId> {}
