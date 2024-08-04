package jebi.hendardi.restTemplate.service.impl;

import jebi.hendardi.restTemplate.model.User;
import jebi.hendardi.restTemplate.service.UserConsumerService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserConsumerServiceImpl implements UserConsumerService {

    private static final String BASE_URL = "http://localhost:8080/api/v1/users";
    private final RestTemplate restTemplate;

    public UserConsumerServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<String> getUserNames() {
        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(BASE_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {});
        List<User> users = responseEntity.getBody();
        return users.stream().map(User::getName).collect(Collectors.toList());
    }
}
