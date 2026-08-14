# Negative API Test Results

| Scenario | Expected | Test |
|---|---:|---|
| Missing JWT for event query | 401 | `queryRequiresAuthentication` |
| Invalid credentials | 401 | `invalidCredentialsAreRejected` |
| Writer verifies chain | 403 | `writerCannotVerifyChain` |
| Writer uses export/compliance routes | 403 | `writerCannotUseAdministrativeReports` |
| Public H2 console | 401 | `h2ConsoleIsNotPubliclyAccessible` |
| Malformed event | 400 | `malformedAuditEventIsRejected` |
| Oversized page | 400 | `oversizedPageIsRejected` |
| Repeated idempotency key | Original event | `idempotencyKeyReturnsTheOriginalEvent` |

The authoritative results are the Surefire XML/text files generated under `target/surefire-reports/`.
