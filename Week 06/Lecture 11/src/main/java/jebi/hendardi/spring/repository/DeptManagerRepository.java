package jebi.hendardi.spring.repository;

import jebi.hendardi.spring.entity.DeptManager;
import jebi.hendardi.spring.entity.DeptManagerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptManagerRepository extends JpaRepository<DeptManager, DeptManagerId> {}
