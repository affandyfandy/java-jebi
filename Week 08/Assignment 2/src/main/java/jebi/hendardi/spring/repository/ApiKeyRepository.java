package jebi.hendardi.spring.repository;

import jebi.hendardi.spring.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {
    ApiKey findByKey(String key);
}
