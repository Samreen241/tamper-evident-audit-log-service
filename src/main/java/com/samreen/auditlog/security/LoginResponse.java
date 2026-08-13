package com.samreen.auditlog.security;
public record LoginResponse(String accessToken,String tokenType,long expiresInSeconds) { }
