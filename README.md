# Book Management System

This project is a book management system consisting of two microservices: `book-storage-service` and `book-tracker-service`. Both services are written in Java and use Maven for dependency management and build automation. Additionally, the project uses Keycloak for authentication and authorization, which is run using Docker.

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Running the Application](#running-the-application)
  - [Running book-storage-service](#running-book-storage-service)
  - [Running book-tracker-service](#running-book-tracker-service)
  - [Running Keycloak](#running-keycloak)
- [Testing](#testing)
- [Making requests](#Making-requests)
- [Swagger](#Swagger-documentation)
## Requirements

To run this project, you will need:

- Java Development Kit (JDK) version 17 or higher
- Apache Maven version 3.9+
- Docker
- MySQL
- Postman (for testing)

## Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/gerushenka/Library-API.git
    ```

2. Install dependencies for both services:

    ```sh
    mvn clean install
    ```

## Running the Application

To run the application, you need to start two services: `book-storage-service` and `book-tracker-service`. Additionally, you need to set up Keycloak and MySQL.

### Running book-storage-service

1. Navigate to the `book-storage-service` directory:

    ```sh
    cd book-storage-service
    ```

2. Run the service:

    ```sh
    java -jar target/book-storage-service-0.0.1-SNAPSHOT.jar
    ```

The service will be available at `http://localhost:8081`.

### Running book-tracker-service

1. Navigate to the `book-tracker-service` directory:

    ```sh
    cd book-tracker-service
    ```

2. Run the service:

    ```sh
    java -jar target/book-tracker-service-0.0.1-SNAPSHOT.jar
    ```

The service will be available at `http://localhost:8082`.

### Running Keycloak

1. Navigate to the root directory of the project:

    ```sh
    cd Library-API
    ```

2. Run Keycloak using Docker:

    ```sh
    docker-compose up
    ```

Keycloak will be available at `http://localhost:8080`. You can log in using the username `admin` and the password `admin`. After logging in, click on the "Create Realm" button and import the `realm-config.json` file located in the root directory of the project.

## Testing

To check tests, run Docker and MySQL, use the following command in the root directory of the project:

```sh
mvn test  
```

## Making requests

To make requests to the services, you need to obtain a Bearer token for authentication. Follow  these steps:
1. Open Postman.
2. Import the Postman collection provided in the project.
3. Execute the request to obtain a token for either a manager or a user.
4. Copy the access_token from the response.
5. Add the token after the word Bearer, in the field Authorization in headers.

## Swagger documentation
All endpoints can be seen in the swagger documentation

![img](readme-img/storage-service-swagger.png)
![img](readme-img/tracker-service-swagger.png)
