package jebi.hendardi.spring.repository;

import jebi.hendardi.spring.entity.DeptEmp;
import jebi.hendardi.spring.entity.DeptEmpId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptEmpRepository extends JpaRepository<DeptEmp, DeptEmpId> {}
