package jebi.hendardi.webClient.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8080"))
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .responseTimeout(java.time.Duration.ofMillis(5000))
                        .doOnConnected(conn ->
                                conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                        .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)))))
                .build();
    }
}
