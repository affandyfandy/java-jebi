#
## Compare restful api best practice with your Assignment 2 – lecture 5​

## 1. Accept and Respond with JSON

**Best Practice**: REST APIs should accept JSON for request payloads and respond with JSON. The `Content-Type` header should be set to `application/json`.

**My Implementation**: My Spring Boot controller implicitly handles JSON requests and responses because of the use of `@RestController` and appropriate request/response mappings. This adheres to best practices.

## 2. Use Nouns Instead of Verbs in Endpoint Paths

**Best Practice**: Endpoint paths should use nouns to represent resources and not verbs, as HTTP methods (GET, POST, PUT, DELETE) already define the actions.

**My Implementation**: My endpoint paths (`/employee/all`, `/employee/find/{employeeID}`, `/employee/add`, etc.) mostly follow this convention. However, `/employee/find/{employeeID}` could be simplified to `/employee/{employeeID}` for consistency.

## 3. Name Collections with Plural Nouns

**Best Practice**: Collections of resources should be named with plural nouns.

**My Implementation**: My endpoints (`/employee`, `/employee/{employeeID}`) use singular nouns. Changing `/employee` to `/employees` would better indicate that the endpoint deals with a collection of employee resources.

## 4. Nesting Resources for Hierarchical Objects

**Best Practice**: Nest resources for hierarchical relationships. For example, if departments can have employees, use `/departments/{departmentId}/employees`.

**My Implementation**: I have a department-related endpoint (`/employee/{department}`). This should ideally be nested to reflect the relationship more clearly, like `/departments/{department}/employees`.

## 5. Handle Errors Gracefully and Return Standard Error Codes

**Best Practice**: Use appropriate HTTP status codes to indicate the result of operations and handle errors gracefully.

**My Implementation**: I handle errors and return status codes appropriately. For example, I return `HttpStatus.NOT_FOUND` if an employee is not found. This aligns well with best practices.

## 6. Allow Filtering, Sorting, and Pagination

**Best Practice**: Allow clients to filter, sort, and paginate resources to handle large datasets efficiently.

**My Implementation**: My API does not currently support filtering, sorting, or pagination. Consider adding query parameters to allow for these functionalities, such as `/employees?department={department}&sort=lastName&limit=10&page=2`.

## 7. Maintain Good Security Practices

**Best Practice**: Ensure secure communication (e.g., using SSL/TLS), and implement authentication and authorization to restrict access based on roles.

**My Implementation**: Security aspects are not evident from the provided code. Ensure that my application uses HTTPS and incorporates proper authentication and authorization mechanisms.

## 8. Cache Data to Improve Performance

**Best Practice**: Implement caching to improve performance by reducing the need to query the database repeatedly.

**My Implementation**: Caching is not shown in my code. Consider adding caching mechanisms, such as using Spring Cache or integrating with caching solutions like Redis, to enhance performance.

## 9. Versioning Your APIs

**Best Practice**: Version your APIs to avoid breaking changes for existing clients.

**My Implementation**: API versioning is not visible in my code. We can version our API by adding version numbers to your endpoint paths, like `/v1/employees`.
