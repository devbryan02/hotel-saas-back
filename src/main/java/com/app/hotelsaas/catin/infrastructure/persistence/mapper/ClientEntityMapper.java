package com.app.hotelsaas.catin.infrastructure.persistence.mapper;

import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.infrastructure.persistence.Entity.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientEntityMapper {

    Client toDomain(ClientEntity entity);
    ClientEntity toEntity(Client domain);

    @Mapping(target = "id", ignore = true)
    ClientEntity toEntityForCreate(Client domain);

}
