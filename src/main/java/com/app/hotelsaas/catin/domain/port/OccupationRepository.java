package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.Occupation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OccupationRepository {

    Occupation save(Occupation occupation);
    List<Occupation> findAllByTenantId(UUID tenantId);
    Optional<Occupation> findByIdAndTenantId(UUID id, UUID tenantId);
    List<Occupation> findActiveByTenantId(UUID tenantId);

}
