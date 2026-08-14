package com.samreen.auditlog.domain.model;

import java.util.Map;
import java.util.UUID;

public record AuditRecord(
    UUID id,
    long sequenceNumber,
    AuditEvent event,
    String previousHash,
    String contentHash,
    String status,
    String redactionMetadata) {
  public AuditRecord(
      UUID id, long sequenceNumber, AuditEvent event, String previousHash, String contentHash) {
    this(id, sequenceNumber, event, previousHash, contentHash, "ACTIVE", null);
  }

  public Map<String, Object> hashContent() {
    return Map.of(
        "eventType",
        event.eventType(),
        "actorId",
        event.actorId(),
        "resourceType",
        event.resourceType(),
        "resourceId",
        event.resourceId(),
        "payload",
        event.payload(),
        "timestamp",
        event.timestamp());
  }
}
