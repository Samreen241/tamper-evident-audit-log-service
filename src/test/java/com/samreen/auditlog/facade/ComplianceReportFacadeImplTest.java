package com.samreen.auditlog.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.samreen.auditlog.entity.AuditRecordEntity;
import com.samreen.auditlog.repository.AuditRecordRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

class ComplianceReportFacadeImplTest {
  private final AuditRecordRepository repository = mock(AuditRecordRepository.class);
  private final ComplianceReportFacadeImpl facade = new ComplianceReportFacadeImpl(repository);

  @Test
  void returnsTraceableAccessReport() {
    AuditRecordEntity entity = new AuditRecordEntity();
    entity.setId(UUID.randomUUID());
    entity.setSequenceNumber(7);
    entity.setActorId("user-1");
    entity.setResourceId("account-1");
    entity.setEventType("RECORD_ACCESS");
    entity.setEventTimestamp(Instant.parse("2026-01-01T00:00:00Z"));
    entity.setContentHash("hash-7");
    when(repository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(entity)));

    var result = facade.accessReport("user-1", "account-1", null, null, null, 0, 20);

    assertThat(result.getContent())
        .singleElement()
        .satisfies(
            report -> {
              assertThat(report.auditRecordId()).isEqualTo(entity.getId());
              assertThat(report.contentHash()).isEqualTo("hash-7");
            });
  }

  @Test
  void rejectsInvalidPageSize() {
    assertThatThrownBy(() -> facade.accessReport(null, null, null, null, null, 0, 101))
        .isInstanceOf(IllegalArgumentException.class);
    verifyNoInteractions(repository);
  }
}
