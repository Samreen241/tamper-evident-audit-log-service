package com.samreen.auditlog;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuditApiSmokeIntegrationTest {
  @Autowired MockMvc mockMvc;

  @Test
  void swaggerIsPubliclyAvailable() throws Exception {
    mockMvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
  }

  @Test
  void loginReturnsJwtForConfiguredLocalUser() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"writer\",\"password\":\"local-writer-password\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").isString())
        .andExpect(jsonPath("$.tokenType").value("Bearer"));
  }

  @Test
  void queryRequiresAuthentication() throws Exception {
    mockMvc.perform(get("/api/v1/audit/events")).andExpect(status().isUnauthorized());
  }

  @Test
  void h2ConsoleIsNotPubliclyAccessible() throws Exception {
    mockMvc.perform(get("/h2-console")).andExpect(status().isUnauthorized());
  }

  @Test
  void writerCannotVerifyChain() throws Exception {
    String token = login("writer", "local-writer-password");
    mockMvc
        .perform(get("/api/v1/audit/verify").header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }

  @Test
  void adminCanVerifyChain() throws Exception {
    String token = login("admin", "local-admin-password");
    mockMvc
        .perform(get("/api/v1/audit/verify").header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void invalidCredentialsAreRejected() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"writer\",\"password\":\"wrong\"}"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void malformedAuditEventIsRejected() throws Exception {
    String token = login("writer", "local-writer-password");
    mockMvc
        .perform(
            post("/api/v1/audit/events")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content("{\"eventType\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void oversizedPageIsRejected() throws Exception {
    String token = login("admin", "local-admin-password");
    mockMvc
        .perform(
            get("/api/v1/audit/events")
                .param("size", "101")
                .header("Authorization", "Bearer " + token))
        .andExpect(status().isBadRequest());
  }

  @Test
  void idempotencyKeyReturnsTheOriginalEvent() throws Exception {
    String token = login("writer", "local-writer-password");
    String event =
        "{\"eventType\":\"LOGIN\",\"actorId\":\"u1\",\"resourceType\":\"USER\","
            + "\"resourceId\":\"u1\",\"payload\":{\"ok\":true},"
            + "\"timestamp\":\"2026-08-14T00:00:00Z\"}";
    var first =
        mockMvc
            .perform(
                post("/api/v1/audit/events")
                    .header("Authorization", "Bearer " + token)
                    .header("Idempotency-Key", "smoke-key-1")
                    .contentType("application/json")
                    .content(event))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();
    mockMvc
        .perform(
            post("/api/v1/audit/events")
                .header("Authorization", "Bearer " + token)
                .header("Idempotency-Key", "smoke-key-1")
                .contentType("application/json")
                .content(event))
        .andExpect(status().isCreated())
        .andExpect(content().json(first));
  }

  @Test
  void writerCannotUseAdministrativeReports() throws Exception {
    String token = login("writer", "local-writer-password");
    mockMvc
        .perform(get("/api/v1/audit/export").header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
    mockMvc
        .perform(
            get("/api/v1/audit/compliance/access-report")
                .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden());
  }

  private String login(String username, String password) throws Exception {
    return mockMvc
        .perform(
            post("/api/v1/auth/login")
                .contentType("application/json")
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString()
        .replaceAll(".*\\\"accessToken\\\":\\\"([^\\\"]+)\\\".*", "$1");
  }
}
