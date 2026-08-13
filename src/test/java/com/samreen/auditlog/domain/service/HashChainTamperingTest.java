package com.samreen.auditlog.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samreen.auditlog.domain.model.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

class HashChainTamperingTest {
    @Test void detectsChangedContent() {
        var hashing = new HashChainService(new CanonicalJsonService(new ObjectMapper()));
        var event = new AuditEvent("LOGIN", "actor", "USER", "id", Map.of("ok", true), Instant.parse("2026-08-13T00:00:00Z"));
        var draft = new AuditRecord(UUID.randomUUID(), 1, event, HashChainService.GENESIS_HASH, "");
        var valid = new AuditRecord(draft.id(), 1, event, draft.previousHash(), hashing.contentHash(draft));
        var changed = new AuditRecord(valid.id(), 1, new AuditEvent("LOGIN", "actor", "USER", "id", Map.of("ok", false), event.timestamp()), valid.previousHash(), valid.contentHash());
        var result = hashing.verify(List.of(changed));
        assertThat(result.intact()).isFalse();
        assertThat(result.violationType()).isEqualTo(VerificationResult.ViolationType.CONTENT_HASH_MISMATCH);
    }
}
