# Final Engineering Summary

## Current summary

The audit-log service implements canonical SHA-256 hash chaining, persistence, append/query/verification APIs, environment profiles, JWT authentication foundations, retention, redaction metadata, export, and compliance reporting.

The implementation has been validated locally with Java 21 and Apache Maven 3.9.11. The packaged application starts successfully with the local profile and responds successfully through its API documentation endpoint.

AI-assisted work and human decisions are recorded in `ai-usage/AI_USAGE_LOG.md`.

## Implemented artifacts

- Spring Boot audit-log service with layered domain, facade, web, persistence, mapping, and security components.
- Canonical JSON and SHA-256 hash-chain processing with tamper detection.
- H2 local profile and PostgreSQL production profile.
- JWT-based authentication foundations and protected audit APIs.
- Retention, redaction, export, and compliance-reporting services.
- OpenAPI/Swagger documentation support.

## Architecture and key decisions

- Domain and service logic are kept separate from web and persistence concerns.
- Canonical serialization is used to make hash results deterministic.
- Local development uses file-backed H2; production configuration uses PostgreSQL through environment variables.
- The test profile uses an isolated in-memory H2 database.

## Testing and validation evidence

`mvn clean verify` completed successfully:

- 22 tests executed.
- 0 failures, 0 errors, and 0 skipped tests.
- Java version enforcement passed.
- JaCoCo execution data and HTML coverage report generated at `target/site/jacoco/index.html`.
- Spotless formatting check passed for all Java files.
- Application JAR was built and repackaged successfully.
- The packaged application started successfully with Java 21 and returned HTTP 200 for `/v3/api-docs` during the final startup check.

## Risks, trade-offs, assumptions, and limitations

- The local profile uses H2 and is intended for development and demonstration; production deployment requires PostgreSQL environment configuration.
- The build emits a non-blocking H2 dialect deprecation warning.
- Mockito emits a JDK warning about dynamic agent attachment; it does not currently fail the build.
- Production secrets and infrastructure credentials must be supplied externally and are not committed.

## Security review

- Authentication is enabled for protected API operations.
- JWT handling and password-based local authentication are covered by tests.
- Production database credentials are externalized through environment variables.
- Dependency and formatting checks are configured in the Maven lifecycle.

## AI-assisted development traceability

AI-assisted work and human decisions are recorded in `ai-usage/AI_USAGE_LOG.md`.

## Setup and demonstration instructions

Use Java 21 and Maven 3.9.11, then run:

```powershell
mvn clean verify
```

For local application startup:

```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Swagger/OpenAPI documentation is available at `/swagger-ui.html` and `/v3/api-docs` when the application is running.
