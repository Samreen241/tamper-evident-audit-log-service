package com.samreen.auditlog.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samreen.auditlog.domain.model.AuditEvent;
import com.samreen.auditlog.domain.model.AuditRecord;
import com.samreen.auditlog.domain.model.VerificationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class HashChainServiceTest {
    private HashChainService service;
    private Instant timestamp;

    @BeforeEach
    void setUp() { service = new HashChainService(new CanonicalJsonService(new ObjectMapper())); timestamp = Instant.parse("2026-08-13T10:15:30Z"); }

    @Test
    void sameInputProducesSameHash() {
        AuditRecord record = record(1, event(Map.of("b", 2, "a", 1)), HashChainService.GENESIS_HASH, "");
        assertThat(service.contentHash(record)).isEqualTo(service.contentHash(record));
    }

    @Test
    void differentPayloadProducesDifferentHash() {
        AuditRecord first = record(1, event(Map.of("value", "one")), HashChainService.GENESIS_HASH, "");
        AuditRecord second = record(1, event(Map.of("value", "two")), HashChainService.GENESIS_HASH, "");
        assertThat(service.contentHash(first)).isNotEqualTo(service.contentHash(second));
    }

    @Test
    void firstRecordUsesGenesisAndSecondReferencesFirst() {
        AuditRecord firstWithoutHash = record(1, event(Map.of("value", 1)), HashChainService.GENESIS_HASH, "");
        AuditRecord first = record(1, firstWithoutHash.event(), HashChainService.GENESIS_HASH, service.contentHash(firstWithoutHash));
        AuditRecord secondWithoutHash = record(2, event(Map.of("value", 2)), first.contentHash(), "");
        AuditRecord second = record(2, secondWithoutHash.event(), first.contentHash(), service.contentHash(secondWithoutHash));
        assertThat(service.verify(List.of(first, second)).intact()).isTrue();
    }

    @Test
    void nullAndEmptyFieldsAreHandledDeterministically() {
        AuditEvent event = new AuditEvent("LOGIN", "actor", "USER", "id", Map.of(), timestamp);
        AuditRecord record = record(1, event, HashChainService.GENESIS_HASH, "");
        assertThat(service.contentHash(record)).hasSize(64);
    }

    @Test
    void timestampIsNormalizedAsInstant() {
        AuditEvent first = new AuditEvent("LOGIN", "actor", "USER", "id", Map.of(), Instant.parse("2026-08-13T10:15:30Z"));
        AuditEvent second = new AuditEvent("LOGIN", "actor", "USER", "id", Map.of(), Instant.parse("2026-08-13T12:15:30+02:00"));
        assertThat(service.contentHash(record(1, first, HashChainService.GENESIS_HASH, "")))
                .isEqualTo(service.contentHash(record(1, second, HashChainService.GENESIS_HASH, "")));
    }

    private AuditEvent event(Map<String, Object> payload) { return new AuditEvent("LOGIN", "actor", "USER", "id", payload, timestamp); }
    private AuditRecord record(long sequence, AuditEvent event, String previous, String hash) { return new AuditRecord(UUID.randomUUID(), sequence, event, previous, hash); }
}
