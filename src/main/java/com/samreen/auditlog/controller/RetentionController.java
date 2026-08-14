package com.samreen.auditlog.controller;

import com.samreen.auditlog.facade.RetentionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit/retention")
public class RetentionController {
    private final RetentionFacade facade;
    public RetentionController(RetentionFacade facade) { this.facade = facade; }

    @PostMapping("/archive-expired")
    @Operation(summary = "Archive expired audit events", security = @SecurityRequirement(name = "bearerAuth"))
    public int archiveExpired() { return facade.archiveExpired(); }
}
