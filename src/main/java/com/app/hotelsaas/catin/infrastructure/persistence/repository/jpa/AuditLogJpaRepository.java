package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, UUID> {
}
