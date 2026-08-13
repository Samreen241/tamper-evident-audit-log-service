# Security and Limitations

## Milestone 17 security decisions

Production credentials and JWT settings come from environment variables. Redaction paths are allow-listed and admin protected, and compliance reporting requires the compliance reviewer role. Key rotation, payload limits, persistent identity management, and immutable external storage remain limitations.

The prototype review will cover input validation, payload size limits, query bounds, sensitive-data logging, authorization assumptions, error safety, concurrency, timestamp trust, retention behavior, and export exposure.

Unless implemented explicitly, this service does not claim to provide authentication, distributed consensus, hardware-backed key protection, immutable external storage, or full production deployment controls.
