package com.samreen.auditlog.facade;

public interface AuditExportFacade {
  AuditExportBundle export(String resourceId, String actorId);
}
