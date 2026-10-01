#  Order Management System

A professional e-commerce order management REST API built with **Spring Boot 3** and **PostgreSQL**. Features advanced business logic including dynamic discounts, inventory management, state-based order workflows, and comprehensive validation.

---

##  Key Features

### **Business Logic**
- **Dynamic Discount System**: Multi-tier discounts based on customer type (STANDARD/PREMIUM) and order total
- **Inventory Management**: Real-time stock tracking with automatic adjustments on order creation/cancellation
- **Order State Machine**: Enforced status transitions (PENDING ? CONFIRMED ? SHIPPED ? DELIVERED)
- **Soft Delete**: Maintain data integrity with logical deletion for customers and products
- **Order Cancellation**: Automatic stock restoration with business rule validation

### **Technical Highlights**
- **JPA Specifications**: Type-safe, dynamic query building for complex filtering
- **Pagination**: Efficient data retrieval with customizable page size, sorting, and filtering
- **Custom Exception Handling**: Centralized error responses with detailed validation feedback
- **MapStruct Integration**: Type-safe DTO ? Entity mapping

---

##  Data Model
```
Customer (1) --< (N) Order (1) --< (N) OrderItem (N) >-- (1) Product
```

### **Entities**
- **Customer**: Customer details with type-based discount rates
- **Product**: Product catalog with category, pricing, and stock tracking
- **Order**: Order header with status, amounts, and shipping info
- **OrderItem**: Order line items with quantity, unit price, and subtotals

### **Enums**
- **CustomerType**: STANDARD (0% discount), PREMIUM (10% discount)
- **OrderStatus**: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
- **ProductCategory**: ELECTRONICS, CLOTHING, FOOD, BOOKS, HOME, TOYS

---

##  Business Rules

