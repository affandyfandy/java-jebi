package jebi.hendardi.webClient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class ApiService {

    private final WebClient webClient;

    @Autowired
    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> postData() {
        return webClient.post()
                .uri("/post")
                .bodyValue("data")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new WebClientResponseException(
                                "Client Error",
                                response.statusCode().value(),
                                response.statusCode().toString(),
                                null, null, null
                        ))
                )
                .bodyToMono(String.class);
    }
}
