package com.app.hotelsaas.catin.infrastructure.persistence.mapper;

import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.infrastructure.persistence.Entity.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomEntityMapper {

    Room toDomain(RoomEntity entity);
    RoomEntity toEntity(Room domain);

}
