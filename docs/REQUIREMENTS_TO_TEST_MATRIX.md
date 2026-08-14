# Requirements-to-Test Matrix

| Requirement / control | Primary implementation | Verification |
|---|---|---|
| JWT login and invalid credentials | `AuthController`, `SecurityConfig` | `AuditApiSmokeIntegrationTest.invalidCredentialsAreRejected` |
| Authentication required | `SecurityConfig` | `AuditApiSmokeIntegrationTest.queryRequiresAuthentication` |
| Role protection | `SecurityConfig` | `writerCannotVerifyChain`, `writerCannotUseAdministrativeReports`, `adminCanVerifyChain` |
| H2 console is not public | `SecurityConfig` | `h2ConsoleIsNotPubliclyAccessible` |
| Audit-event validation | `CreateAuditEventRequest` | `malformedAuditEventIsRejected` |
| Query page-size limit | `AuditLogFacadeImpl` | `oversizedPageIsRejected` |
| Hash-chain integrity | `HashChainService` | `HashChainServiceTest`, `HashChainTamperingTest` |
| Serializable append transaction | `AuditLogCommandService` | Maven integration test execution; concurrency test remains recommended |
| Idempotent append | `Idempotency-Key`, V3 migration, repository lookup | `idempotencyKeyReturnsTheOriginalEvent` |
| Retention and redaction | Corresponding facades/services | `RetentionPolicyServiceTest`, `RedactionFacadeImplTest` |
| Export and compliance behavior | Export/compliance facades | `AuditExportFacadeImplTest`, `ComplianceReportFacadeImplTest` |

## Deliberate remaining controls

Rate limiting and distributed JWT revocation are deployment-level controls not implemented in this prototype. Production deployment must provide an API gateway/WAF rate limit and a token revocation or short-lived-token strategy.
