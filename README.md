<!-- README.md - Fortune API Backend -->

<h1 align="center">Fortune API</h1>
<p align="center">
  <a href="#">
    <img alt="Fortune" title="Fortune" src="https://i.imgur.com/GizLdUN.png" width="200">
  </a>

<p>Api used on Fortune-APP</p>
</p>
<p align="center">
  <img alt="Download on GitHub" title="GitHub" src="https://i.imgur.com/bVQ9UBG.png" width="200">
</p>

## Overview

Fortune API is a secure, scalable backend built with **Spring Boot 3.4** that supports all banking operations for the Fortune mobile app. It provides REST endpoints for user authentication, account and card management, transaction tracking, and peer-to-peer payments through a Bizum-like system. 

The API ensures data protection through JWT-based stateless authentication and encrypted password handling.

## Technology Stack

- **Language:** Java 22  
- **Framework:** Spring Boot 3.4  
- **Security:** Spring Security 6.4 with JWT (JJWT 0.11.5)  
- **Database:** MySQL with Spring Data JPA and Hibernate  
- **Build System:** Maven  
- **Containerization:** Docker using OpenJDK 22  
- **Utilities:** Lombok, BCrypt for password hashing, JSON processing  

## Core Components

| Module              | Responsibilities                       | Controllers                |
|---------------------|--------------------------------------|----------------------------|
| Authentication      | User login, registration, JWT tokens | AuthController             |
| User Management     | User profiles, settings               | UserController             |
| Banking Operations  | Accounts, cards, transfers            | AccountController, CardController |
| Bizum Payments      | Peer-to-peer payments & requests     | BizumController            |
| Transaction History | Track movements and payment status   | MovementController         |

---

## Security Features

- **JWT Authentication:** Secure, stateless token-based access control  
- **Password Encryption:** BCrypt hashing for user passwords and PINs  
- **Token Validation:** Request filtering to verify JWT on every API call  
- **Configurable Secrets:** JWT secret and DB credentials managed through environment variables

---

## Configuration & Deployment

The API is designed for flexible deployment with externalized configuration:

- Uses environment variables for sensitive settings like `DB_URL`, `JWT_SECRET`, `DB_USERNAME`, and `DB_PASSWORD`  
- Supports automatic database schema updates through Hibernate's `ddl-auto=update`  
- Dockerized container runs the packaged `fortune-api-0.0.1-SNAPSHOT.jar` on OpenJDK 22 runtime  

---

## Request Flow Overview

1. Client sends HTTP request with JWT token  
2. TokenAuthenticationFilter validates JWT and sets authentication context  
3. Controller processes request and calls service layer  
4. Service layer executes business logic and accesses database via repositories  
5. Response is returned as JSON to client  

---

## Related Projects

- Frontend Android App: [Fortune-app-frontend](https://github.com/S4nchzz/Fortune-app-frontend)

---

## Acknowledgments

- 💙 [Spring Boot](https://spring.io/projects/spring-boot) for robust backend framework  
- 🐘 [MySQL](https://www.mysql.com/) for reliable relational data storage  
- 🐳 [Docker](https://www.docker.com/) for easy containerized deployment  
- 🔐 [JWT (Json Web Token)](https://jwt.io) for secure token management  

---
