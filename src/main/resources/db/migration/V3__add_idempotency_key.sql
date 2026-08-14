ALTER TABLE audit_records ADD COLUMN IF NOT EXISTS idempotency_key VARCHAR(128);
CREATE UNIQUE INDEX IF NOT EXISTS uq_audit_records_idempotency_key
    ON audit_records (idempotency_key);
