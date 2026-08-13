package com.samreen.auditlog.facade;
import java.util.Set;
public record RedactionRequest(Set<String> jsonPaths,String reason) { }
