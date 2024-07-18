package jebi.hendardi.spring.repository;

import jebi.hendardi.spring.entity.Salary;
import jebi.hendardi.spring.entity.SalaryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {}
