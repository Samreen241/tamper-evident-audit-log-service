package com.samreen.auditlog.facade;
import org.springframework.data.domain.Page; import java.time.Instant;
public interface ComplianceReportFacade { Page<ComplianceAccessReport> accessReport(String actorId,String resourceId,String eventType,Instant from,Instant to,int page,int size); }
