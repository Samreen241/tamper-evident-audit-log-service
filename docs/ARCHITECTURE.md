# Architecture

## Components

The service will be organized into the following logical components:

- API layer: HTTP endpoints, request parsing, response mapping, and validation errors.
- Audit service: append-only event creation and query orchestration.
- Hash-chain service: canonicalization, hashing, chain-link construction, and verification.
- Persistence layer: transactions, ordered records, indexes, and test-store integration.
- Retention service: policy evaluation and archival or soft-deletion state changes.
- Redaction service: authorized structured redaction and preservation of verification commitments.
- Export service: filtered bundles with chain metadata and independent verification information.

## Facade pattern

The application layer will expose a small audit-log facade that coordinates validation, hashing, persistence, verification, retention, redaction, and export use cases. Controllers will depend on facade interfaces rather than directly accessing repositories or lower-level services. This keeps HTTP concerns separate from business orchestration and provides a stable boundary for tests and future adapters.

## Persistence

Spring Data JPA will provide the persistence abstraction. PostgreSQL is the production database, while H2 is used for local development and fast tests. Flyway will manage versioned schema migrations so local and production schemas remain reproducible.

## Data model

An audit record will contain:

- `id`
- `sequenceNumber`
- `eventType`
- `actorId`
- `resourceType`
- `resourceId`
- `payload`
- `eventTimestamp`
- `ingestedAt`
- `previousHash`
- `contentHash`
- `status`
- redaction metadata, when applicable

## Hash-chain design

`contentHash` is SHA-256 over canonical UTF-8 JSON containing the authoritative event fields. `previousHash` stores the hash of the immediately preceding sequence. The first record references the configured genesis value. Verification recalculates each content hash and checks every chain link in sequence order.

## Write consistency

Creating a record and advancing the chain position must occur in one transaction. The implementation must prevent concurrent writers from producing duplicate sequence numbers or divergent links.

## Key trade-offs

- A serialized write path simplifies correctness but limits write throughput.
- Soft-archiving preserves verification history more simply than physical deletion.
- Redaction improves privacy but requires explicit commitments and metadata to preserve evidence.
- A filtered export is easier to consume than a complete chain, but requires boundary metadata for independent verification.

## Scope boundary

Authentication, distributed consensus, key rotation, external immutable storage, and production deployment infrastructure are outside the initial prototype unless added explicitly during implementation.
