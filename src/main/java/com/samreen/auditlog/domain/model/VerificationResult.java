package com.samreen.auditlog.domain.model;

public record VerificationResult(
    boolean intact,
    long checkedRecords,
    String firstInvalidRecordId,
    ViolationType violationType,
    String message) {
  public enum ViolationType {
    NONE,
    GENESIS_MISMATCH,
    PREVIOUS_HASH_MISMATCH,
    CONTENT_HASH_MISMATCH,
    SEQUENCE_GAP
  }

  public static VerificationResult intact(long count) {
    return new VerificationResult(true, count, null, ViolationType.NONE, null);
  }
}
