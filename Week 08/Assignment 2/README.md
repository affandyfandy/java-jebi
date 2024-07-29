# Spring Boot CRUD Project with API Key Verification

## Project Structure

The project is structured as follows:

- **Entities**
  - `ApiKey.java`
  - `Product.java`
- **Repositories**
  - `ApiKeyRepository.java`
  - `ProductRepository.java`
- **Services**
  - `ApiKeyService.java`
  - `ProductService.java`
- **Controllers**
  - `ProductController.java`
- **Filters**
  - `ApiKeyFilter.java`
- **Configuration**
  - `FilterConfig.java`

## Entities

### `ApiKey.java`

Represents the API key entity used for request verification.

**Explanation**:
- **Annotation**: `@Entity` denotes that this class is a JPA entity.
- **Fields**:
  - `id`: Primary key of type UUID.
  - `key`: The actual API key value.
- **Purpose**: To store API key values for authentication purposes.

### `Product.java`

Represents the product entity with details such as name and price.

**Explanation**:
- **Annotation**: `@Entity` denotes that this class is a JPA entity.
- **Fields**:
  - `id`: Primary key of type UUID.
  - `name`: Name of the product.
  - `price`: Price of the product.
- **Purpose**: To manage product information in the database.

## Repositories

### `ApiKeyRepository.java`

Interface for accessing `ApiKey` data in the database.

**Explanation**:
- **Extends**: `JpaRepository<ApiKey, UUID>`.
- **Methods**:
  - `findByKey(String key)`: Retrieves an `ApiKey` entity by its key value.
- **Purpose**: Provides CRUD operations and custom queries for the `ApiKey` entity.

### `ProductRepository.java`

Interface for accessing `Product` data in the database.

**Explanation**:
- **Extends**: `JpaRepository<Product, UUID>`.
- **Methods**:
  - Standard CRUD operations inherited from `JpaRepository`.
- **Purpose**: Provides CRUD operations for the `Product` entity.

## Services

### `ApiKeyService.java`

Service for managing API key validation.

**Explanation**:
- **Dependencies**: Uses `ApiKeyRepository`.
- **Methods**:
  - `isValidApiKey(String key)`: Checks if the API key is present in the database.
- **Purpose**: Handles business logic related to API key verification.

### `ProductService.java`

Service for managing CRUD operations for products.

**Explanation**:
- **Dependencies**: Uses `ProductRepository`.
- **Methods**:
  - `getAllProducts()`: Retrieves all products.
  - `getProductById(UUID id)`: Retrieves a product by ID.
  - `createProduct(Product product)`: Creates a new product.
  - `updateProduct(UUID id, Product product)`: Updates an existing product.
  - `deleteProduct(UUID id)`: Deletes a product by ID.
- **Purpose**: Contains business logic for handling product data.

## Controllers

### `ProductController.java`

Handles HTTP requests related to products.

**Explanation**:
- **Annotations**: `@RestController` and `@RequestMapping("/products")`.
- **Endpoints**:
  - `GET /products`: Retrieves all products.
  - `GET /products/{id}`: Retrieves a product by ID.
  - `POST /products`: Creates a new product.
  - `PUT /products/{id}`: Updates an existing product.
  - `DELETE /products/{id}`: Deletes a product by ID.
- **Purpose**: Manages the HTTP layer for product-related operations.

## Filters

### `ApiKeyFilter.java`

Filters incoming HTTP requests to verify the presence and validity of the `api-key` header.

**Explanation**:
- **Annotation**: Implements `javax.servlet.Filter`.
- **Methods**:
  - `doFilter(ServletRequest request, ServletResponse response, FilterChain chain)`: Checks the `api-key` header and validates it using `ApiKeyService`. Adds `source` header to the response.
- **Purpose**: Ensures that requests are authenticated and adds a custom header to responses.

## Configuration

### `FilterConfig.java`

Configures the `ApiKeyFilter` for the application.

**Explanation**:
- **Annotation**: `@Configuration`.
- **Methods**:
  - `addFilters(FilterRegistrationBean<ApiKeyFilter> registrationBean)`: Registers `ApiKeyFilter` with specific URL patterns.
- **Purpose**: Sets up the filter for handling requests and responses.


### Initializes the database with sample data.
```sql
INSERT INTO api_key (id, key) VALUES ('1', 'test-key');
INSERT INTO product (name, price) VALUES ('Product 1', 10.0);
INSERT INTO product (name, price) VALUES ('Product 2', 20.0);
```

## Testing with Postman

### Test Cases

| Request Type | Endpoint           | Headers                                | Body (if applicable)                        | Expected Response       |
|--------------|---------------------|----------------------------------------|---------------------------------------------|-------------------------|
| GET          | `/products`         | `api-key: test-key`                     | N/A                                         | 200 OK, List of products |
| GET          | `/products/{id}`    | `api-key: test-key`                     | N/A                                         | 200 OK, Product details |
| POST         | `/products`         | `api-key: test-key`, `Content-Type: application/json` | `{ "name": "New Product", "price": 30.0 }` | 201 Created, Product created |
| PUT          | `/products/{id}`    | `api-key: test-key`, `Content-Type: application/json` | `{ "name": "Updated Product", "price": 35.0 }` | 200 OK, Product updated |
| DELETE       | `/products/{id}`    | `api-key: test-key`                     | N/A                                         | 200 OK, Product deleted |
