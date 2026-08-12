# Scenario A - Core Audit Log Service

## Decomposition

1. Normalize event and chain requirements.
2. Define the record schema and canonical hash input.
3. Implement transactional append.
4. Implement filtered, paginated queries.
5. Implement full-chain verification.
6. Add direct-tampering integration tests.
7. Document and rehearse the end-to-end demonstration.

## Validation evidence

Evidence will include API tests, hash-chain unit tests, concurrency tests, and an integration test that changes persisted data and confirms detection.
