package com.samreen.auditlog.facade;

import com.samreen.auditlog.dto.AuditEventResponse;
import org.springframework.data.domain.Page;

public interface AuditLogFacade {
  AuditEventResponse appendEvent(CreateAuditEventCommand command);

  AuditEventResponse appendEvent(CreateAuditEventCommand command, String idempotencyKey);

  Page<AuditEventResponse> queryEvents(AuditEventQuery query);

  AuditChainVerificationResponse verifyChain();
}
