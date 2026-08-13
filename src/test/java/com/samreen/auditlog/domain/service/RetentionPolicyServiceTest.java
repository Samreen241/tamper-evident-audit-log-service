package com.samreen.auditlog.domain.service;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RetentionPolicyServiceTest {
    @Test void calculatesCutoffFromConfiguredWindow() {
        var service = new RetentionPolicyService(Duration.ofDays(30));
        assertThat(service.cutoff(Instant.parse("2026-08-13T00:00:00Z"))).isEqualTo(Instant.parse("2026-07-14T00:00:00Z"));
    }
    @Test void rejectsNonPositiveWindow() {
        assertThatThrownBy(() -> new RetentionPolicyService(Duration.ZERO)).isInstanceOf(IllegalArgumentException.class);
    }
}
