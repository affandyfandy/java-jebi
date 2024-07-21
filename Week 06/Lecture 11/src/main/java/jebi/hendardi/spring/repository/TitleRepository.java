package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jebi.hendardi.spring.entity.Title;
import jebi.hendardi.spring.entity.TitleId;

public interface TitleRepository extends JpaRepository<Title, TitleId> {
    @Query("SELECT t FROM Title t WHERE t.employee.empNo = :empNo ORDER BY t.toDate DESC")
    Title findTopByEmployeeOrderByToDateDesc(@Param("empNo") int empNo);
}
