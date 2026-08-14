# Testing and Validation

## Milestone 17 evidence status

Existing coverage includes context loading and hash-chain tests. Persistence, tampering, JWT, retention, redaction, export, compliance, concurrency, and clean-checkout tests must be executed before claiming quality gates pass.

Additional unit coverage now includes retention cutoff validation, non-positive retention rejection, direct content-tampering detection, append validation, and maximum query-page validation.

## Test levels

- Unit tests for canonicalization, hashing, validation, filtering, redaction, and verification.
- Integration tests for persistence, transactions, APIs, and direct datastore tampering.
- End-to-end tests covering write, query, verify, tamper, and verify again.
- Negative tests for malformed inputs, unauthorized operations, gaps, and inconsistent hashes.

## Quality gates

- Clean build succeeds.
- Formatting and static analysis pass.
- Automated tests pass.
- Dependency and security checks are reviewed.
- Setup instructions work from a clean checkout.

## Verification evidence

The successful verification command is:

```text
mvn clean verify
```

The command produces Surefire results under `target/surefire-reports/` and JaCoCo reports under `target/site/jacoco/`. These generated artifacts should be copied into the evaluation evidence bundle because `target/` is intentionally ignored by Git.

The requirement-to-test mapping is maintained in `docs/REQUIREMENTS_TO_TEST_MATRIX.md`.

Production-readiness decisions and infrastructure dependencies are maintained in `docs/PRODUCTION_READINESS.md`.

## Human review

High-impact changes to chain construction, redaction, retention, and authorization assumptions require engineer review before acceptance.
