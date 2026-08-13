package com.samreen.auditlog.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
@Schema(description = "Persisted audit event")
public record AuditEventResponse(UUID id, long sequenceNumber, String eventType, String actorId, String resourceType, String resourceId, Map<String,Object> payload, Instant timestamp, String previousHash, String contentHash) { }
