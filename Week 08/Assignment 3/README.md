### 📘 Project Documentation: Simple Interceptor
#
### 🎯 Project Objective

The objective of this project is to create a simple interceptor for a Spring Boot application. The interceptor should perform the following tasks:
1. Store the username for each API key and add the username to the request header.
2. Print the username in a function within the controller.
3. Include a `timestamp` header in all responses returned to the client.
4. Store the last time the API key was used.


#
### 🔄 Updates and Implementation

#### 1. Store Username for Each API Key
- The `ApiKey` entity was updated to include a `username` field.
- The `ApiKeyService` was modified to retrieve the username associated with the API key and set it in the request header.
- The `ApiKeyFilter` was updated to add the username to the request header.

#### 2. Print Username in Controller
- A method was added to the `ProductController` to print the username stored in the request header.

#### 3. Include `timestamp` Header in All Responses
- The `ApiKeyFilter` was updated to add a `timestamp` header to each response, indicating the current time.

#### 4. Store the Last Time the API Key Was Used
- The `ApiKey` entity was updated to include a `lastUsed` field.
- The `ApiKeyService` was modified to update the `lastUsed` field whenever an API key is used.

#
### 🛠️ Updated Code

### `ApiKey` Entity

```java
package jebi.hendardi.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "apikey")
public class ApiKey {
    @Id
    private String id;

    @Column(name = "xkey")
    private String key;

    @Column(name = "username")
    private String username;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;
}
```

### `ApiKeyService`

```java
package jebi.hendardi.spring.service;

import jebi.hendardi.spring.entity.ApiKey;
import jebi.hendardi.spring.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApiKeyService {
    @Autowired
    private ApiKeyRepository apiKeyRepository;

    public boolean isValidApiKey(String key) {
        return apiKeyRepository.findByKey(key) != null;
    }

    public String getUsernameByApiKey(String key) {
        ApiKey apiKey = apiKeyRepository.findByKey(key);
        if (apiKey != null) {
            apiKey.setLastUsed(LocalDateTime.now());
            apiKeyRepository.save(apiKey);
            return apiKey.getUsername();
        }
        return null;
    }
}
```

### `ApiKeyFilter`

```java
package jebi.hendardi.spring.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

        String username = apiKeyService.getUsernameByApiKey(apiKey);
        if (username != null) {
            response.addHeader("username", username);
            request.setAttribute("username", username);
        }

        response.addHeader("source", "fpt-software");
        response.addHeader("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        filterChain.doFilter(request, response);
    }
}
```

### `ProductController`

```java
package jebi.hendardi.spring.controller;

import jebi.hendardi.spring.entity.Product;
import jebi.hendardi.spring.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        printUsername();
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        printUsername();
        Product product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        printUsername();
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        printUsername();
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        printUsername();
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private void printUsername() {
        String username = (String) request.getAttribute("username");
        System.out.println("Username: " + username);
    }
}
```
#
### 🌐 Endpoint Table

| Method | Endpoint           | Description                   | Headers                  | Body (JSON)                           |
|--------|--------------------|-------------------------------|--------------------------|---------------------------------------|
| GET    | /products          | Get all products              | api-key                  |                                       |
| GET    | /products/{id}     | Get product by ID             | api-key                  |                                       |
| POST   | /products          | Create a new product          | api-key                  | {"name": "Product D", "price": 40.0}  |
| PUT    | /products/{id}     | Update an existing product    | api-key                  | {"name": "Product A Updated", "price": 15.0} |
| DELETE | /products/{id}     | Delete a product              | api-key                  |                                       |

            |                                   |
#

### 📊 Initial Data for Testing

To test the endpoints, you can insert the following initial data into the database:

#### Insert API Keys and products

```sql
INSERT INTO apikey (id, xkey, username, last_used) VALUES
('1', 'apikeyjebi', 'jebihendardi', NULL),
('2', 'apikeynite', 'nitewalter', NULL);
```
```sql
INSERT INTO product (id, name, price) VALUES
(1, 'Product A', 10.0),
(2, 'Product B', 20.0),
(3, 'Product C', 30.0);
```

#
### 🧪 Postman Test Table

| Test Description                | Method | Endpoint           | Headers           | Body (JSON)                           | Expected Response               |
|---------------------------------|--------|--------------------|-------------------|---------------------------------------|---------------------------------|
| Get all products                | GET    | /products          | api-key           |                                       | 200 OK, List of products        |
| Get product by ID               | GET    | /products/1        | api-key           |                                       | 200 OK, Product details         |
| Create a new product            | POST   | /products          | api-key           | {"name": "Product D", "price": 40.0}  | 201 Created, New product details |
| Update an existing product      | PUT    | /products/1        | api-key           | {"name": "Product A Updated", "price": 15.0} | 200 OK, Updated product details |
| Delete a product                | DELETE | /products/1        | api-key           |                                       | 204 No Content                  |
| Get product with invalid API key| GET    | /products          | invalid-api-key   |                                       | 401 Unauthorized                |



#
### ✅ Test Cases

### 1. Add Product

- **Body**
  
  ![alt text](image.png)

---

- **Authorization With Unregistered API Key**
    
  ![alt text](image-1.png)

---

- **Authorization With Correct API Key**
  
  **Response : Body** 
      
  ![alt text](image-2.png)

  **Response : Header**
      
  ![alt text](image-3.png)

---

### 2. Update Product

- **Body**
  
  ![alt text](image-4.png)

---

- **Authorization With Unregistered API Key**
    
  ![alt text](image-5.png)

---

- **Authorization With Correct API Key**
  
  **Response : Body** 
      
  ![alt text](image-6.png)

  **Response : Header**
      
  ![alt text](image-7.png)

---

### 3. Get Product

- **Authorization With Unregistered API Key**
    
  ![alt text](image-8.png)

---

- **Authorization With Correct API Key**
  
  **Response : Body** 
      
  ![alt text](image-9.png)

  **Response : Header**
      
  ![alt text](image-10.png)

---

### 4. Delete Product

- **Authorization With Unregistered API Key**
    
  ![alt text](image-11.png)

---

- **Authorization With Correct API Key**
  
  **Response : Body** 
      
  ![alt text](image-12.png)

  **Response : Header**
      
  ![alt text](image-13.png)