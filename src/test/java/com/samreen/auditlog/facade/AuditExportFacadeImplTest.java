package com.samreen.auditlog.facade;

import com.samreen.auditlog.domain.model.AuditEvent;
import com.samreen.auditlog.domain.model.AuditRecord;
import com.samreen.auditlog.entity.AuditRecordEntity;
import com.samreen.auditlog.mapper.AuditRecordMapper;
import com.samreen.auditlog.repository.AuditRecordRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuditExportFacadeImplTest {
    private final AuditRecordRepository repository = mock(AuditRecordRepository.class);
    private final AuditRecordMapper mapper = mock(AuditRecordMapper.class);
    private final AuditExportFacadeImpl facade = new AuditExportFacadeImpl(repository, mapper);

    @Test
    void exportsOnlyMatchingResourceRecords() {
        UUID id = UUID.randomUUID();
        AuditRecordEntity entity = new AuditRecordEntity(); entity.setResourceId("account-1"); entity.setActorId("user-1");
        var domain = new AuditRecord(id, 3, new AuditEvent("RECORD_ACCESS", "user-1", "ACCOUNT", "account-1", Map.of("ok", true), Instant.now()), "prev", "hash");
        when(repository.findAllByOrderBySequenceNumberAsc()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        AuditExportBundle bundle = facade.export("account-1", null);

        assertThat(bundle.records()).hasSize(1);
        assertThat(bundle.firstSequence()).isEqualTo(3);
        assertThat(bundle.records().get(0).contentHash()).isEqualTo("hash");
    }

    @Test
    void requiresAtLeastOneExportCriterion() {
        assertThatThrownBy(() -> facade.export(null, " "))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(repository, mapper);
    }
}
