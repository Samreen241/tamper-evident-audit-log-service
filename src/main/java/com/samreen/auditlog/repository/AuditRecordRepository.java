package com.samreen.auditlog.repository;

import com.samreen.auditlog.entity.AuditRecordEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface AuditRecordRepository extends JpaRepository<AuditRecordEntity, UUID>, JpaSpecificationExecutor<AuditRecordEntity> {
    Optional<AuditRecordEntity> findTopByOrderBySequenceNumberDesc();
    java.util.List<AuditRecordEntity> findAllByOrderBySequenceNumberAsc();
    @Modifying
    @Query("update AuditRecordEntity record set record.status = 'ARCHIVED' where record.status = 'ACTIVE' and record.eventTimestamp < :cutoff")
    int archiveActiveBefore(@Param("cutoff") Instant cutoff);
    Page<AuditRecordEntity> findBySequenceNumberBetween(long from, long to, Pageable pageable);
    Page<AuditRecordEntity> findByActorId(String actorId, Pageable pageable);
    Page<AuditRecordEntity> findByResourceTypeAndResourceId(String resourceType, String resourceId, Pageable pageable);
    Page<AuditRecordEntity> findByEventType(String eventType, Pageable pageable);
    Page<AuditRecordEntity> findByEventTimestampBetween(Instant from, Instant to, Pageable pageable);
}
