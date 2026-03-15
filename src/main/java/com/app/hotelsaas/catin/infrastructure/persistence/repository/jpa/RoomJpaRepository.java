package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomJpaRepository extends JpaRepository<RoomEntity, UUID> {

    boolean existsByRoomNumberAndTenantId(String roomNumber, UUID tenantId);
    boolean existsByRoomNumberAndTenantIdAndIdNot(String roomNumber, UUID tenantId, UUID roomId);
    Optional<RoomEntity> findByTenantIdAndId(UUID tenantId, UUID roomId);
    List<RoomEntity> findAllByTenantId(UUID tenantId);
    long countByTenantId(UUID tenantId);
    long countByTenantIdAndStatus(UUID tenantId, String status);

}
