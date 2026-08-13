package com.samreen.auditlog.controller;

import com.samreen.auditlog.dto.AuditEventResponse;
import com.samreen.auditlog.dto.CreateAuditEventRequest;
import com.samreen.auditlog.facade.AuditLogFacade;
import com.samreen.auditlog.facade.CreateAuditEventCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit/events")
public class AuditEventController {
    private final AuditLogFacade facade;
    public AuditEventController(AuditLogFacade facade) { this.facade = facade; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Append an audit event", description = "Creates an immutable event in the audit hash chain.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Event appended"), @ApiResponse(responseCode = "400", description = "Validation failure"), @ApiResponse(responseCode = "401", description = "Authentication required")})
    public AuditEventResponse append(@Valid @RequestBody CreateAuditEventRequest request) {
        var result = facade.appendEvent(new CreateAuditEventCommand(request.eventType(), request.actorId(), request.resourceType(), request.resourceId(), request.payload(), request.timestamp()));
        return new AuditEventResponse(result.id(), result.sequenceNumber(), result.eventType(), result.actorId(), result.resourceType(), result.resourceId(), result.payload(), result.timestamp(), result.previousHash(), result.contentHash());
    }
}
