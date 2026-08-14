package com.samreen.auditlog.domain.model;

import java.time.Instant;
import java.util.Map;

public record AuditEvent(
    String eventType,
    String actorId,
    String resourceType,
    String resourceId,
    Map<String, Object> payload,
    Instant timestamp) {}
