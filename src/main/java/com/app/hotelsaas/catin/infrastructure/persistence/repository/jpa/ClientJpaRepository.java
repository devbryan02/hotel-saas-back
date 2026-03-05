package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {

    boolean existsByDocumentAndTenantId(String document, UUID tenantId);
    List<ClientEntity> findAllByTenantId(UUID tenantId);
    Optional<ClientEntity> findByTenantIdAndId(UUID tenantId, UUID clientId);

    @Query("""
    SELECT c FROM ClientEntity c
    WHERE c.tenant.id = :tenantId
    AND (
        LOWER(c.document) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
    )
    """)
    List<ClientEntity> searchByTenantIdAndQuery(
            @Param("tenantId") UUID tenantId,
            @Param("query") String query
    );

    @Query("""
    SELECT c FROM ClientEntity c
    WHERE c.tenant.id = :tenantId
    AND (:query IS NULL OR
        LOWER(c.document) LIKE LOWER(CONCAT('%', :query, '%'))
        OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%'))
    )
    AND (:status IS NULL OR c.status = :status)
    """)
    List<ClientEntity> searchByTenantIdAndQueryAndStatus(
            @Param("tenantId") UUID tenantId,
            @Param("query") String query,
            @Param("status") String status
    );

}
