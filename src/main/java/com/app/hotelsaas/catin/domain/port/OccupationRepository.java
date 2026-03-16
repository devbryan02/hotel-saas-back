package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.Occupation;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OccupationRepository {

    Occupation save(Occupation occupation);
    Page<Occupation> findAllByTenantId(UUID tenantId, int page, int size);
    Page<Occupation> findAllByTenantIdAndStatus(UUID tenantId, String status, int page, int size);
    Optional<Occupation> findByTenantIdAndId(UUID tenantId, UUID occupationId);
    List<Occupation> findActiveByTenantId(UUID tenantId);
    List<Occupation> findActiveByCheckOutDateLessThanEqual(LocalDate date);

}
