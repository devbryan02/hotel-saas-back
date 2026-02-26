package com.app.hotelsaas.catin.infrastructure.persistence.repository.impl;

import com.app.hotelsaas.catin.domain.model.Ocupation;
import com.app.hotelsaas.catin.domain.port.OcupationRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.mapper.OcupationEntityMapper;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.OcupationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OcupationRepositoryImpl implements OcupationRepository {

    private final OcupationEntityMapper mapper;
    private final OcupationJpaRepository jpa;

    @Override
    public Ocupation save(Ocupation ocupation) {
        var entity = (ocupation.getId() == null)
                ? mapper.toEntityForCreate(ocupation)
                : mapper.toEntity(ocupation);
        return mapper.toDomain(jpa.save(entity));
    }
}
