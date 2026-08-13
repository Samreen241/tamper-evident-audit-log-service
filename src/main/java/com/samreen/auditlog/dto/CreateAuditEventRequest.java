package com.samreen.auditlog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Map;

@Schema(description = "Audit event submitted by a caller")
public record CreateAuditEventRequest(@NotBlank @Schema(example = "USER_LOGIN") String eventType,
                                      @NotBlank @Schema(example = "user-123") String actorId,
                                      @NotBlank @Schema(example = "USER") String resourceType,
                                      @NotBlank @Schema(example = "user-123") String resourceId,
                                      @NotNull @Schema(example = "{\"success\":true}") Map<String, Object> payload,
                                      @Schema(description = "Optional ISO-8601 timestamp") Instant timestamp) { }
