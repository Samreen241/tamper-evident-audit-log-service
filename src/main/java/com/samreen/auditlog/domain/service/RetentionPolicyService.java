package com.samreen.auditlog.domain.service;

import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RetentionPolicyService {
  private final Duration window;

  public RetentionPolicyService(@Value("${app.retention.window:P365D}") Duration window) {
    if (window.isZero() || window.isNegative())
      throw new IllegalArgumentException("Retention window must be positive");
    this.window = window;
  }

  public Instant cutoff(Instant now) {
    return now.minus(window);
  }
}
