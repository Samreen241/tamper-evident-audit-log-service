package com.samreen.auditlog.facade;

import java.util.Set;
import java.util.UUID;

public record RedactionResponse(
    UUID eventId, Set<String> paths, String status, String commitment) {}
