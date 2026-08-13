package com.samreen.auditlog.domain.service;

import com.samreen.auditlog.domain.model.AuditRecord;
import com.samreen.auditlog.domain.model.VerificationResult;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

@Service
public class HashChainService {
    public static final String GENESIS_HASH = "GENESIS-AUDIT-LOG-V1";
    private final CanonicalJsonService canonicalJsonService;
    public HashChainService(CanonicalJsonService canonicalJsonService) { this.canonicalJsonService = canonicalJsonService; }
    public String contentHash(AuditRecord record) { return sha256(canonicalJsonService.canonicalize(record.hashContent())); }
    public VerificationResult verify(List<AuditRecord> records) {
        String previous = GENESIS_HASH; long sequence = 1;
        for (int index = 0; index < records.size(); index++) {
            AuditRecord record = records.get(index);
            if (record.sequenceNumber() != sequence) return invalid(records, index, VerificationResult.ViolationType.SEQUENCE_GAP, "Unexpected sequence number");
            if (!previous.equals(record.previousHash())) return invalid(records, index, VerificationResult.ViolationType.PREVIOUS_HASH_MISMATCH, "Previous hash mismatch");
            if (!contentHash(record).equals(record.contentHash())) return invalid(records, index, VerificationResult.ViolationType.CONTENT_HASH_MISMATCH, "Content hash mismatch");
            previous = record.contentHash(); sequence++;
        }
        return VerificationResult.intact(records.size());
    }
    private VerificationResult invalid(List<AuditRecord> records, int index, VerificationResult.ViolationType type, String message) {
        return new VerificationResult(false, index, records.get(index).id().toString(), type, message);
    }
    private String sha256(String value) {
        try {
            byte[] bytes = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte item : bytes) result.append(String.format("%02x", item));
            return result.toString();
        } catch (Exception exception) { throw new IllegalStateException("SHA-256 is unavailable", exception); }
    }
}
