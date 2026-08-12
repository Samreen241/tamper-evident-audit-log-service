# Scenario B - Retention, Redaction, and Export

## Planned approach

Prefer logical archival or soft deletion so historical chain links remain available to verification. Structured redaction will preserve a commitment to the original sensitive value and record redaction metadata rather than silently mutating a record. Exports will include selected records plus sufficient chain-boundary metadata for independent verification.

## Validation evidence

Tests will cover policy execution, verification with archived records, allowed and disallowed redaction paths, repeated redaction, export verification, and tampered export detection.
