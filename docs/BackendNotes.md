# Backend notes

Version 0.1 not finished

This is a document outlining some of the technologies the backend uses. This will also outline the features the backend should include.

## Dependencies

There are several dependencies that the backend will utilize to implement the required features.

### [Swagger](swagger.io) through [SpringDoc](springdoc.org)

Swagger is a standardized way to document server app endpoints. SpringDoc is a way to automatically generate swagger docs trough code annotation.

### [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

A tool used to implement JPA repositories.

### mysql-connector-j

Establishes a connection between JPA and a MySQL database

### Spring Boot

Application to create a server to respond to http requests to certain endpoints

### spring dotenv

Get data from environment variables

## Functionality

### Pagination

For endpoints where a large amount of data is requested. There should be an option to request the data in a pagination to increase performance.

### Save data persistently trough a database

### Ability to automatically mark a desk as free trough calendar

If possible the application should have the ability to mark a desk as free if the user is on holiday or is working away.

### Ability of users to reserve a desk

## Other technologies

### MySQL/postgresSQL

Database provider yet to be decided
