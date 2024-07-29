# Assignment 3: Simple Interceptor

## Overview

This assignment involves implementing a filter to store the username for each API key, adding the username to the header, printing it in a function in the controller, including a "timestamp" in the response header, and storing the last time the API key was used.

## Implementation Details

### 1. Filter Configuration

#### `FilterConfig.java`

This configuration class registers the `ApiKeyFilter` for the `/products/*` URL pattern.

```java
package jebi.hendardi.spring.config;

import jebi.hendardi.spring.filter.ApiKeyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
    private ApiKeyFilter apiKeyFilter;

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration() {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/products/*");
        return registrationBean;
    }
}
```

- **Explanation**: The `FilterConfig` class configures the `ApiKeyFilter` to be applied to all endpoints under `/products/*`. The `FilterRegistrationBean` registers the filter and associates it with the specified URL pattern.
#
### 2. API Key Entity

#### `ApiKey.java`

This entity represents an API key with an associated username.

```java
package jebi.hendardi.spring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ApiKey {
    @Id
    private String id;
    private String key;
    private String username;
    private Long lastUsed;
}
```

- **Explanation**: The `ApiKey` entity contains fields for the `id`, `key`, `username`, and `lastUsed` timestamp. The `lastUsed` field tracks the last time the API key was used.
#
### 3. API Key Filter

#### `ApiKeyFilter.java`

This filter validates API keys, adds the username to the request, and tracks the usage time.

```java
package jebi.hendardi.spring.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jebi.hendardi.spring.service.ApiKeyService;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader("api-key");

        if (apiKey == null || !apiKeyService.isValidApiKey(apiKey)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return;
        }

        // Add username to header and track last usage time
        String username = apiKeyService.getUsernameForApiKey(apiKey);
        response.addHeader("username", username);
        response.addHeader("timestamp", String.valueOf(System.currentTimeMillis()));
        apiKeyService.updateLastUsed(apiKey);

        filterChain.doFilter(request, response);
    }
}
```

- **Explanation**: The `ApiKeyFilter` extends `OncePerRequestFilter` to ensure it is executed only once per request. It retrieves the `api-key` from the request header, validates it using `ApiKeyService`, adds the `username` and `timestamp` to the response header, and updates the last usage time of the API key.
#
### 4. API Key Service

#### `ApiKeyService.java`

This service provides methods to validate API keys and manage their associated data.

```java
package jebi.hendardi.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jebi.hendardi.spring.entity.ApiKey;
import jebi.hendardi.spring.repository.ApiKeyRepository;

@Service
public class ApiKeyService {
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public boolean isValidApiKey(String key) {
        return apiKeyRepository.findByKey(key) != null;
    }

    public String getUsernameForApiKey(String key) {
        ApiKey apiKey = apiKeyRepository.findByKey(key);
        return apiKey != null ? apiKey.getUsername() : null;
    }

    public void updateLastUsed(String key) {
        ApiKey apiKey = apiKeyRepository.findByKey(key);
        if (apiKey != null) {
            apiKey.setLastUsed(System.currentTimeMillis());
            apiKeyRepository.save(apiKey);
        }
    }
}
```

- **Explanation**: The `ApiKeyService` provides methods to check if an API key is valid (`isValidApiKey`), retrieve the username associated with an API key (`getUsernameForApiKey`), and update the last usage timestamp (`updateLastUsed`).
#
### 5. Product Controller

#### `ProductController.java`

The controller handles product-related operations and prints the username from the request header.

```java
package jebi.hendardi.spring.controller;

import jebi.hendardi.spring.entity.Product;
import jebi.hendardi.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestHeader("username") String username) {
        System.out.println("API called by user: " + username);
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@RequestHeader("username") String username, @PathVariable Long id) {
        System.out.println("API called by user: " + username);
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestHeader("username") String username, @RequestBody Product product) {
        System.out.println("API called by user: " + username);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestHeader("username") String username, @PathVariable Long id, @RequestBody Product product) {
        System.out.println("API called by user: " + username);
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@RequestHeader("username") String username, @PathVariable Long id) {
        System.out.println("API called by user: " + username);
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
```

- **Explanation**: The `ProductController` manages CRUD operations for products. For each method, the username is extracted from the request header (`@RequestHeader("username") String username`) and printed to the console. This demonstrates how the username, added to the response headers by the `ApiKeyFilter`, can be accessed and utilized in controller methods.



# Testing

| No | Method | Endpoint        | Headers                  | Body (JSON)                          | Expected Response  | Description                                       |
|----|--------|-----------------|--------------------------|--------------------------------------|--------------------|---------------------------------------------------|
| 1  | GET    | /products       | api-key: {validApiKey}   |                                      | 200 OK             | Fetch all products. Username and timestamp headers should be included. |
| 2  | GET    | /products       | api-key: {invalidApiKey} |                                      | 401 Unauthorized   | Invalid API key should return unauthorized error. |
| 3  | GET    | /products/{id}  | api-key: {validApiKey}   |                                      | 200 OK / 404 Not Found | Fetch specific product by ID. Username and timestamp headers should be included. |
| 4  | POST   | /products       | api-key: {validApiKey}   | { "name": "Product A", "price": 10.0 } | 201 Created        | Create a new product. Username and timestamp headers should be included. |
| 5  | PUT    | /products/{id}  | api-key: {validApiKey}   | { "name": "Product A", "price": 12.0 } | 200 OK / 404 Not Found | Update an existing product by ID. Username and timestamp headers should be included. |
| 6  | DELETE | /products/{id}  | api-key: {validApiKey}   |                                      | 204 No Content     | Delete a product by ID. Username and timestamp headers should be included. |

