package com.samreen.auditlog.controller;

import com.samreen.auditlog.facade.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit/export")
public class AuditExportController {
  private final AuditExportFacade facade;

  public AuditExportController(AuditExportFacade f) {
    facade = f;
  }

  @GetMapping
  @Operation(
      summary = "Export verifiable audit records",
      security = @SecurityRequirement(name = "bearerAuth"))
  public AuditExportBundle export(
      @RequestParam(required = false) String resourceId,
      @RequestParam(required = false) String actorId) {
    return facade.export(resourceId, actorId);
  }
}
