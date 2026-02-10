package com.app.hotelsaas.catin.infrastructure.persistence.mapper;

import com.app.hotelsaas.catin.domain.model.AppUser;
import com.app.hotelsaas.catin.infrastructure.persistence.Entity.AppUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppUserEntityMapper {

    AppUser toDomain(AppUserEntity entity);
    AppUserEntity toEntity(AppUser domain);

}
