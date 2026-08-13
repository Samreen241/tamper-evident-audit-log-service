package com.samreen.auditlog.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samreen.auditlog.domain.model.AuditEvent;
import com.samreen.auditlog.domain.model.AuditRecord;
import com.samreen.auditlog.entity.AuditRecordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface AuditRecordMapper {
    @Mapping(target = "event", expression = "java(toEvent(entity))")
    AuditRecord toDomain(AuditRecordEntity entity);

    @Mapping(target = "eventType", source = "event.eventType")
    @Mapping(target = "actorId", source = "event.actorId")
    @Mapping(target = "resourceType", source = "event.resourceType")
    @Mapping(target = "resourceId", source = "event.resourceId")
    @Mapping(target = "eventTimestamp", source = "event.timestamp")
    @Mapping(target = "payload", expression = "java(toJson(domain.event().payload()))")
    AuditRecordEntity toEntity(AuditRecord domain);

    default AuditEvent toEvent(AuditRecordEntity entity) {
        try {
            Map<String, Object> payload = new ObjectMapper().readValue(entity.getPayload(), Map.class);
            return new AuditEvent(entity.getEventType(), entity.getActorId(), entity.getResourceType(), entity.getResourceId(), payload, entity.getEventTimestamp());
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid audit payload JSON", exception);
        }
    }

    default String toJson(Map<String, Object> payload) {
        try { return new ObjectMapper().writeValueAsString(payload); }
        catch (JsonProcessingException exception) { throw new IllegalArgumentException("Unable to serialize audit payload", exception); }
    }
}
