package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jebi.hendardi.spring.entity.Salary;
import jebi.hendardi.spring.entity.SalaryId;

public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {}
