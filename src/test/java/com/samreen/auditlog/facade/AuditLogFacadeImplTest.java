package com.samreen.auditlog.facade;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AuditLogFacadeImplTest {
  @Test
  void rejectsInvalidAppendBeforeCallingService() {
    var service = mock(AuditLogCommandService.class);
    var facade = new AuditLogFacadeImpl(service);
    assertThatThrownBy(
            () ->
                facade.appendEvent(
                    new CreateAuditEventCommand("", "actor", "USER", "id", Map.of(), null)))
        .isInstanceOf(IllegalArgumentException.class);
    verifyNoInteractions(service);
  }

  @Test
  void rejectsOversizedQueryPage() {
    var service = mock(AuditLogCommandService.class);
    var facade = new AuditLogFacadeImpl(service);
    assertThatThrownBy(
            () ->
                facade.queryEvents(
                    new AuditEventQuery(
                        null, null, null, null, null, null, 0, 101, "sequenceNumber")))
        .isInstanceOf(IllegalArgumentException.class);
    verifyNoInteractions(service);
  }
}
