package com.app.hotelsaas.catin.infrastructure.persistence.mapper;

import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.infrastructure.persistence.Entity.OccupationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OccupationEntityMapper {

    Occupation toDomain(OccupationEntity entity);
    OccupationEntity toEntity(Occupation domain);

    @Mapping(target = "id", ignore = true)
    OccupationEntity toEntityForCreate(Occupation domain);

}
