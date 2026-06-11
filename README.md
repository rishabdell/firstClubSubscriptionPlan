# Membership Plan API

A production-ready Spring Boot application that demonstrates a complete Membership Management System with support for subscription plans, membership tiers, upgrades, downgrades, cancellations, and automated tier evaluation using the Strategy Design Pattern.

## 🚀 Features

### Membership Plans

* Monthly
* Quarterly
* Yearly

### Membership Tiers

* Silver
* Gold
* Platinum

### Subscription Management

* Create Subscription
* Upgrade Membership Tier
* Downgrade Membership Tier
* Cancel Subscription
* Retrieve Current Membership Details

### Tier Evaluation

The application uses the **Strategy Pattern** to determine membership tier eligibility based on customer activity and spending behavior.

## 🛠 Technology Stack

* Java 21
* Spring Boot 4
* Spring Data JPA
* H2 In-Memory Database
* Maven
* OpenAPI / Swagger
* RESTful APIs
* Strategy Design Pattern

## 🏗 Architecture

```text
                     +------------------+
                     | Membership API   |
                     +--------+---------+
                              |
        ------------------------------------------
        |                |            |          |
        v                v            v          v

+---------------+ +--------------+ +------------+ +-------------+
| Plan Service  | | Tier Service | | BenefitSvc | | Subscription|
+---------------+ +--------------+ +------------+ +-------------+
                                                |
                                                |
                                                v

                                    +--------------------+
                                    | Tier EvaluationSvc |
                                    +---------+----------+
                                              |
                                              v

                                    +--------------------+
                                    | Order Statistics   |
                                    +--------------------+
```

## 📂 Project Structure

```text
src/main/java
├── controller
├── service
├── strategy
├── repository
├── entity
├── dto
├── exception
└── config
```

## ⚙️ Getting Started

### Prerequisites

* Java 21+
* Maven 3.9+

### Clone Repository

```bash
git clone https://github.com/<your-username>/membership-plan.git
cd membership-plan
```

### Build Application

#### Windows (PowerShell)

```bash
.\mvnw.cmd clean package
```

#### Linux / Mac

```bash
./mvnw clean package
```

### Run Application

```bash
java -jar target/membership-plan.jar
```

Alternatively:

```bash
mvn spring-boot:run
```

## 📖 API Documentation

Swagger UI is available at:

```text
http://localhost:8080/
```

## 🗄 Database

The application uses an H2 in-memory database.

H2 Console:

```text
http://localhost:8080/h2-console
```

Default Configuration:

```text
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password:
```

## 📡 REST APIs

### Get Available Plans

```http
GET /plans
```

### Get Available Membership Tiers

```http
GET /tiers
```

### Create Subscription

```http
POST /subscriptions
```

Request Body

```json
{
  "userId": 104,
  "planInterval": "MONTHLY",
  "tierName": "GOLD"
}
```

### Upgrade Membership Tier

```http
PUT /subscriptions/{userId}/upgrade
```

Example:

```http
PUT /subscriptions/101/upgrade
```

### Downgrade Membership Tier

```http
PUT /subscriptions/{userId}/downgrade
```

Example:

```http
PUT /subscriptions/101/downgrade
```

### Track User Activity

```http
PUT /subscriptions/{userId}/tracking
```

Example:

```http
PUT /subscriptions/101/tracking
```

Request Body

```json
{
  "monthStartDate": "2026-06-01",
  "orderCount": 25,
  "totalSpend": 5400.75
}
```

### Cancel Subscription

```http
DELETE /subscriptions/{userId}
```

### Get Current Membership

```http
GET /subscriptions/{userId}
```

## 🎯 Design Patterns Used

### Strategy Pattern

The tier evaluation logic is implemented using the Strategy Pattern.

Benefits:

* Open for extension, closed for modification
* Easy to add new membership tiers
* Clean separation of business rules
* Improved maintainability and testability

## ✅ Production Readiness Considerations

* Layered Architecture
* DTO-based API Contracts
* Exception Handling
* Validation Support
* Separation of Concerns
* Extensible Tier Evaluation Logic
* RESTful API Design
* OpenAPI Documentation
* JPA Repository Abstraction

## 👨‍💻 Author

Rishab Kumar Singh

Backend Engineer | Java | Spring Boot | Microservices | Cloud
