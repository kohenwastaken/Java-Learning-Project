# 🏦 Java Banking Application

A service-based banking application developed with **Java 25** and **Spring Boot**.

The project was built to practice backend development concepts such as REST APIs, Hexagonal Architecture, PostgreSQL persistence, Spring Data JPA, transaction management, Docker, and unit testing.

> This project does not include a graphical frontend. The REST API is tested using Postman.

---

## ✨ Features

- Create a customer and account
- View account balance
- Deposit money
- Withdraw money
- Transfer money between accounts
- List account transactions

---

## 🧰 Technologies

- Java 25
- Spring Boot
- Maven
- PostgreSQL
- Spring Data JPA
- Docker
- Docker Compose
- JUnit
- Postman
- Git / GitHub

---

## 🧱 Architecture

The project follows a **Hexagonal Architecture** approach.

The main goal is to keep the business logic independent from external technologies such as REST, PostgreSQL, and Spring Data JPA.

```text
                REST API
                   |
                   v
          +------------------+
          | Inbound Adapter  |
          | REST Controller  |
          +------------------+
                   |
                   v
          +------------------+
          |   Application    |
          |   BankService    |
          +------------------+
                   |
                   v
          +------------------+
          |      Domain      |
          | Account          |
          | Customer         |
          | Transaction      |
          +------------------+
                   |
                   v
          +------------------+
          | Outbound Ports   |
          | Repositories     |
          +------------------+
                   |
                   v
          +------------------+
          | Outbound Adapter |
          | PostgreSQL / JPA |
          +------------------+
                   |
                   v
              PostgreSQL
```

Example persistence flow:

```text
BankService
    |
    v
CustomerRepository
    |
    v
PostgreSqlCustomerRepositoryAdapter
    |
    v
CustomerSpringDataRepository
    |
    v
PostgreSQL
```

---

## 📁 Project Structure

```text
src/main/java/org.example.bank
├── adapter
│   ├── in
│   │   └── rest
│   │       ├── controller
│   │       └── dto
│   └── out
│       ├── memory
│       └── persistence
│           ├── adapter
│           ├── entity
│           └── repository
├── application
│   ├── port
│   │   └── out
│   └── service
├── configuration
├── domain
│   ├── model
│   └── result
└── BankApplication.java
```

### Domain

Contains the core banking models and business rules.

Examples:

- `Account`
- `Customer`
- `Transaction`

The domain layer does not directly depend on PostgreSQL, JPA, REST, or Docker.

### Application

Contains application use cases.

The main service is `BankService`.

It coordinates:

- customer registration
- deposits
- withdrawals
- transfers
- transaction history

### Ports

Repository interfaces are defined under `application/port/out`.

Examples:

- `AccountRepository`
- `CustomerRepository`
- `TransactionRepository`

These interfaces define what the application needs from the persistence layer.

### Inbound Adapters

REST controllers receive HTTP requests and call the application layer.

DTOs are used to transfer request and response data between the REST API and the application.

### Outbound Adapters

The project contains both in-memory and PostgreSQL repository implementations.

PostgreSQL adapters use Spring Data JPA repositories internally.

---

## 🔌 REST API

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/customers` | Creates a new customer and account |
| GET | `/api/accounts/{accountId}/balance` | Returns account balance |
| POST | `/api/accounts/{accountId}/deposits` | Deposits money |
| POST | `/api/accounts/{accountId}/withdrawals` | Withdraws money |
| POST | `/api/accounts/{accountId}/transfers` | Transfers money to another account |
| GET | `/api/accounts/{accountId}/transactions` | Lists account transactions |

The API can be tested using Postman.

---

## 💾 Persistence

PostgreSQL is used as the persistent database.

Spring Data JPA is used to communicate with PostgreSQL.

The domain models are kept separate from persistence-specific entities.

For example:

- `Customer` → domain model
- `CustomerJpaEntity` → persistence model

This keeps persistence concerns outside the domain layer.

---

## 🔄 Transaction Management

Banking write operations use Spring transaction management with `@Transactional`.

This is especially important for transfers because a transfer affects multiple pieces of data:

- sender account
- receiver account
- transaction record

If an unexpected error occurs, the database transaction can be rolled back to prevent a partially completed transfer.

---

## 🐳 Docker

The Spring Boot application and PostgreSQL database run in separate containers and are managed together with Docker Compose.

```text
Postman
   |
   v
localhost:8080
   |
   v
bank-app container
   |
   v
Spring Boot / Java 25
   |
   v
Docker Network
   |
   v
postgres container
   |
   v
Docker Volume
```

---

## 🚀 Running the Project with Docker

### 1. Clone the repository

```bash
git clone https://github.com/kohenwastaken/Java-Learning-Project
cd <PROJECT_DIRECTORY>
```

### 2. Create the environment file

```bash
cp .env.example .env
```

Then replace the example value in `.env` with your own database password.

```env
BANK_DB_PASSWORD=your_secure_password
```

The password is used for both the PostgreSQL `bank_user` and the Spring Boot database connection.

🔐 **Do not commit your real `.env` file to Git.**

### 3. Start the application

```bash
docker compose up -d --build
```

### 4. Check the containers

```bash
docker compose ps
```

The `bank-app` and `postgres` services should be running.

### 5. Stop the application

```bash
docker compose down
```

Database data is stored in a Docker volume and is preserved when the containers are stopped or recreated.

To remove the containers and the database volume:

```bash
docker compose down -v
```

⚠️ This deletes the PostgreSQL data stored by the project.

---

## 🗄️ Database Access

```bash
docker compose exec postgres psql -U bank_user -d bank_db
```

Example queries:

```sql
SELECT * FROM customers;
SELECT * FROM accounts;
SELECT * FROM transactions ORDER BY transaction_id;
```

---

## 🧪 Testing

Run unit tests with Maven:

```bash
mvn clean test
```

The REST API was also manually smoke-tested using Postman.

Tested operations include:

- customer creation
- deposit
- withdrawal
- transfer
- transaction history
- persistence after container recreation

---

## 🔐 Environment Variables

The project currently requires:

```text
BANK_DB_PASSWORD
```

A template is provided in `.env.example`.

The real `.env` file is excluded from Git.

---

## 📚 Key Concepts Practiced

- Object-Oriented Programming
- Interfaces
- Dependency Injection
- Dependency Inversion
- Hexagonal Architecture
- Repository Pattern
- REST API development
- DTOs
- Spring Data JPA
- PostgreSQL
- Transaction management
- Docker
- Docker Compose
- Environment variables
- Unit testing
- Git branching and merging

---

## 🛠️ Possible Future Improvements

- Password hashing
- Authentication and authorization
- Global exception handling
- Integration tests
- Database migrations with Flyway or Liquibase
- Separate customer and account identifiers
- Multiple accounts per customer
- Web or mobile frontend

---

## 👤 Author

**Nezir Karakaşlı**

---

## ✅ Project Status

The main project requirements have been completed.

The application can be built, tested, and run as a Spring Boot + PostgreSQL environment using Docker Compose.
