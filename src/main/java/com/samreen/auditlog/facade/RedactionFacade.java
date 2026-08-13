package com.samreen.auditlog.facade;
import java.util.UUID;
public interface RedactionFacade { RedactionResponse redact(UUID eventId,RedactionRequest request); }
