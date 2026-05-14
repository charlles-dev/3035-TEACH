# TeachGram Pro Backend

Backend Spring Boot do desafio final.

## Stack

- Java 21.
- Spring Boot 3.5.x.
- Maven.
- Spring Security.
- Spring Data JPA.
- PostgreSQL.
- Redis.
- Flyway.
- Spring Modulith.
- OpenAPI/Swagger.
- Actuator.

## Como Rodar

```bash
cd backend
./mvnw spring-boot:run
```

No Windows, se o wrapper ainda nao tiver sido gerado:

```bash
mvn spring-boot:run
```

## URLs

- API: `http://localhost:8080/api/v1`
- Health: `http://localhost:8080/actuator/health`
- Swagger: `http://localhost:8080/swagger-ui/index.html`

## Ordem Recomendada

Siga [../docs/13-guia-implementacao.md](../docs/13-guia-implementacao.md).

