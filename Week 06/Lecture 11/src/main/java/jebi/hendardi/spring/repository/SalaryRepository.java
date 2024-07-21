package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jebi.hendardi.spring.entity.Salary;
import jebi.hendardi.spring.entity.SalaryId;

public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {
    @Query("SELECT s FROM Salary s WHERE s.employee.empNo = :empNo ORDER BY s.toDate DESC")
    Salary findTopByEmployeeOrderByToDateDesc(@Param("empNo") int empNo);
}
