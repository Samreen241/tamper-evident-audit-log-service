package com.samreen.auditlog.security;

import java.util.Set;

public record AuthenticatedUser(String username, Set<String> roles) {}
