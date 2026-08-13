package com.samreen.auditlog.facade;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
public record AuditEventResponse(UUID id,long sequenceNumber,String eventType,String actorId,String resourceType,String resourceId,Map<String,Object> payload,Instant timestamp,String previousHash,String contentHash) { }
