package com.samreen.auditlog.facade;

import com.samreen.auditlog.domain.service.RetentionPolicyService;
import com.samreen.auditlog.repository.AuditRecordRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetentionFacadeImpl implements RetentionFacade {
  private final AuditRecordRepository repository;
  private final RetentionPolicyService policy;

  public RetentionFacadeImpl(AuditRecordRepository repository, RetentionPolicyService policy) {
    this.repository = repository;
    this.policy = policy;
  }

  @Override
  @Transactional
  public int archiveExpired() {
    return repository.archiveActiveBefore(policy.cutoff(Instant.now()));
  }
}
