package com.samreen.auditlog.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samreen.auditlog.repository.AuditRecordRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedactionFacadeImpl implements RedactionFacade {
  private final AuditRecordRepository repository;
  private final ObjectMapper mapper;

  public RedactionFacadeImpl(AuditRecordRepository r) {
    this(r, new ObjectMapper());
  }

  @org.springframework.beans.factory.annotation.Autowired
  public RedactionFacadeImpl(AuditRecordRepository r, ObjectMapper mapper) {
    repository = r;
    this.mapper = mapper;
  }

  @Transactional
  public RedactionResponse redact(UUID id, RedactionRequest request) {
    if (request == null || request.jsonPaths() == null || request.jsonPaths().isEmpty())
      throw new IllegalArgumentException("At least one JSON path is required");
    var entity =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Audit event not found"));
    if ("REDACTED".equals(entity.getStatus()))
      throw new IllegalArgumentException("Audit event is already redacted");
    try {
      Map<String, Object> payload = mapper.readValue(entity.getPayload(), Map.class);
      Map<String, String> commitments = new LinkedHashMap<>();
      for (String path : request.jsonPaths()) {
        if (path == null || !path.startsWith("$.") || path.length() < 3)
          throw new IllegalArgumentException("Only top-level JSON paths are allowed");
        String key = path.substring(2);
        if (!payload.containsKey(key))
          throw new IllegalArgumentException("Path not found: " + path);
        Object original = payload.put(key, "[REDACTED]");
        commitments.put(path, sha256(mapper.writeValueAsString(original)));
      }
      entity.setPayload(mapper.writeValueAsString(payload));
      entity.setStatus("REDACTED");
      entity.setRedactionMetadata(
          mapper.writeValueAsString(
              Map.of(
                  "reason",
                  request.reason() == null ? "" : request.reason(),
                  "commitments",
                  commitments)));
      repository.save(entity);
      return new RedactionResponse(
          id, request.jsonPaths(), "REDACTED", sha256(entity.getRedactionMetadata()));
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Invalid audit payload JSON", e);
    }
  }

  private String sha256(String value) {
    try {
      byte[] bytes =
          MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
      StringBuilder out = new StringBuilder();
      for (byte b : bytes) out.append(String.format("%02x", b));
      return out.toString();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
