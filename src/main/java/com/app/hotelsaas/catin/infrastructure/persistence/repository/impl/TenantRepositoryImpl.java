package com.app.hotelsaas.catin.infrastructure.persistence.repository.impl;

import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.TenantRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.mapper.TenantEntityMapper;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.TenantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TenantRepositoryImpl implements TenantRepository {

    private final TenantJpaRepository jpa;
    private final TenantEntityMapper mapper;

    @Override
    public Optional<Tenant> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }
}
