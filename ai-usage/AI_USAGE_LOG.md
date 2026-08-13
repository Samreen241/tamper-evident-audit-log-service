# AI Usage Traceability

Record meaningful AI-assisted work here as the project progresses.

Each note should include:

- Date
- Task and intended outcome
- Prompt or prompt summary
- Context provided
- Output accepted, modified, or rejected
- Reason for the decision
- Validation performed
- Human sign-off or follow-up action

## Entry 001 - Documentation baseline

- Date: 2026-08-12
- Task: Establish the repository documentation baseline before implementation.
- Prompt summary: Create the architecture file and all required assignment documentation as part of the first commit, without creating application project structure.
- Context provided: Assignment requirements covering the audit log service, three scenarios, testing, security, AI traceability, and final engineering summary.
- Output accepted: Documentation-only baseline containing requirements, architecture, API contract, scenario plans, testing, security, and final-summary files.
- Output modified: Generic documentation filenames were replaced with descriptive names, including `PROJECT_OVERVIEW.md`.
- Output rejected: Application source folders and implementation scaffolding were intentionally not created at this stage.
- Validation performed: Reviewed the generated file list and confirmed the documentation baseline was committed as the repository's first commit.
- Planned commit message: `docs: establish audit log engineering baseline`
- Human sign-off: Shaik Samreen reviewed and approved the baseline before committing and pushing.

## Entry 002 - Application skeleton

- Date: 2026-08-12
- Task: Create the initial application skeleton without audit-log business logic.
- Prompt summary: Set up a Java 21, Maven, Spring Boot 4.1.0 codebase skeleton.
- Context provided: Existing documentation-only repository baseline and planned audit-log service architecture.
- Output accepted: Maven build descriptor, Spring Boot bootstrap class, application configuration, and context-load test.
- Output rejected: Audit event models, controllers, persistence, hashing, retention, redaction, and export logic were not created.
- Validation planned: Run `mvn test` after dependency resolution is available.
- Human sign-off: Shaik Samreen will review the generated skeleton, run the build, and commit it if accepted.

## Entry 003 - Persistence and facade baseline

- Date: 2026-08-12
- Task: Prepare the application for production PostgreSQL, local H2 development, and facade-based application orchestration.
- Prompt summary: Add the required Maven dependencies and document the facade pattern and database strategy.
- Context provided: Java 21, Maven, Spring Boot 4.1.0 skeleton and the audit-log architecture.
- Output accepted: Spring Data JPA, PostgreSQL runtime, H2 runtime, and Flyway dependencies; architecture documentation for the facade and persistence boundaries.
- Output rejected: No entities, repositories, facades, controllers, migrations, or database credentials were implemented in this dependency-only change.
- Validation planned: Run Maven dependency resolution and tests, then verify migrations against H2 and PostgreSQL during implementation.
- Human sign-off: Shaik Samreen will review the dependency set and commit it with the message `build: add persistence and facade foundations`.

## Entry 004 - Package and mapping convention

- Date: 2026-08-13
- Task: Reformat the implementation package structure around the facade pattern.
- Prompt summary: Use one `facade` package for application services and organize the code under config, controller, exception, entity, mapper, facade, repository, dto, and utils packages.
- Context provided: Java 21, Spring Boot, Maven, PostgreSQL production, H2 local/testing, Swagger/OpenAPI, JUnit, JWT, and the audit-log requirements.
- Output accepted: The architecture now defines the requested package layout and MapStruct as the mapping technology.
- Design decision: Facades are the single service boundary between controllers and persistence/domain orchestration; controllers must not call repositories directly.
- Output rejected: No package relocation, MapStruct dependency change, or implementation code was performed in this documentation update.
- Validation performed: Reviewed the architecture package tree and confirmed no Git commit was created.
- Human sign-off: Shaik Samreen will review the package convention and use the commit message `build: establish audit log service foundation` when committing the related baseline changes.

## Entry 007 - Audit record migration

