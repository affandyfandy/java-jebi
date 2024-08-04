package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.ApiKey;
import jebi.hendardi.spring.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ApiKeyService {
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public boolean isValidApiKey(String key) {
        return apiKeyRepository.findByKey(key) != null;
    }

    public String getUsernameForKey(String key) {
        ApiKey apiKey = apiKeyRepository.findByKey(key);
        return apiKey != null ? apiKey.getUsername() : null;
    }

    public void updateLastUsed(String key) {
        ApiKey apiKey = apiKeyRepository.findByKey(key);
        if (apiKey != null) {
            apiKey.setLastUsed(Instant.now());
            apiKeyRepository.save(apiKey);
        }
    }
}
