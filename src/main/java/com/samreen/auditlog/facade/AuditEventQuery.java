package com.samreen.auditlog.facade;

import java.time.Instant;

public record AuditEventQuery(
    String actorId,
    String resourceType,
    String resourceId,
    String eventType,
    Instant from,
    Instant to,
    int page,
    int size,
    String sort) {}
