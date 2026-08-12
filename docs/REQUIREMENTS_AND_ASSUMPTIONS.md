# Requirements and Assumptions

## Objective

Build a tamper-evident, append-only audit log service that records events, supports filtered retrieval, and detects unauthorized changes through hash-chain verification.

## Functional scope

- Accept audit events containing event type, actor, resource type, resource ID, payload, and timestamp.
- Store events in append-only logical order.
- Query events using actor, resource, event type, resource, and time-range filters.
- Support bounded pagination.
- Expose full-chain verification and identify the first detected violation.
- Extend the service with retention, structured redaction, and verifiable export.
- Clarify and implement a focused compliance access-reporting capability.

## Initial decisions

- SHA-256 will be used for record and chain hashes.
- The server will assign the authoritative ingestion timestamp; an optional caller timestamp may be retained as event metadata.
- Records will have a stable ID and monotonically increasing sequence number.
- The first record will reference a documented genesis value.
- Hash input will use deterministic canonical JSON with UTF-8 encoding.
- Query filters will combine using AND semantics.
- Query results will use stable sequence ordering and bounded page sizes.
- Writes will be serialized transactionally so two records cannot claim the same chain position.

## Ambiguities to validate during implementation

- Whether caller timestamps need to be trusted for compliance reporting.
- Whether archived records must remain queryable.
- Which payload paths are eligible for redaction.
- Whether exported subsets require a full-chain proof or boundary proofs.
- Authentication and authorization expectations, which are not defined by the prototype scope.

## Acceptance criteria

The system can write, query, verify, tamper with, and re-verify records end to end. All extended behavior is documented, tested, and represented in the final engineering summary.
