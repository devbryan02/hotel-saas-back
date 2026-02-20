package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {

    Boolean existsByDocumentAndTenantId(String document, UUID tenantId);
    List<ClientEntity> findAllByTenantId(UUID tenantId);
    Optional<ClientEntity> findByIdAndTenantId(UUID id, UUID tenantId);
}
