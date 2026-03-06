package com.app.hotelsaas.catin.infrastructure.persistence.repository.impl;

import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.mapper.OccupationEntityMapper;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.OccupationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OccupationRepositoryImpl implements OccupationRepository {

    private final OccupationEntityMapper mapper;
    private final OccupationJpaRepository jpa;

    @Override
    public Occupation save(Occupation occupation) {
        var entity = (occupation.getId() == null)
                ? mapper.toEntityForCreate(occupation)
                : mapper.toEntity(occupation);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public Page<Occupation> findAllByTenantId(UUID tenantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return jpa.findAllByTenantId(tenantId, pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<Occupation> findByTenantIdAndId(UUID tenantId, UUID occupationId) {
        return jpa.findByTenantIdAndId(tenantId, occupationId).map(mapper::toDomain);
    }

    @Override
    public List<Occupation> findActiveByTenantId(UUID tenantId) {
        return jpa.findActiveByTenantId(tenantId).stream().map(mapper::toDomain).toList();
    }

}
