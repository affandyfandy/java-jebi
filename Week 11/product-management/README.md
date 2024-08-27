# Product Management

This application allows users to manage products, including creating, editing, deleting, and listing products. It also includes features for searching, sorting, and paginating the product list.

## Overview

This Angular project is focused on product management. Below is the explanation of the components, services, routes, and lifecycle hooks within the project.

### 1. `src/app/config/app.config.ts`

This file defines a constant `RouterConfig` that holds route information for the product page.
- **Purpose**: Provides configuration for routing, specifically the path, link, and title of the product page.
- **Usage**: The `RouterConfig` is used in the main routing configuration to load the `ProductListComponent`.

### 2. `src/app/main/component/footer.component.ts`

This file defines the `FooterComponent` with a simple HTML and CSS structure.
- **Purpose**: This component renders the footer section of the application.
- **Lifecycle**: `FooterComponent` is a standalone component that gets included in the main application component.
- **Usage**: It’s imported and used in the `AppComponent`.

### 3. `src/app/component/header/header.component.ts`

Similar to the footer, this file defines the `HeaderComponent`.
- **Purpose**: Renders the header of the application, typically used for navigation or branding.
- **Lifecycle**: Standalone, used in the main application component.
- **Usage**: Imported into the `AppComponent`.

### 4. `src/app/component/main/app.component.ts`

This is the root component of the application.
- **Purpose**: The `AppComponent` is the root of the application, responsible for rendering the header, footer, and main content via `RouterOutlet`.
- **Lifecycle**:
  - `ngOnInit`: Not implemented, but it would be used for any initialization logic.
  - `ngAfterViewInit`: Could be used to handle actions after the view is fully initialized.
- **Usage**: The main template (`app.component.html`) pulls in the router outlet and the header and footer components to build the main layout of the app.

### 5. `src/app/models/product.model.ts`

This file defines the `Product` interface, which is used across the application to represent a product.
- **Purpose**: Serves as a model for the product data, ensuring consistent structure throughout the application.
- **Fields**:
  - `id`, `name`, `price`, `status`, `createdAt`, `updatedAt`.
- **Usage**: The `Product` model is used by services and components to manipulate product data.

### 6. `src/app/pages/product/product-form/product-form.component.ts`

This component handles the form for adding and editing products.
- **Purpose**: Provides a form for creating or updating a product. It emits events when the form is saved or canceled.
- **Lifecycle**:
  - `ngOnInit`: Initializes the form and pre-fills it if editing an existing product.
- **Key Methods**:
  - `onSubmit`: Validates the form and emits the product data.
  - `onClose`: Closes the form and emits a cancel event.
- **Usage**: Included in the `ProductListComponent` and displayed when adding or editing a product.

### 7. `src/app/pages/product/product-list/product-list.component.ts`

This component displays the list of products and allows for product management (add, edit, delete).
- **Purpose**: Manages the list of products, handling operations like sorting, filtering, and pagination.
- **Lifecycle**:
  - `ngOnInit`: Fetches the list of products from the service.
- **Key Methods**:
  - `getProductList`: Fetches and updates the list of products.
  - `onSave`: Handles the saving of a product, either updating an existing product or adding a new one.
  - `onCancel`: Closes the form modal.
  - `onSort`: Sorts the products by the selected column.
  - `toggleStatus`: Toggles the product status between 'ACTIVE' and 'INACTIVE'.
  - `onPageChange`: Updates the pagination.
  - `deleteProduct`: Deletes a product and updates the list.
- **Usage**: Handles most of the user interactions related to product management.

### 8. `src/app/pages/product/product.routes.ts`

This file defines the route configuration for the product module.
- **Purpose**: Maps the default route to the `ProductListComponent`.
- **Usage**: Loaded as a child route in the main `app.routes.ts` file.

### 9. `src/app/services/product.service.ts`

This service handles HTTP requests to the backend API related to products.
- **Purpose**: Provides CRUD operations for products.
- **Key Methods**:
  - `getProducts`: Fetches the list of products.
  - `addProduct`: Sends a POST request to add a new product.
  - `updateProduct`: Sends a PUT request to update an existing product.
  - `getProductById`: Fetches a product by its ID.
  - `deleteProduct`: Sends a DELETE request to remove a product.
- **Usage**: Injected into components to handle product data operations.

### 10. `src/app/app.config.ts`

This file configures the Angular application.
- **Purpose**: Provides the router and HTTP client services to the application.
- **Usage**: Used at the application level to set up providers for routing and HTTP requests.

### 11. `src/app/app.routes.ts`

Defines the main routing configuration for the application.
- **Purpose**: Handles the main routes for the application. It lazy loads the product module and includes a wildcard route that redirects to the root.
- **Usage**: Ensures that the `ProductListComponent` is loaded when the user navigates to the root path.


## Features

- **Add Product**: Allows adding a new product with details such as ID, name, price, and status.
- **Edit Product**: Enables editing the details of an existing product.
- **Delete Product**: Allows deletion of a product from the list.
- **Search and Filter**: Provides a search functionality for products by name.
- **Sorting**: Supports sorting of the product list by different columns such as name, price, etc.
- **Pagination**: Displays the product list in pages to enhance usability and performance.
#
## Setup and Installation

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Start the JSON Server**:
   Before running the application, ensure that the JSON server is running to simulate an API backend.

   Run the following command to start the JSON server:
   ```bash
   npx json-server --watch db.json
   ```

   This command watches the `db.json` file for changes and provides RESTful API endpoints for the data.

3. **Run the Angular Application**:
   ```bash
   ng serve
   ```

   Open the browser and navigate to `http://localhost:4200/` to access the application.
#
## Project Structure

The project follows a modular structure, where each feature or component is encapsulated within its own module. Key directories and files include:

- **`src/app/config/`**: Contains configuration files for routing and application constants.
- **`src/app/main/components/`**: Contains shared components like the header and footer.
- **`src/app/pages/product/`**: Contains product-related components, such as the product form and product list.
- **`src/app/services/`**: Contains services for handling business logic and API calls.
- **`src/app/models/`**: Contains data models (interfaces) representing the application's data structures.
#
## Components

### 1. ProductFormComponent

This component provides a form for adding or editing a product. It includes input fields for product ID, name, price, and status, supporting both the addition of new products and updating existing ones.

### 2. ProductListComponent

This component displays a list of products with features such as searching, sorting, pagination, and actions for editing or deleting a product.
#
## Screenshots

![Screenshot 1](img/image.png)
![Screenshot 2](img/image-1.png)
![Screenshot 3](img/image-2.png)
![Screenshot 4](img/image-3.png)
![Screenshot 5](img/image-4.png)
![Screenshot 6](img/image-5.png)
![Screenshot 7](img/image-6.png)