### **Order Creation**
1. Validate customer is active
2. Check product availability and stock levels
3. Calculate subtotals (quantity × unit price)
4. Apply customer type discount (0-10%)
5. Apply bulk order discount (5% for orders > 500 TL)
6. Deduct inventory from products
7. Generate unique order number (ORD-YYYYMMDD-####)

### **Order Cancellation**
- Only PENDING orders can be cancelled via DELETE endpoint
- Automatically restores product stock quantities
- CONFIRMED/SHIPPED orders require status transition via PATCH

### **Status Transitions**
```
PENDING ? CONFIRMED ?
PENDING ? CANCELLED ?
CONFIRMED ? SHIPPED ?
CONFIRMED ? CANCELLED ? (Use status endpoint)
SHIPPED ? DELIVERED ?
DELIVERED ? * ? (Final state)
```

---

##  Tech Stack

- **Framework**: Spring Boot 3.2.1
- **Language**: Java 21
- **Database**: PostgreSQL 15 (Docker)
- **ORM**: Spring Data JPA + Hibernate
- **Validation**: Jakarta Bean Validation
- **Mapping**: MapStruct 1.6.3
- **Build Tool**: Maven
- **Containerization**: Docker Compose

---

##  Getting Started

### **Prerequisites**
- Java 21+
- Docker & Docker Compose
- Maven 3.8+

### **Run Application**
```bash
# Start PostgreSQL
docker-compose up -d

# Run application
mvn spring-boot:run
```

Application runs on `http://localhost:8080`

---

##  API Endpoints

### **Customers**
```http
POST   /api/customers                    # Create customer
GET    /api/customers/{id}               # Get customer
GET    /api/customers                    # List customers (paginated, filterable)
PUT    /api/customers/{id}               # Update customer
DELETE /api/customers/{id}               # Soft delete customer
```

**Filters**: `email`, `firstName`, `lastName`, `customerType`, `isActive`, `city`, `country`

### **Products**
```http
POST   /api/products                     # Create product
GET    /api/products/{id}                # Get product
GET    /api/products                     # List products (paginated, filterable)
PUT    /api/products/{id}                # Update product
PATCH  /api/products/{id}/stock          # Update stock quantity
DELETE /api/products/{id}                # Soft delete product
```

**Filters**: `name`, `sku`, `minPrice`, `maxPrice`, `inStock`, `category`, `isActive`

### **Orders**
```http
POST   /api/orders                       # Create order
GET    /api/orders/{id}                  # Get order
GET    /api/orders                       # List orders (paginated, filterable)
PATCH  /api/orders/{id}/status           # Update order status
DELETE /api/orders/{id}                  # Cancel order (PENDING only)
```

**Filters**: `orderNumber`, `status`, `totalAmountMin/Max`, `finalAmountMin/Max`, `orderDateFrom/To`

---

##  Advanced Features

### **Dynamic Filtering with Specifications**
```java
// JPA Specification Pattern
GET /api/customers?customerType=PREMIUM&city=Istanbul&isActive=true&page=0&size=20&sortBy=firstName&direction=ASC
```

All filters are optional and can be combined. Default: returns only active records.

### **Pagination Response**
```json
{
  "content": [...],
  "totalElements": 150,
  "totalPages": 8,
  "pageSize": 20,
  "pageNumber": 0,
  "isFirst": true,
  "isLast": false
}
```

### **Validation Error Response**
```json
{
  "timestamp": "2024-01-25T15:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Order validation failed",
  "details": {
    "errorCount": 2,
    "items": [
      {
        "productId": 5,
        "productName": "iPhone 15",
        "errorCode": "INSUFFICIENT_STOCK",
        "requestedQuantity": 10,
        "availableQuantity": 3
      }
    ]
  }
}
```

---

##  Project Structure
```
src/main/java/com/meghana/ordermanagementsystem/
+-- common/
¦   +-- dto/                    # Shared DTOs (PaginationResponse)
¦   +-- exception/              # Global exception handling
¦   +-- mapper/                 # Base mapper interface
+-- customer/
¦   +-- controller/             # REST endpoints
¦   +-- dto/                    # Request/Response DTOs
¦   +-- entity/                 # JPA entity
¦   +-- enums/                  # CustomerType enum
¦   +-- exception/              # Domain-specific exceptions
¦   +-- mapper/                 # MapStruct mappers
¦   +-- repository/             # JPA repository
¦   +-- service/                # Business logic
¦   +-- specification/          # JPA Criteria queries
+-- product/                    # Similar structure
+-- order/                      # Similar structure + utils
```

**Design Principles**:
- Clean Architecture with clear separation of concerns
- Package-by-feature for better modularity
- DTOs for API contracts, Entities for persistence
- Specification pattern for complex queries
- Centralized exception handling

---

##  Design Decisions

### **Soft Delete Strategy**
- Customers and Products use `isActive` flag
- Orders use status-based lifecycle (CANCELLED state)
- Prevents foreign key violations and maintains audit trail

### **DTO Pattern**
- Separate Request/Response DTOs prevent over-posting
- MapStruct for type-safe, performant mapping
- Request DTOs include validation annotations
- Response DTOs exclude sensitive/internal fields

### **Enum-Based Business Logic**
- CustomerType encapsulates discount calculation
- OrderStatus enforces valid state transitions
- ProductCategory provides display names and descriptions

---

##  Testing
```bash
# Run tests
mvn test

# Run with coverage
mvn clean verify
```

---

##  Configuration

**Database**: `src/main/resources/application.yml`
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/order-management-system
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
```

**Docker**: `docker-compose.yml`
```yaml
services:
  postgres:
    image: postgres:15-alpine
    ports:
      - "5432:5432"
```

---

##  Exception Handling

Centralized error handling with consistent response format:

- **404 Not Found**: Resource doesn't exist
- **400 Bad Request**: Validation errors, business rule violations
- **409 Conflict**: Duplicate email/SKU, invalid state transitions

---

##  Learning Highlights

This project demonstrates:
-  RESTful API design principles
-  Complex business logic implementation
-  Advanced JPA features (Specifications, Cascades, Fetch Strategies)
-  Proper error handling and validation
-  Clean code architecture
-  Real-world e-commerce workflows

---

##  License

This project is for educational purposes.

---

##  Author

**Meghana mahaveer**
- GitHub: [Meghan-05](https://github.com/Meghan-05)
