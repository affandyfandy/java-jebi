package jebi.auth.service.service;

import org.springframework.stereotype.Service;
import jebi.auth.service.repository.ApiKeyRepository;
import jebi.auth.service.entity.ApiKey;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ApiKeyRepository apiKeyRepository;

    public boolean isValidApiKey(String apiKey) {
        Optional<ApiKey> key = apiKeyRepository.findByXKEY(apiKey);
        return key.isPresent();
    }
}