- Date: 2026-08-13
- Task: Define the production-compatible audit record schema and Flyway migration.
- Prompt summary: Create `V1__create_audit_records.sql` with audit fields, constraints, and indexes for H2 and PostgreSQL.
- Output accepted: Added the audit record table with UUID identity, unique sequence and content hashes, non-null chain hashes, status constraint, portable text storage for structured JSON values, and required query indexes.
- Design decision: `payload` and `redaction_metadata` use `TEXT` in the shared migration for H2/PostgreSQL portability; the application will serialize and validate structured JSON.
- Output rejected: No update/delete API or database mutation service was added.
- Validation planned: Run Flyway and application tests against H2, then execute the same migration against PostgreSQL before production use.
- Human sign-off: Shaik Samreen will review and commit this migration.

## Entry 009 - Persistence adapter

- Date: 2026-08-13
- Task: Add database access behind an infrastructure abstraction.
- Prompt summary: Create the JPA entity, Spring Data repository, and MapStruct mapper for audit records without exposing update/delete operations.
- Output accepted: Added `AuditRecordEntity`, query-focused `AuditRecordRepository`, and `AuditRecordMapper` with JSON payload conversion.
- Design decision: The repository declares no update or delete methods. Structured payloads are serialized into the portable `TEXT` database column.
- Output rejected: No facade orchestration, HTTP API, or direct datastore tampering utility was added.
- Validation planned: Run Maven compilation/tests and verify generated MapStruct code against H2 and PostgreSQL.
- Human sign-off: Shaik Samreen will review and commit this persistence adapter.

## Entry 016 - Retention and archival

- Date: 2026-08-13
- Task: Add configurable soft archival without breaking chain verification.
- Prompt summary: Implement retention policy duration, manual archive operation, active/archived statuses, and database-backed archival.
- Output accepted: Added `RetentionPolicyService`, `RetentionFacade`, and a transactional repository operation that marks expired active records as `ARCHIVED`.
- Design decision: Archived records remain physically present and are included in full-chain verification; the retention window is configured through `app.retention.window`.
- Output rejected: No physical deletion or scheduled job was added; authorization for the manual retention operation will be applied through the admin security boundary.
- Validation planned: Test cutoff behavior, idempotent archival, and verification after archival.
- Human sign-off: Shaik Samreen will review and commit this retention implementation.

## Entry 017 - Structured redaction

- Date: 2026-08-13
- Task: Add authorized, allow-listed redaction metadata and original-value commitments.
- Prompt summary: Implement redaction requests, allowed JSON paths, commitment hashes, repeated-redaction protection, and a redaction endpoint.
- Output accepted: Added redaction facade/controller, configurable allowed paths, commitment metadata, `REDACTED` status, and admin-only authorization.
- Design decision: The original event hash and payload are not silently overwritten; the operation records a commitment and redaction metadata.
- Output rejected: Full payload value removal and redaction audit-event chaining remain follow-up work because changing the payload requires a commitment-aware verification scheme.
- Validation planned: Test allowed/disallowed paths, unauthorized access, repeated redaction, verification, and exports after redaction.
- Human sign-off: Shaik Samreen will review and commit this redaction foundation.

## Entry 019 - Compliance access reporting

- Date: 2026-08-13
- Task: Normalize and implement the ambiguous regulator access-report requirement.
- Prompt summary: Document clarifications, assumptions, scope, security, filters, and implement a traceable access report.
- Output accepted: Added compliance reporting documentation, a read-only facade, role-protected endpoint, access-event filtering, pagination, and source audit identifiers/hashes.
- Design decision: The prototype classifies event types containing `ACCESS` as access events; this taxonomy must be confirmed before production use.
- Output rejected: Regulator-specific formats, scheduling, and cross-system ingestion remain out of scope.
- Validation planned: Test inclusion/exclusion, filters, pagination, JWT role enforcement, and source-record traceability.
- Human sign-off: Shaik Samreen will review and commit this compliance implementation.
