package com.app.hotelsaas.catin.infrastructure.persistence.mapper;

import com.app.hotelsaas.catin.domain.model.Ocupation;
import com.app.hotelsaas.catin.infrastructure.persistence.Entity.OcupationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OcupationEntityMapper {

    Ocupation toDomain(OcupationEntity entity);
    OcupationEntity toEntity(Ocupation domain);

    @Mapping(target = "id", ignore = true)
    OcupationEntity toEntityForCreate(Ocupation domain);

}
