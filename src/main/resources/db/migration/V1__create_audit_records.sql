CREATE TABLE audit_records (
    id UUID NOT NULL,
    sequence_number BIGINT NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    actor_id VARCHAR(255) NOT NULL,
    resource_type VARCHAR(100) NOT NULL,
    resource_id VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    event_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    ingested_at TIMESTAMP WITH TIME ZONE NOT NULL,
    previous_hash VARCHAR(128) NOT NULL,
    content_hash VARCHAR(128) NOT NULL,
    status VARCHAR(32) NOT NULL,
    redaction_metadata TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_audit_records PRIMARY KEY (id),
    CONSTRAINT uq_audit_records_sequence UNIQUE (sequence_number),
    CONSTRAINT uq_audit_records_content_hash UNIQUE (content_hash),
    CONSTRAINT ck_audit_records_status CHECK (status IN ('ACTIVE', 'ARCHIVED', 'REDACTED'))
);

CREATE INDEX idx_audit_records_actor_id
    ON audit_records (actor_id);

CREATE INDEX idx_audit_records_event_type
    ON audit_records (event_type);

CREATE INDEX idx_audit_records_resource
    ON audit_records (resource_type, resource_id);

CREATE INDEX idx_audit_records_event_timestamp
    ON audit_records (event_timestamp);
