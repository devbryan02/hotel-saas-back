package com.app.hotelsaas.catin.infrastructure.persistence.repository.impl;

import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.mapper.RoomEntityMapper;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.RoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository jpa;
    private final RoomEntityMapper mapper;

    @Override
    public Room save(Room room) {
        var entity = (room.getId() == null)
                ? mapper.toEntityForCreate(room)
                : mapper.toEntity(room);
        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public Optional<Room> findByIdAndTenantId(UUID id, UUID tenantId) {
        return jpa.findByIdAndTenantId(id, tenantId)
                .map(mapper::toDomain);
    }

    @Override
    public Boolean existsByRoomNumberAndTenantId(String roomNumber, UUID tenantId) {
        return jpa.existsByRoomNumberAndTenantId(roomNumber,tenantId);
    }
}
