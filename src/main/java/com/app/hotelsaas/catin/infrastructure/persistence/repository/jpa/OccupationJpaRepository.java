package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.OccupationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OccupationJpaRepository extends JpaRepository<OccupationEntity, UUID> {

    @Query("SELECT o FROM OccupationEntity o WHERE o.tenant.id = :tenantId")
    @EntityGraph(attributePaths = {"room", "client"}) // ← resuelve N+1 ✅
    Page<OccupationEntity> findAllByTenantId(
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    Optional<OccupationEntity> findByTenantIdAndId(UUID tenantId, UUID occupationId);

    @Query("SELECT o FROM OccupationEntity o " +
            "JOIN FETCH o.room " +
            "JOIN FETCH o.client " +
            "WHERE o.tenant.id = :tenantId " +
            "AND o.status = 'ACTIVE'")
    List<OccupationEntity> findActiveByTenantId(@Param("tenantId") UUID tenantId);

}
