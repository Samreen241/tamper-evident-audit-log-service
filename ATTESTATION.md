# Attestation

Latest development update: 2026-08-13 - Milestone 17 engineering evidence and AI traceability updated; no commit created by AI.

Full name: Shaik Samreen

Email address: samreenshaik241@gmail.com

Assignment title: Build an AI-Assisted Software Engineering System - Audit Log Service

Start date: 2026-08-12

Submission date: 2026-08-12

Latest development update: 2026-08-13 - package structure and facade convention documented; no commit created by AI.

Latest development update: 2026-08-13 - audit record Flyway migration added for H2/PostgreSQL portability; no commit created by AI.

Latest development update: 2026-08-13 - JPA persistence adapter, repository abstraction, and MapStruct mapper added; no commit created by AI.

Latest development update: 2026-08-13 - configurable soft archival and retention policy added; no commit created by AI.

Latest development update: 2026-08-13 - compliance access reporting requirement normalized and endpoint added; no commit created by AI.

Latest development update: 2026-08-13 - persistence classes moved to entity, mapper, and repository packages; imports updated and infrastructure package removed; no commit created by AI.

Latest development update: 2026-08-13 - final repository readiness checks recorded; clean Maven rehearsal remains pending; no commit created by AI.

Latest development update: 2026-08-14 - additional JUnit coverage added for retention, tampering, facade validation, and pagination limits; tests require local Maven execution; no commit created by AI.

Latest development update: 2026-08-14 - Maven clean build and test validation succeeded after completing API, JWT, retention, redaction, Swagger, and H2 test coverage; no commit created by AI.

Latest development update: 2026-08-14 - AI usage log order corrected through Entry 031; Swagger JWT authorization and `/verify` role behavior documented; no commit created by AI.

Latest development update: 2026-08-14 - H2 console access was removed from the public security allowlist; production credentials are now required through configuration; endpoint authorization, invalid-credential, malformed-request, and pagination-limit integration tests were added; serializable append transactions and centralized safe error responses were added.

Evidence update: `.github/workflows/ci.yml` runs reproducible Java 21 verification and uploads build, Surefire, JaCoCo, and dependency-check artifacts. The `evaluation-evidence/` directory documents the artifact bundle and negative API test mapping. These generated reports must be archived from the final committed revision.

Reproducibility clarification: the included `target/` files are archived build evidence, not proof of a fresh execution in every evaluator environment. This archive does not currently contain Maven Wrapper files. Fresh verification requires Maven 3.9+ and Java 21, or the Java 21 CI workflow; `docs/REPRODUCIBLE_BUILD.md` records this limitation and the exact command.

Revision evidence: final reviewed branch is `main`. The parent revision before this attestation update is `becbffc`. The final reviewed submission revision is the commit created after adding this attestation update; after committing, record that commit's full hash here and archive the Maven, Surefire, JaCoCo, and dependency-check artifacts generated from that exact commit.

I, Shaik Samreen, attest that this submission is my own individual work, completed on my own machine and accounts, and that it honestly reflects my development process and use of AI.
