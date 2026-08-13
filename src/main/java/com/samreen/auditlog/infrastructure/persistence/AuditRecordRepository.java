package com.samreen.auditlog.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface AuditRecordRepository extends JpaRepository<AuditRecordEntity, UUID> {
    Optional<AuditRecordEntity> findTopByOrderBySequenceNumberDesc();
    Page<AuditRecordEntity> findBySequenceNumberBetween(long from, long to, Pageable pageable);
    Page<AuditRecordEntity> findByActorId(String actorId, Pageable pageable);
    Page<AuditRecordEntity> findByResourceTypeAndResourceId(String resourceType, String resourceId, Pageable pageable);
    Page<AuditRecordEntity> findByEventType(String eventType, Pageable pageable);
    Page<AuditRecordEntity> findByEventTimestampBetween(Instant from, Instant to, Pageable pageable);
}
