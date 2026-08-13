package com.samreen.auditlog.domain.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
@Service public class RetentionPolicyService {
 private final Duration window;
 public RetentionPolicyService(@Value("${app.retention.window:P365D}") Duration window){if(window.isZero()||window.isNegative())throw new IllegalArgumentException("Retention window must be positive");this.window=window;}
 public Instant cutoff(Instant now){return now.minus(window);}
}
