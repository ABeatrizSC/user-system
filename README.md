# Project User System
## About
This repository contains Challenge 3 of the Compass Uol scholarship program SPRINGBOOT_AWS_AGO/24. The project consists of creating an API that allows user registration and password updates using Spring Boot. The API includes security with JWT and a messaging system for communication with another microservice using RabbitMQ. The API utilizes OpenFeign to connect with the ViaCEP API, enabling seamless retrieval of postal code (CEP) information.

## Project sctructure - Microservices
- `users-management-ms`: microservice for managing a user's password, such as: creating, authenticating and updating a password (saving in a MySQL database).
- `notify-ms`: microservice responsible for receiving messages from `user-management-ms` and saving them in a mongoDB database.
- The communication between microservices is managed by RabbitMQ, which uses two specific queues to route events. When a user creation action is performed, a message is sent to the notify.user-events.created queue. For user update actions, the message is routed to the notify.user-events.updated queue.

## Features
- Creation of a user
- Authentication of an existing user 
- Updating the user's password, where the Bearer Token returned in the authentication process is required 
- Every time a CREATE or UPDATE action on users is performed, the Notify microservice receives a message containing the `username` and the `operation` performed, and saves it in a MongoDB database.

## Technologies Used
* **Java**: A high-level, object-oriented programming language widely used for building server-side applications, web services, and Android applications.

* **Spring Boot**: A framework that simplifies the development of Java applications by providing built-in features for dependency injection, configuration, and microservices support.

* **Spring Security**: A powerful and customizable authentication and access control framework for Java applications.

* **JWT (JSON Web Token)**: A compact, URL-safe means of representing claims to be transferred between two parties. In this application, JWT is used for securely transmitting information about the user’s identity and for maintaining stateless authentication in a microservices architecture.

* **RabbitMQ:** An open-source message broker that facilitates communication between microservices through messaging.

* **MySQL**: A popular open-source relational database management system used for storing and managing data in web applications.

* **MongoDB**: A NoSQL database that stores data in flexible, JSON-like documents, allowing for a more dynamic data structure and easier scalability compared to traditional relational databases.

* **Docker**: A platform that allows developers to automate the deployment of applications inside lightweight containers, ensuring consistency across different environments and simplifying the setup process.

* **ViaCep**: A free API for retrieving address information in Brazil based on postal codes (CEP).

* **OpenFeign**: A declarative web service client for Java that simplifies HTTP requests to RESTful APIs. It allows developers to define API endpoints using annotations, resulting in cleaner code and seamless integration with Spring Cloud for microservices communication.

* **Jakarta Bean Validation**: A Java framework that provides a standardized way to handle validation of object properties, fields, and method parameters.

* **Lombok**: A Java library that reduces boilerplate code by generating common methods like getters, setters, constructors, and more through annotations.

* **MapStruct**: A code generator that simplifies the mapping of Java bean properties, making it easier to convert between different object models and reducing the amount of manual mapping code required.

* **Jackson Databind**: A powerful data-binding library for Java that allows converting Java objects to JSON and vice versa.

* **JPA**: The Java Persistence API, a specification that provides object-relational mapping (ORM) to manage relational data in Java applications.

* **Postman**: A tool used for API testing and development, enabling users to send HTTP requests, inspect responses, and automate API tests.

**IDE: IntelliJ IDEA Community Edition 2024.2**

## Requirements
To use the project on your machine, the following tools must be installed and configured beforehand:

- Java Development Kit (JDK) 17
- Apache Maven
- Docker
- Git
- Postman (optional)

## Installation guide
Follow the steps below to download, configure, and run the project in your environment:
1. **Clone the repository**
```bash
git clone https://github.com/ABeatrizSC/user-system.git
 ```

2. **Navigate to the project directory**

```bash
cd user-system
 ```

3. **Install dependencies and create the .jar**

 ```bash
cd user-management-ms
mvn clean install
mvn clean package -DskipTests

cd .. #Returns to the root folder

cd notify-ms
mvn clean install
mvn clean package -DskipTests
 ```

