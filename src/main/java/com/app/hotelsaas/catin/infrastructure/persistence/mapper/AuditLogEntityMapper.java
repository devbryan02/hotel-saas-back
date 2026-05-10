package com.app.hotelsaas.catin.infrastructure.persistence.mapper;

import com.app.hotelsaas.catin.domain.model.AuditLog;
import com.app.hotelsaas.catin.infrastructure.persistence.Entity.AuditLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuditLogEntityMapper {

    AuditLog toDomain(AuditLogEntity entity);
    AuditLogEntity toEntity(AuditLog domain);

}
