package com.samreen.auditlog.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenServiceTest {
    private static final String SECRET = "test-secret-that-is-at-least-32-characters-long";

    @Test
    void issuesAndParsesTokenWithClaims() {
        JwtTokenService service = new JwtTokenService(SECRET, "issuer", "audience", Duration.ofMinutes(5));

        var claims = service.parse(service.issue("alice", List.of("AUDIT_READER")));

        assertThat(claims.getSubject()).isEqualTo("alice");
        assertThat(claims.getIssuer()).isEqualTo("issuer");
        assertThat(claims.getAudience()).contains("audience");
        assertThat(claims.get("roles", List.class)).containsExactly("AUDIT_READER");
    }

    @Test
    void rejectsTokenForDifferentAudience() {
        JwtTokenService service = new JwtTokenService(SECRET, "issuer", "audience", Duration.ofMinutes(5));
        JwtTokenService otherAudience = new JwtTokenService(SECRET, "issuer", "other-audience", Duration.ofMinutes(5));

        assertThatThrownBy(() -> otherAudience.parse(service.issue("alice", List.of())))
                .isInstanceOf(JwtException.class);
    }
}
