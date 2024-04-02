# <h1 align="center">Bookstore Inventory Management System</h1>

### Introduction
It is a SpringBoot application to manage a bookstore's inventory.
This backend system support operations to add, update, delete, and retrieve books
The focus is on using gRPC with Protobuf for communication.

### Technologies Used
`The following technologies are used to build the BIMS Application:`
- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Swagger**
- **PostgresSQL**
- **Lombok**
- **MapStruct**
- **gRPC**

### How to use
`Before running Online Book Store, ensure you have the following installed:`
- ☕ Java
- Protocol Buffer Compiler
- PostgresSQL

`Follow the steps below to install:`
1. Clone the repository from GitHub and navigate to the project directory.
2. Create a `.env` file with the necessary environment variables. (See `.env-sample` for a sample.)
3. Run 2 spring boot services client-service, grpc-service.
4. The application should now be running at `http://localhost:8080`. 