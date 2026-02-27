package com.app.hotelsaas.catin.infrastructure.persistence.repository.impl;

import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.mapper.OccupationEntityMapper;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.OccupationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
