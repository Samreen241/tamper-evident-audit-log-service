package com.samreen.auditlog.facade;

import com.samreen.auditlog.domain.model.AuditRecord;
import com.samreen.auditlog.dto.AuditEventResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class AuditLogFacadeImpl implements AuditLogFacade {
  private final AuditLogCommandService service;

  public AuditLogFacadeImpl(AuditLogCommandService service) {
    this.service = service;
  }

  public AuditEventResponse appendEvent(CreateAuditEventCommand c) {
    return appendEvent(c, null);
  }

  public AuditEventResponse appendEvent(CreateAuditEventCommand c, String idempotencyKey) {
    if (c == null
        || blank(c.eventType())
        || blank(c.actorId())
        || blank(c.resourceType())
        || blank(c.resourceId())
        || c.payload() == null)
      throw new IllegalArgumentException("Required audit event fields are missing");
    return response(service.append(c, idempotencyKey));
  }

  public Page<AuditEventResponse> queryEvents(AuditEventQuery q) {
    if (q.page() < 0 || q.size() < 1 || q.size() > 100)
      throw new IllegalArgumentException("Page size must be between 1 and 100");
    return service
        .query(
            q,
            PageRequest.of(
                q.page(),
                q.size(),
                Sort.by("sequenceNumber").ascending().and(Sort.by("id").ascending())))
        .map(this::response);
  }

  public AuditChainVerificationResponse verifyChain() {
    var r = service.verify();
    return new AuditChainVerificationResponse(
        r.intact(), r.checkedRecords(), r.firstInvalidRecordId(), r.violationType(), r.message());
  }

  private AuditEventResponse response(AuditRecord r) {
    return new AuditEventResponse(
        r.id(),
        r.sequenceNumber(),
        r.event().eventType(),
        r.event().actorId(),
        r.event().resourceType(),
        r.event().resourceId(),
        r.event().payload(),
        r.event().timestamp(),
        r.previousHash(),
        r.contentHash());
  }

  private boolean blank(String value) {
    return value == null || value.isBlank();
  }
}
