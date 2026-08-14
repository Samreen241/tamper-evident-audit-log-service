package com.samreen.auditlog.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.samreen.auditlog.repository.AuditRecordRepository;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RedactionFacadeImplTest {
  private final AuditRecordRepository repository = mock(AuditRecordRepository.class);
  private final RedactionFacadeImpl facade = new RedactionFacadeImpl(repository);

  @Test
  void acceptsARequestedRedactionForExistingEvent() {
    UUID id = UUID.randomUUID();
    var entity = new com.samreen.auditlog.entity.AuditRecordEntity();
    entity.setPayload("{\"accountNumber\":\"1234\",\"safe\":true}");
    entity.setStatus("ACTIVE");
    when(repository.findById(id)).thenReturn(java.util.Optional.of(entity));

    RedactionResponse response =
        facade.redact(id, new RedactionRequest(Set.of("$.accountNumber"), "privacy"));

    assertThat(response.status()).isEqualTo("REDACTED");
    assertThat(response.paths()).containsExactly("$.accountNumber");
  }

  @Test
  void rejectsEmptyPathsAndUnknownEvent() {
    UUID id = UUID.randomUUID();
    assertThatThrownBy(() -> facade.redact(id, new RedactionRequest(Set.of(), "reason")))
        .isInstanceOf(IllegalArgumentException.class);
    when(repository.findById(id)).thenReturn(java.util.Optional.empty());
    assertThatThrownBy(() -> facade.redact(id, new RedactionRequest(Set.of("$.secret"), "reason")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Audit event not found");
  }
}
