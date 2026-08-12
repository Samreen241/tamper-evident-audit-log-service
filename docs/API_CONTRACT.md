# API Contract

The concrete framework-specific contract will be finalized with implementation. The planned endpoints are:

## Write event

`POST /audit/events`

Accepts the required event fields and returns the stored record metadata, including its ID, sequence number, and hashes.

## Query events

`GET /audit/events`

Supports actor, event type, resource, time range, and pagination filters. Filters combine with AND semantics.

## Verify chain

`GET /audit/verify`

Returns whether the chain is intact. If not, it returns the first inconsistent record and a stable violation type.

## Extended endpoints

- Retention operation: implementation choice to be documented.
- Structured redaction: implementation choice to be documented.
- `GET /audit/export` for resource or actor exports.
- `GET /audit/compliance/access-report` for the clarified Scenario C requirement.
