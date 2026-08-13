package com.samreen.auditlog.facade;

import java.time.Instant;
import java.util.Map;

public record CreateAuditEventCommand(String eventType, String actorId, String resourceType,
                                     String resourceId, Map<String, Object> payload, Instant timestamp) {
}
