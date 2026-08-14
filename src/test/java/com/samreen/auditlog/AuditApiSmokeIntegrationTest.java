package com.samreen.auditlog;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
