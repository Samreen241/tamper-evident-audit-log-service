package com.samreen.auditlog.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  @Bean
  SecurityFilterChain chain(HttpSecurity h, JwtAuthenticationFilter f) throws Exception {
    return h.csrf(c -> c.disable())
        .cors(c -> {})
        .exceptionHandling(
            e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            a ->
                a.requestMatchers(
                        "/api/v1/auth/login",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/audit/events")
                    .hasRole("AUDIT_WRITER")
                    .requestMatchers(HttpMethod.GET, "/api/v1/audit/events")
                    .hasAnyRole("AUDIT_READER", "AUDIT_ADMIN")
                    .requestMatchers("/api/v1/audit/verify")
                    .hasRole("AUDIT_ADMIN")
                    .requestMatchers("/api/v1/audit/export")
                    .hasAnyRole("AUDIT_ADMIN", "COMPLIANCE_REVIEWER")
                    .requestMatchers("/api/v1/audit/compliance/**")
                    .hasRole("COMPLIANCE_REVIEWER")
                    .requestMatchers(
                        "/api/v1/audit/retention/**", "/api/v1/audit/events/*/redactions/**")
                    .hasRole("AUDIT_ADMIN")
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(f, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource(
      @Value("${app.security.allowed-origins:}") String allowedOrigins) {
    var config = new CorsConfiguration();
    config.setAllowedOrigins(
        allowedOrigins.isBlank() ? List.of() : List.of(allowedOrigins.split(",")));
    config.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
    config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Idempotency-Key"));
    config.setMaxAge(3600L);
    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  UserDetailsService users(
      PasswordEncoder e,
      @Value("${app.security.writer-password}") String w,
      @Value("${app.security.admin-password}") String a) {
    return new InMemoryUserDetailsManager(
        User.withUsername("writer").password(e.encode(w)).roles("AUDIT_WRITER").build(),
        User.withUsername("admin")
            .password(e.encode(a))
            .roles("AUDIT_ADMIN", "AUDIT_READER")
            .build());
  }

  @Bean
  org.springframework.security.authentication.AuthenticationManager authenticationManager(
      org.springframework.security.config.annotation.authentication.configuration
              .AuthenticationConfiguration
          c)
      throws Exception {
    return c.getAuthenticationManager();
  }
}
