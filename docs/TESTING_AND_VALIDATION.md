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

## Human review

High-impact changes to chain construction, redaction, retention, and authorization assumptions require engineer review before acceptance.
