package com.samreen.auditlog.facade;

import com.samreen.auditlog.domain.service.HashChainService;
import com.samreen.auditlog.mapper.AuditRecordMapper;
import com.samreen.auditlog.repository.AuditRecordRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditExportFacadeImpl implements AuditExportFacade {
  private final AuditRecordRepository repository;
  private final AuditRecordMapper mapper;

  public AuditExportFacadeImpl(AuditRecordRepository r, AuditRecordMapper m) {
    repository = r;
    mapper = m;
  }

  @Transactional(readOnly = true)
  public AuditExportBundle export(String resourceId, String actorId) {
    if ((resourceId == null || resourceId.isBlank()) && (actorId == null || actorId.isBlank()))
      throw new IllegalArgumentException("resourceId or actorId is required");
    var all = repository.findAllByOrderBySequenceNumberAsc();
    var selected =
        all.stream()
            .filter(
                e ->
                    (resourceId == null || resourceId.equals(e.getResourceId()))
                        && (actorId == null || actorId.equals(e.getActorId())))
            .map(mapper::toDomain)
            .toList();
    var records =
        selected.stream()
            .map(
                r ->
                    new ExportedAuditRecord(
                        r.id(),
                        r.sequenceNumber(),
                        r.event().eventType(),
                        r.event().actorId(),
                        r.event().resourceType(),
                        r.event().resourceId(),
                        r.event().payload(),
                        r.event().timestamp(),
                        r.previousHash(),
                        r.contentHash(),
                        "ACTIVE"))
            .toList();
    long first = records.isEmpty() ? 0 : records.get(0).sequenceNumber(),
        last = records.isEmpty() ? 0 : records.get(records.size() - 1).sequenceNumber();
    return new AuditExportBundle(
        Instant.now(),
        resourceId,
        actorId,
        first,
        last,
        records.isEmpty() ? HashChainService.GENESIS_HASH : records.get(0).previousHash(),
        null,
        HashChainService.GENESIS_HASH,
        records,
        "Recalculate content hashes and verify previous-hash links and boundaries.");
  }
}
