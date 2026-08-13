package com.samreen.auditlog.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_records", indexes = {
        @Index(name = "idx_audit_records_actor_id", columnList = "actor_id"),
        @Index(name = "idx_audit_records_event_type", columnList = "event_type"),
        @Index(name = "idx_audit_records_resource", columnList = "resource_type,resource_id"),
        @Index(name = "idx_audit_records_event_timestamp", columnList = "event_timestamp")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uq_audit_records_sequence", columnNames = "sequence_number"),
        @UniqueConstraint(name = "uq_audit_records_content_hash", columnNames = "content_hash")
})
public class AuditRecordEntity {
    @Id
    private UUID id;
    @Column(name = "sequence_number", nullable = false)
    private long sequenceNumber;
    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;
    @Column(name = "actor_id", nullable = false)
    private String actorId;
    @Column(name = "resource_type", nullable = false, length = 100)
    private String resourceType;
    @Column(name = "resource_id", nullable = false)
    private String resourceId;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;
    @Column(name = "event_timestamp", nullable = false)
    private Instant eventTimestamp;
    @Column(name = "ingested_at", nullable = false)
    private Instant ingestedAt;
    @Column(name = "previous_hash", nullable = false, length = 128)
    private String previousHash;
    @Column(name = "content_hash", nullable = false, length = 128)
    private String contentHash;
    @Column(nullable = false, length = 32)
    private String status;
    @Column(name = "redaction_metadata", columnDefinition = "TEXT")
    private String redactionMetadata;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public AuditRecordEntity() { }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public long getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(long value) { sequenceNumber = value; }
    public String getEventType() { return eventType; }
    public void setEventType(String value) { eventType = value; }
    public String getActorId() { return actorId; }
    public void setActorId(String value) { actorId = value; }
    public String getResourceType() { return resourceType; }
    public void setResourceType(String value) { resourceType = value; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String value) { resourceId = value; }
    public String getPayload() { return payload; }
    public void setPayload(String value) { payload = value; }
    public Instant getEventTimestamp() { return eventTimestamp; }
    public void setEventTimestamp(Instant value) { eventTimestamp = value; }
    public Instant getIngestedAt() { return ingestedAt; }
    public void setIngestedAt(Instant value) { ingestedAt = value; }
    public String getPreviousHash() { return previousHash; }
    public void setPreviousHash(String value) { previousHash = value; }
    public String getContentHash() { return contentHash; }
    public void setContentHash(String value) { contentHash = value; }
    public String getStatus() { return status; }
    public void setStatus(String value) { status = value; }
    public String getRedactionMetadata() { return redactionMetadata; }
    public void setRedactionMetadata(String value) { redactionMetadata = value; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant value) { createdAt = value; }
}
