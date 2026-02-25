


# 💼 Recruitment Management System – RESTful API

A role-based recruitment management system built with Spring Boot following RESTful architecture principles.



## 🚀 Features

- JWT-based stateless authentication
- Role & permission management (Admin, HR, User)
- Company management
- Job posting management
- Resume submission
- File upload/download for CV handling
- Skill-based email notification system
- Pagination, sorting, validation
- Global exception handling


## 🛠 Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT
- JPA/Hibernate
- MySQL
- Gradle



## 🏗 Architecture

- RESTful API design
- Layered architecture (Controller – Service – Repository)
- DTO pattern for request/response handling
- Role-based authorization



## 🔐 Authentication Flow

1. User login  
2. Server validates credentials  
3. JWT token is generated  
4. Client sends token in Authorization header  
5. Security filter validates token for each request  



