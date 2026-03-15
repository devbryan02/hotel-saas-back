package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.Room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {

    Room save(Room room);
    Optional<Room> findByTenantIdAndId(UUID tenantId, UUID roomId);
    List<Room> findAllByTenantId(UUID tenantId);
    boolean existsByRoomNumberAndTenantId(String roomNumber, UUID tenantId);
    boolean existsByRoomNumberAndTenantIdAndIdNot(String roomNumber, UUID tenantId, UUID roomId);
}
