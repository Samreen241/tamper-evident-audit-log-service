package com.samreen.auditlog.facade;

import java.time.Instant;
import org.springframework.data.domain.Page;

public interface ComplianceReportFacade {
  Page<ComplianceAccessReport> accessReport(
      String actorId,
      String resourceId,
      String eventType,
      Instant from,
      Instant to,
      int page,
      int size);
}
