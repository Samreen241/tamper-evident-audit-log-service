package com.samreen.auditlog.facade;
import java.time.Instant;
import java.util.List;
public record AuditExportBundle(Instant exportedAt,String resourceId,String actorId,long firstSequence,long lastSequence,String precedingHash,String followingHash,String genesisHash,List<ExportedAuditRecord> records,String verificationInstructions) { }
