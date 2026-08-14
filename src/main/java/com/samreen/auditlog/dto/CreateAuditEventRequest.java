package com.samreen.auditlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.Map;

@Schema(description = "Audit event submitted by a caller")
public record CreateAuditEventRequest(
    @NotBlank @Size(max = 100) @Schema(example = "USER_LOGIN") String eventType,
    @NotBlank @Size(max = 255) @Schema(example = "user-123") String actorId,
    @NotBlank @Size(max = 100) @Schema(example = "USER") String resourceType,
    @NotBlank @Size(max = 255) @Schema(example = "user-123") String resourceId,
    @NotNull @Size(max = 100) @Schema(example = "{\"success\":true}") Map<String, Object> payload,
    @Schema(description = "Optional ISO-8601 timestamp") Instant timestamp) {}
