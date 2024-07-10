package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jebi.hendardi.spring.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    List<Employee> findAllByOrderByNameAsc();

    Page<Employee> findAllByOrderByNameAsc(Pageable pageable);
}
