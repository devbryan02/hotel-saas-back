package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.OccupationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OccupationJpaRepository extends JpaRepository<OccupationEntity, UUID> {

    @Query("SELECT o FROM OccupationEntity o " +
            "JOIN FETCH o.room " +
            "JOIN FETCH o.client " +
            "WHERE o.tenant.id = :tenantId")
    List<OccupationEntity> findAllByTenantId(UUID tenantId);

    Optional<OccupationEntity> findByIdAndTenantId(UUID id, UUID tenantId);

}
