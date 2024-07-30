package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.ApiKey;
import jebi.hendardi.spring.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApiKeyService {
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public boolean isValidApiKey(String key) {
        return apiKeyRepository.findByKey(key) != null;
    }

    public String getUsernameByApiKey(String key) {
        ApiKey apiKey = apiKeyRepository.findByKey(key);
        if (apiKey != null) {
            apiKey.setLastUsed(LocalDateTime.now());
            apiKeyRepository.save(apiKey);
            return apiKey.getUsername();
        }
        return null;
    }
}
