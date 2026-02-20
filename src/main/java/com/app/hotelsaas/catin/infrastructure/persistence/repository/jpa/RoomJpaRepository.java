package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, UUID> {

    boolean existsByRoomNumberAndTenantId(String roomNumber, UUID tenantId);

}
