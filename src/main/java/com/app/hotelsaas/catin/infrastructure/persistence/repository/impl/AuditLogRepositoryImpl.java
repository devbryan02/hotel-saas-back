package com.app.hotelsaas.catin.infrastructure.persistence.repository.impl;

import com.app.hotelsaas.catin.domain.model.AuditLog;
import com.app.hotelsaas.catin.domain.port.AuditLogRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.mapper.AuditLogEntityMapper;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.AuditLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuditLogRepositoryImpl implements AuditLogRepository {

    private final AuditLogJpaRepository jpa;
    private final AuditLogEntityMapper mapper;

    @Override
    public AuditLog save(AuditLog auditLog) {
        return mapper.toDomain(jpa.save(mapper.toEntity(auditLog)));
    }
}
