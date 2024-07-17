package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jebi.hendardi.spring.entity.DeptManager;
import jebi.hendardi.spring.entity.DeptManagerId;

public interface DeptManagerRepository extends JpaRepository<DeptManager, DeptManagerId> {}
