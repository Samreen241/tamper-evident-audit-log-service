package com.samreen.auditlog.controller;

import com.samreen.auditlog.facade.RedactionFacade;
import com.samreen.auditlog.facade.RedactionRequest;
import com.samreen.auditlog.facade.RedactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit/events/{eventId}/redactions")
public class RedactionController {
    private final RedactionFacade facade;
    public RedactionController(RedactionFacade facade) { this.facade = facade; }
    @PostMapping
    @Operation(summary = "Redact approved sensitive fields", security = @SecurityRequirement(name = "bearerAuth"))
    public RedactionResponse redact(@PathVariable UUID eventId, @Valid @RequestBody RedactionRequest request) { return facade.redact(eventId, request); }
}
