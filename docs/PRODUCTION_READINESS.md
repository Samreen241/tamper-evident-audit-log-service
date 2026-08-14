# Production Readiness Decisions

This repository is an auditable prototype. The following controls are implemented in the application:

- Production secrets and credentials are required through environment configuration.
- Payload and pagination limits are validated.
- Appends use serializable transactions and an idempotency key with a unique database index.
- CORS uses an explicit configured origin list; no origins are allowed when the setting is empty.
- Query sorting accepts only the documented sequence-number orders.

The following controls require platform infrastructure or a design extension before production:

- Rate limiting: enforce per-client login and write limits at an API gateway/WAF, with 429 responses and metrics.
- Key custody and rotation: use a KMS/Vault-managed signing key with overlapping key IDs (`kid`) and rotation runbooks; never store private signing material in application configuration.
- External anchoring: periodically publish the latest sequence/hash and signed manifest to immutable object storage or a transparency-log service. Verification should compare local history with the anchored checkpoint.
- Scalable verification/export: use checkpointed ranges, streaming exports, bounded asynchronous jobs, and signed manifests instead of loading the entire table into memory.
- Ownership: add tenant/resource ownership claims and repository predicates before exposing this service to multiple tenants.
- Failure testing: add Testcontainers/PostgreSQL tests for rollback, serialization conflicts, concurrent appends, and database outages.
