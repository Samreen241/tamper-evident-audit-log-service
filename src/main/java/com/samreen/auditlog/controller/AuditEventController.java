package com.samreen.auditlog.controller;

import com.samreen.auditlog.dto.AuditEventResponse;
import com.samreen.auditlog.dto.CreateAuditEventRequest;
import com.samreen.auditlog.facade.AuditEventQuery;
import com.samreen.auditlog.facade.AuditLogFacade;
import com.samreen.auditlog.facade.CreateAuditEventCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditEventController {
  private final AuditLogFacade facade;

  public AuditEventController(AuditLogFacade facade) {
    this.facade = facade;
  }

  @PostMapping("/events")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Append an audit event",
      description = "Creates an immutable event in the audit hash chain.",
      security = @SecurityRequirement(name = "bearerAuth"))
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Event appended"),
    @ApiResponse(responseCode = "400", description = "Validation failure"),
    @ApiResponse(responseCode = "401", description = "Authentication required")
  })
  public AuditEventResponse append(
      @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
      @Valid @RequestBody CreateAuditEventRequest request) {
    var result =
        facade.appendEvent(
            new CreateAuditEventCommand(
                request.eventType(),
                request.actorId(),
                request.resourceType(),
                request.resourceId(),
                request.payload(),
                request.timestamp()),
            idempotencyKey == null || idempotencyKey.isBlank() ? null : idempotencyKey.trim());
    return new AuditEventResponse(
        result.id(),
        result.sequenceNumber(),
        result.eventType(),
        result.actorId(),
        result.resourceType(),
        result.resourceId(),
        result.payload(),
        result.timestamp(),
        result.previousHash(),
        result.contentHash());
  }

  @GetMapping("/events")
  @Operation(summary = "Query audit events", security = @SecurityRequirement(name = "bearerAuth"))
  public Page<AuditEventResponse> query(
      @RequestParam(required = false) String actorId,
      @RequestParam(required = false) String resourceType,
      @RequestParam(required = false) String resourceId,
      @RequestParam(required = false) String eventType,
      @RequestParam(required = false) Instant from,
      @RequestParam(required = false) Instant to,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "sequenceNumber,asc") String sort) {
    return facade.queryEvents(
        new AuditEventQuery(
            actorId, resourceType, resourceId, eventType, from, to, page, size, sort));
  }

  @GetMapping("/verify")
  @Operation(
      summary = "Verify the audit chain",
      description = "Walks all records and reports the first detected inconsistency.")
  public com.samreen.auditlog.facade.AuditChainVerificationResponse verify() {
    return facade.verifyChain();
  }
}
