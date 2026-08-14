# Security and Limitations

## Milestone 17 security decisions

Production credentials and JWT settings come from environment variables. Redaction paths are allow-listed and admin protected, and compliance reporting requires the compliance reviewer role. Key rotation, payload limits, persistent identity management, and immutable external storage remain limitations.

The H2 console is not publicly permitted by the application security chain. Local-only credentials are confined to the local profile; production credentials are required through environment configuration. Audit appends use serializable transactions and support an explicit `Idempotency-Key` backed by a unique database index.

The prototype review will cover input validation, payload size limits, query bounds, sensitive-data logging, authorization assumptions, error safety, concurrency, timestamp trust, retention behavior, and export exposure.

Unless implemented explicitly, this service does not claim to provide authentication, distributed consensus, hardware-backed key protection, immutable external storage, or full production deployment controls.

Rate limiting should be enforced at the gateway or WAF in production. JWTs are short-lived, but distributed revocation and refresh-token rotation are not implemented in this prototype and must be supplied by the deployment identity platform before production use.
