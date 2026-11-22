# E-Commerce Backend Application

A backend application developed using **Java and Spring Boot** to manage core e-commerce operations, including products, categories, users, shopping carts, and orders.

##  Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **ORM:** Spring Data JPA / Hibernate (if configured)
- **Database:** PostgreSQL
- **API Testing:** Postman
- **Build Tool:** Maven (if used)

##  Features

- **Product Management:** Create, retrieve, update, and delete products.
- **Category Management:** Organize and manage product categories.
- **User Management:** Manage user information.
- **Cart Management:** Handle shopping cart operations.
- **Order Management:** Manage customer orders and order items.
- **Database Integration:** Persist application data using PostgreSQL and Spring Data JPA.
- **Validation:** Validate incoming application data.
- **Exception Handling:** Handle application errors and invalid requests.

##  Project Architecture

The application follows a layered architecture:

- **Controller Layer:** Handles HTTP requests and API responses.
- **Service Layer:** Implements business logic.
- **Repository Layer:** Handles database operations using Spring Data JPA.
- **Entity Layer:** Defines database entities and relationships.

##  Database

PostgreSQL is used as the relational database.

Main entities include:

- Users
- Customers
- Customer Addresses
- Categories
- Products
- Cart Items
- Orders
- Order Items
- Reviews
- Wishlist Items

##  Getting Started

1. Clone the repository.
2. Configure your PostgreSQL database.
3. Update the database connection details in `application.properties`.
4. Run the application using your IDE or Maven.

##  API Testing

API endpoints can be tested using Postman.

## 🎯 Objective

To develop a structured and maintainable backend system for e-commerce operations using Java, Spring Boot, REST APIs, and PostgreSQL.
