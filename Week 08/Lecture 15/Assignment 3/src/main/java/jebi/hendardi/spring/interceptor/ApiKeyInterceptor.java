package jebi.hendardi.spring.interceptor;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jebi.hendardi.spring.service.ApiKeyService;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String apiKey = request.getHeader("api-key");

        if (apiKey == null || !apiKeyService.isValidApiKey(apiKey)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return false;
        }

        String username = apiKeyService.getUsernameForKey(apiKey);
        request.setAttribute("username", username);
        request.setAttribute("apiKey", apiKey);
        response.addHeader("username", username);
        response.addHeader("timestamp", Instant.now().toString());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String apiKey = (String) request.getAttribute("apiKey");
        if (apiKey != null) {
            apiKeyService.updateLastUsed(apiKey);
        }
        response.addHeader("timestamp_after", Instant.now().toString());
    }
}
