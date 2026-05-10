package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.AuditLog;

public interface AuditLogRepository {

    AuditLog save(AuditLog auditLog);

}
