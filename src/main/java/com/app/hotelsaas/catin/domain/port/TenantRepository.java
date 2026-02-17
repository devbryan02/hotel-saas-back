package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.Tenant;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository {

    Optional<Tenant> findById(UUID id);

}
