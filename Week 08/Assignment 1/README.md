# `OncePerRequestFilter` in Spring 🌟

## Definition 📖
`OncePerRequestFilter` is an abstract base class for Spring filters that ensures a filter is only executed once per request. It provides a mechanism to handle requests and responses in a consistent way across various filters, preventing multiple executions of the same filter for a single request.

## Function 🔧
The main function of `OncePerRequestFilter` is to ensure that the filter logic within it is applied only once for each request. This is particularly useful in web applications where the same request might pass through multiple filters.

## How to Use 🚀
To use `OncePerRequestFilter`, you need to create a subclass and override the `doFilterInternal` method. This method contains the filter logic and is called only once per request.

### Example
Here's a simple example of a custom filter that extends `OncePerRequestFilter`:

```java
package com.example.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomOncePerRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Custom filter logic
        System.out.println("Executing CustomOncePerRequestFilter");

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
```

## Advantages ✅
- **Single Execution:** Guarantees the filter is executed only once per request, preventing redundant processing.
- **Simplified Filter Logic:** By ensuring single execution, the filter logic is simplified and easier to manage.
- **Integration with Spring:** Seamlessly integrates with Spring's filter chain and can be easily configured as a Spring bean.

## Disadvantages ❌
- **Complex Configuration:** Sometimes, configuring multiple filters and ensuring the correct order can be complex.
- **Performance Overhead:** Though minimal, there is some performance overhead associated with the additional checks to ensure single execution.

## Key Points 🗝️
- **Abstract Class:** `OncePerRequestFilter` is an abstract class that must be subclassed.
- **doFilterInternal Method:** Override the `doFilterInternal` method to implement custom filter logic.
- **Single Execution:** Ensures the filter logic is executed only once per request.
- **Spring Integration:** Easily integrates with Spring's filter mechanism and can be managed as a Spring bean.

## Use Cases 📚
`OncePerRequestFilter` can be used in scenarios such as:
- **Logging:** Log details of incoming requests and outgoing responses.
- **Authentication:** Perform authentication checks once per request.
- **Authorization:** Check user permissions and roles.
- **Request Modification:** Modify request headers or parameters once before processing.

## Integration with Spring Security 🔒
When integrating with Spring Security, `OncePerRequestFilter` can be used to create custom security filters. This ensures that security checks, like token validation or role verification, are performed only once per request.

### Example
```java
package com.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomSecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Perform security check
        if (validateToken(request)) {
            // Set security context
            SecurityContextHolder.getContext().setAuthentication(getAuthentication(request));
        }
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(HttpServletRequest request) {
        // Token validation logic
        return true;
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        // Authentication logic
        return new UsernamePasswordAuthenticationToken("user", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
```

## Testing `OncePerRequestFilter` 🧪
To test `OncePerRequestFilter`, you can use MockMvc to simulate HTTP requests and validate the behavior of your custom filter.

### Example
```java
package com.example.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CustomOncePerRequestFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFilter() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isOk());
    }
}
```

## Configuration ⚙️
To configure your custom `OncePerRequestFilter`, you can declare it as a Spring bean using the `@Component` annotation or by defining it in a configuration class.

### Example with Configuration Class
```java
package com.example.config;

import com.example.filter.CustomOncePerRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public CustomOncePerRequestFilter customOncePerRequestFilter() {
        return new CustomOncePerRequestFilter();
    }
}
```

## Detailed Example 🔍
Here's a more detailed example with additional logging and condition checks:

```java
package com.example.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingOncePerRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingOncePerRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Log request details
        logger.info("Request URI: {}", request.getRequestURI());
        logger.info("Request Method: {}", request.getMethod());

        // Example condition check
        if (request.getRequestURI().contains("/api")) {
            logger.info("API request detected.");
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
        
        // Log response status
        logger.info("Response Status: {}", response.getStatus());
    }
}
```

In this example:
- The filter logs the request URI and method.
- It checks if the request URI contains "/api" and logs a message if it does.
- Finally, it logs the response status after the request is processed.

## Conclusion 🎯
`OncePerRequestFilter` is a powerful tool in the Spring framework that helps manage filter execution in web applications. By ensuring that a filter is only executed once per request, it simplifies the filter logic and improves application performance. It integrates seamlessly with Spring, making it a valuable component for any Spring-based web application.

