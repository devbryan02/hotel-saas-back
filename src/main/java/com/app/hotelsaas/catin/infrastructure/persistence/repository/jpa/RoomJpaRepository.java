package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, UUID> {

    // RoomRepository port
    Boolean existsByRoomNumberAndTenantId(String roomNumber, UUID tenantId);
    Boolean existsByRoomNumberAndTenantIdAndIdNot(String roomNumber, UUID tenantId, UUID roomId);
    Optional<RoomEntity> findByIdAndTenantId(UUID id, UUID tenantId);

}
