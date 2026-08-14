package com.samreen.auditlog.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Service;

@Service
public class CanonicalJsonService {
  private final ObjectMapper objectMapper;

  public CanonicalJsonService(ObjectMapper objectMapper) {
    this.objectMapper =
        objectMapper
            .copy()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public String canonicalize(Object value) {
    try {
      return objectMapper.writeValueAsString(sort(objectMapper.valueToTree(value)));
    } catch (Exception exception) {
      throw new IllegalArgumentException("Unable to canonicalize audit content", exception);
    }
  }

  private JsonNode sort(JsonNode node) {
    if (node.isObject()) {
      ObjectNode result = objectMapper.createObjectNode();
      Map<String, JsonNode> fields = new TreeMap<>();
      node.fields().forEachRemaining(entry -> fields.put(entry.getKey(), sort(entry.getValue())));
      fields.forEach(result::set);
      return result;
    }
    if (node.isArray()) node.forEach(this::sort);
    return node;
  }
}
