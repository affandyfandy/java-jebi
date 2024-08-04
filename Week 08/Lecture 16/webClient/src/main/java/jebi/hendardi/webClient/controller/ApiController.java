package jebi.hendardi.webClient.controller;

import jebi.hendardi.webClient.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {
    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/fetch")
    public Mono<String> fetchData() {
        return apiService.postData();
    }
}
