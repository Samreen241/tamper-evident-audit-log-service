package com.samreen.auditlog.facade;
import com.samreen.auditlog.domain.model.VerificationResult;
public record AuditChainVerificationResponse(boolean intact,long checkedRecords,String firstInvalidRecordId,VerificationResult.ViolationType violationType,String message) { }
