package com.samreen.auditlog.facade;
import java.time.Instant;
import java.util.UUID;
public record ComplianceAccessReport(UUID auditRecordId,long sequenceNumber,String actorId,String resourceId,String eventType,Instant eventTimestamp,String contentHash) { }
