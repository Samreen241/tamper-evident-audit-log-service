# Testing and Validation

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
