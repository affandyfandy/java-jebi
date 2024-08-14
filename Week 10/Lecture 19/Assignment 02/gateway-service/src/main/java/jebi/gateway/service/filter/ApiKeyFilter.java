package jebi.gateway.service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import java.nio.charset.StandardCharsets;

@Component
@Order(1)
public class ApiKeyFilter implements GlobalFilter {
    private final WebClient.Builder webClientBuilder;

    public ApiKeyFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String apiKey = exchange.getRequest().getHeaders().getFirst("api-key");

        if (apiKey == null || apiKey.isEmpty()) {
            return unauthorizedResponse(exchange, "API key is missing");
        }

        return webClientBuilder.build()
            .get()
            .uri("http://localhost:8083/auth/validate")
            .header("api-key", apiKey)
            .retrieve()
            .bodyToMono(Boolean.class)
            .flatMap(isValid -> {
                if (Boolean.TRUE.equals(isValid)) {
                    return chain.filter(exchange);
                } else {
                    return unauthorizedResponse(exchange, "API key is invalid");
                }
            });
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}
