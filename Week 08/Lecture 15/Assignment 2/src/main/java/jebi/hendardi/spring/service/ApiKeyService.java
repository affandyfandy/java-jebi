package jebi.hendardi.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jebi.hendardi.spring.repository.ApiKeyRepository;

@Service
public class ApiKeyService {
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public boolean isValidApiKey(String key) {
        return apiKeyRepository.findByKey(key) != null;
    }
}
