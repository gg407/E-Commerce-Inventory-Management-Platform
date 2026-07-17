# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#io.validation)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)

### Running the application locally

```bash
./mvnw spring-boot:run
```

The application starts on port `8080` by default and expects a MySQL 8 instance reachable at the
URL configured in `application.properties` (see the project root `README.md` for full setup
instructions, Docker option, and seed accounts).

### Running the tests

```bash
./mvnw test
```
