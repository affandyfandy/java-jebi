package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jebi.hendardi.spring.entity.Title;
import jebi.hendardi.spring.entity.TitleId;

public interface TitleRepository extends JpaRepository<Title, TitleId> {}
