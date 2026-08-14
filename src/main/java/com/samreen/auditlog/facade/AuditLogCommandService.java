package com.samreen.auditlog.facade;

import com.samreen.auditlog.domain.model.AuditEvent;
import com.samreen.auditlog.domain.model.AuditRecord;
import com.samreen.auditlog.domain.model.VerificationResult;
import com.samreen.auditlog.domain.service.HashChainService;
import com.samreen.auditlog.entity.AuditRecordEntity;
import com.samreen.auditlog.mapper.AuditRecordMapper;
import com.samreen.auditlog.repository.AuditRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuditLogCommandService {
    private final AuditRecordRepository repository;
    private final AuditRecordMapper mapper;
    private final HashChainService hashing;

    public AuditLogCommandService(AuditRecordRepository r, AuditRecordMapper m, HashChainService h) {
        repository = r;
        mapper = m;
        hashing = h;
    }

    @Transactional
    public AuditRecord append(CreateAuditEventCommand c) {
        var latest = repository.findTopByOrderBySequenceNumberDesc();
        long seq = latest.map(AuditRecordEntity::getSequenceNumber).orElse(0L) + 1;
        String previous = latest.map(AuditRecordEntity::getContentHash).orElse(HashChainService.GENESIS_HASH);
        var event = new AuditEvent(c.eventType(), c.actorId(), c.resourceType(), c.resourceId(), c.payload(), c.timestamp() == null ? Instant.now() : c.timestamp());
        var draft = new AuditRecord(UUID.randomUUID(), seq, event, previous, "");
        var record = new AuditRecord(draft.id(), seq, event, previous, hashing.contentHash(draft));
        var entity = mapper.toEntity(record);
        var now = Instant.now();
        entity.setIngestedAt(now);
        entity.setCreatedAt(now);
        entity.setStatus("ACTIVE");
        repository.save(entity);
        return record;
    }

    @Transactional(readOnly = true)
    public Page<AuditRecord> query(AuditEventQuery c, Pageable pageable) {
        Specification<AuditRecordEntity> s = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        if (c.actorId() != null) s = s.and((r, q, b) -> b.equal(r.get("actorId"), c.actorId()));
        if (c.resourceType() != null) s = s.and((r, q, b) -> b.equal(r.get("resourceType"), c.resourceType()));
        if (c.resourceId() != null) s = s.and((r, q, b) -> b.equal(r.get("resourceId"), c.resourceId()));
        if (c.eventType() != null) s = s.and((r, q, b) -> b.equal(r.get("eventType"), c.eventType()));
        if (c.from() != null) s = s.and((r, q, b) -> b.greaterThanOrEqualTo(r.get("eventTimestamp"), c.from()));
        if (c.to() != null) s = s.and((r, q, b) -> b.lessThan(r.get("eventTimestamp"), c.to()));
        return repository.findAll(s, pageable).map(mapper::toDomain);
    }

    @Transactional(readOnly = true)
    public VerificationResult verify() {
        return hashing.verify(repository.findAllByOrderBySequenceNumberAsc().stream().map(mapper::toDomain).toList());
    }
}
