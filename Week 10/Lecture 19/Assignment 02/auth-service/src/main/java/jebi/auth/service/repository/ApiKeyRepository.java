package jebi.auth.service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jebi.auth.service.entity.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByXKEY(String XKEY);
}
