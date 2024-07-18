package jebi.hendardi.spring.repository;

import jebi.hendardi.spring.entity.Title;
import jebi.hendardi.spring.entity.TitleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository extends JpaRepository<Title, TitleId> {}
