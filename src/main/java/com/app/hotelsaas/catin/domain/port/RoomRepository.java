package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.Room;

import java.util.UUID;

public interface RoomRepository {

    Room save(Room room);
    Boolean existsByRoomNumberAndTenantId(String roomNumber, UUID tenantId);

}
