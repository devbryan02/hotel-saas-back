package com.app.hotelsaas.catin.infrastructure.persistence.mapper;

import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.infrastructure.persistence.Entity.TenantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TenantEntityMapper {

    Tenant toDomain(TenantEntity entity);
    TenantEntity toEntity(Tenant domain);

}
