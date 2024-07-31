package jebi.hendardi.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jebi.hendardi.spring.entity.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {
    ApiKey findByKey(String key);
}