4. **Initialize the docker container**

 ```bash
cd .. #Returns to the root folder
docker-compose up --build
 ```

## Endpoints - User management microservice

### Resume
| Method | Endpoint                     | Description                                      |
|--------|------------------------------|--------------------------------------------------|
| POST   | `/api/auth`                  | Authenticates a user and returns a JWT token     |
| POST   | `/api/users/register`        | Registers a new user                             |
| PUT    | `/api/users/update-password` | Updates a user's password (you need to be authenticated)

### User Registration
* Endpoint: /api/users/register
* HTTP method: POST

#### Request Body
```json
{
  "username": "username",
  "password": "password",
  "email": "username@email.com",
  "cep": "01001-000"
}
 ```

#### Response Body (Success) 
```json
{
  "username": "username",
  "email": "username@email.com",
  "address": {
    "zipCode": "01001-000",
    "street": "Praça da Sé",
    "complement": "lado ímpar",
    "neighborhood": "Sé",
    "city": "São Paulo",
    "state": "SP"
  }
} 
```

#### Possible responses to errors and exceptions
```json
{
  "error": "Username or email provided has already been registered.",
  "timestamp": "2024-11-03T18:27:19.834574107",
  "status": 409
}
 ```
```json
{
  "error": "Invalid fields. Make sure that no field is null and the zip code and email are entered correctly.",
  "timestamp": "2024-11-03T19:57:30.622304775",
  "status": 400
}
 ```

### User Authentication
* Endpoint: /api/auth
* HTTP method: POST

#### Request Body
```json
{
  "username": "username",
  "password": "password"
}
 ```

#### Response Body (Success)
```json
{
  "token": "your Bearer Token appears here"
}
 ```

#### Possible responses to errors and exceptions
```json
{
  "message": "User 'aleatoryUsername' not found.",
  "timestamp": "2024-11-03T20:36:08.639635999",
  "status": 404
}
 ```

```json
{
  "error": "Invalid fields. Make sure that no field is null and the zip code and email are entered correctly.",
  "timestamp": "2024-11-03T19:57:30.622304775",
  "status": 400
}
 ```

`401 Unauthorized`: Appears when the password is incorretly.

### User Password Update
* Endpoint: /api/users/update-password
* HTTP method: PUT
* To be able to change your password, you must be authenticated

#### Request Body
```json
{
  "username": "username",
  "oldPassword": "password",
  "newPassword": "newpassword"
}
 ```

#### Response Body (Success)
`204 - No Content`

#### Possible responses to errors and exceptions
```json
{
  "error": "The new password can't be the same as the old password.",
  "timestamp": "2024-11-03T20:18:38.911360321",
  "status": 400
}
 ```

```json
{
  "error": "Current password is incorrect.",
  "timestamp": "2024-11-03T20:09:15.578563232",
  "status": 401
}
 ```

```json
//Occurs when a user is authenticated but tries to change another user's password
{
  "error": "Unauthorized action.",
  "timestamp": "2024-11-03T20:09:50.077105897",
  "status": 401
}
 ```

```json
{
  "error": "Invalid or expired token.",
  "timestamp": "2024-11-05T12:17:18.402299361",
  "status": 401
}
 ```

```json
{
  "message": "User 'aleatoryUsername' not found.",
  "timestamp": "2024-11-03T20:36:08.639635999",
  "status": 404
}
 ```

```json
{
  "error": "Invalid fields. Make sure that no field is null and the zip code and email are entered correctly.",
  "timestamp": "2024-11-03T19:57:30.622304775",
  "status": 400
}
 ```

## Contact
* GitHub: [ABeatrizSC](https://github.com/ABeatrizSC)
* Linkedin: [Ana Beatriz Santucci Carmoni](www.linkedin.com/in/ana-carmoni)
* Email: [anabeatrizscarmoni@gmail.com](mailto:anabeatrizscarmoni@gmail.com)
