# Audit Log Service

Tamper-evident audit log service prototype for the interview assignment.

## Project status

Implementation and local verification are complete. The repository contains the application, migrations, automated tests, engineering summary, and reproducible validation notes below.

## Capabilities

- Append-only audit event ingestion with deterministic canonical JSON and SHA-256 hash chaining.
- Filtered and paginated event queries.
- Chain verification and tamper detection.
- Retention, redaction metadata, and verifiable export.
- JWT authentication foundations and compliance access reporting.
- H2 local profile and PostgreSQL production profile with Flyway migrations.

## Reproducible validation

Run from the repository root with Java 21 and Maven 3.9.11:

```powershell
mvn clean verify
```

The verified run executed 22 tests with 0 failures, 0 errors, and 0 skipped tests. It also passed Java version enforcement and Spotless, built the application JAR, and generated JaCoCo output at `target/site/jacoco/index.html`.

JaCoCo aggregate figures from the verified run:

| Metric | Covered | Total | Rate |
|---|---:|---:|---:|
| Instructions | 1,498 | 2,426 | 61.8% |
| Lines | 321 | 517 | 62.1% |
| Branches | 58 | 138 | 42.0% |
| Methods | 93 | 142 | 65.5% |
| Classes | 35 | 40 | 87.5% |

### Test-to-requirement traceability

| Requirement | Evidence |
|---|---|
| Canonical hashing and append behavior | `HashChainServiceTest`, `AuditLogFacadeImplTest` |
| Tamper detection | `HashChainTamperingTest` |
| Retention behavior | `RetentionPolicyServiceTest` |
| Export behavior | `AuditExportFacadeImplTest` |
| Compliance reporting | `ComplianceReportFacadeImplTest` |
| Redaction metadata | `RedactionFacadeImplTest` |
| JWT issuance and parsing | `JwtTokenServiceTest`, `AuditApiSmokeIntegrationTest` |
| Authentication and public API boundaries | `AuditApiSmokeIntegrationTest` |
| Application context and persistence wiring | `AuditLogServiceApplicationTests` |

## Database and deployment validation

The migration chain is versioned in `src/main/resources/db/migration`:

- `V1__create_audit_records.sql`
- `V2__add_redaction_metadata.sql`

The local profile has been startup-checked with H2 and the packaged JAR returned HTTP 200 for `/v3/api-docs`. The production profile is PostgreSQL-based and requires `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`; PostgreSQL deployment execution is not claimed here because no PostgreSQL instance or deployment environment is committed to the repository. Before production release, run the packaged JAR against a disposable PostgreSQL instance and verify Flyway migration, append/query/verify, and rollback/backup procedures.

## Concurrency and direct-tampering evidence boundary

The automated suite proves hash-chain tampering behavior at the service level, but it does not currently provide an independently reproducible PostgreSQL test that edits a row directly while concurrent writers append events. These are explicit validation gaps, not claims of completion. A production readiness test should use two writer clients against PostgreSQL, assert unique ordering/transaction behavior, then modify a persisted hash or payload through SQL and assert the verification endpoint reports the broken chain.

## Privacy and external immutability boundaries

Redaction currently records redaction metadata while preserving chain/audit integrity; it is not a cryptographic erasure guarantee. The product must define whether sensitive payloads are physically deleted, replaced by a fixed marker, or retained under restricted access, and must document the legal-retention exception path.

The current service does not implement immutable external storage. A production design should write signed/exported chain-boundary proofs to WORM/object-lock storage, retain the object version and digest, and periodically verify them. Until that integration exists, exported proofs are portable evidence files rather than independently immutable attestations.

## Compliance assumptions requiring product confirmation

- Access-event classification must define which reads, exports, verification calls, administrative actions, and failed access attempts are reportable.
- Exported chain-boundary proofs must define the covered event IDs, first/last hashes, timestamp, algorithm, signer/key identity, and verification procedure.
- These decisions should be confirmed by the product/compliance owner before the prototype is treated as a production control.

## Local setup

Use Java 21 and Maven 3.9.11. For local startup:

```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Swagger/OpenAPI endpoints are available at `/swagger-ui.html` and `/v3/api-docs` while the application is running.

## Development process and confidentiality

Requirements, design decisions, validation evidence, and AI-assisted development notes are kept in this repository. AI-assisted work and human decisions are recorded in `ai-usage/AI_USAGE_LOG.md`.

This repository is private and is intended only for the assessment reviewers and the candidate. Do not commit the supplied assignment document or other confidential assessment material.
