package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.Room;

import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {

    Room save(Room room);
    Optional<Room> findByIdAndTenantId(UUID id, UUID tenantId);
    Boolean existsByRoomNumberAndTenantId(String roomNumber, UUID tenantId);
    Boolean existsByRoomNumberAndTenantIdAndIdNot(String roomNumber, UUID tenantId, UUID roomId);

}
