package com.samreen.auditlog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** A bounded single-node safeguard; use a gateway for distributed production rate limiting. */
@Component
public class RequestRateLimitFilter extends OncePerRequestFilter {
  private record Window(Instant started, AtomicInteger count) {}

  private final int loginLimit;
  private final int writeLimit;
  private final Duration window;
  private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

  public RequestRateLimitFilter(
      @Value("${app.security.rate-limit.login-per-window:10}") int loginLimit,
      @Value("${app.security.rate-limit.write-per-window:120}") int writeLimit,
      @Value("${app.security.rate-limit.window:PT1M}") Duration window) {
    if (loginLimit < 1 || writeLimit < 1 || window.isZero() || window.isNegative())
      throw new IllegalArgumentException("Rate-limit settings must be positive");
    this.loginLimit = loginLimit;
    this.writeLimit = writeLimit;
    this.window = window;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String path = request.getRequestURI();
    int limit = path.equals("/api/v1/auth/login") ? loginLimit : -1;
    if (limit < 0 && request.getMethod().equals("POST") && path.startsWith("/api/v1/audit/"))
      limit = writeLimit;
    if (limit < 0) {
      chain.doFilter(request, response);
      return;
    }
    String key = request.getRemoteAddr() + ":" + path;
    Window current = windows.compute(key, (ignored, old) -> next(old));
    if (current.count().incrementAndGet() > limit) {
      response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
      response.setHeader("Retry-After", Long.toString(window.toSeconds()));
      response.setContentType("application/json");
      response.getWriter().write("{\"error\":\"rate limit exceeded\"}");
      return;
    }
    chain.doFilter(request, response);
  }

  private Window next(Window old) {
    Instant now = Instant.now();
    return old == null || old.started().plus(window).isBefore(now)
        ? new Window(now, new AtomicInteger())
        : old;
  }
}
