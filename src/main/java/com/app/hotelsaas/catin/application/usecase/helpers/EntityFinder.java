package com.app.hotelsaas.catin.application.usecase.helpers;

import com.app.hotelsaas.catin.domain.exception.ClientNotFoundException;
import com.app.hotelsaas.catin.domain.exception.OccupationNotFoundException;
import com.app.hotelsaas.catin.domain.exception.RoomNotFoundException;
import com.app.hotelsaas.catin.domain.exception.TenantNotFoundException;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import com.app.hotelsaas.catin.domain.port.TenantRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntityFinder {

    private final TenantRepository tenantRepository;
    private final RoomRepository roomRepository;
    private final ClientRepository clientRepository;
    private final OccupationRepository occupationRepository;

    public Tenant findTenant(UUID tenantId) {
        return tenantRepository
            .findById(tenantId)
            .orElseThrow(() -> {
                log.warn("Tenant not found: {}", tenantId);
                return new TenantNotFoundException("Tenant not found");
            });
    }

    public Room findRoom(UUID tenantId, UUID roomId) {
        return roomRepository
            .findByTenantIdAndId(tenantId, roomId)
            .orElseThrow(() -> {
                log.warn("Room {} not found for tenant {}", roomId, tenantId);
                return new RoomNotFoundException(
                    "Room not found or not associated with tenant"
                );
            });
    }

    public Client findClient(UUID tenantId, UUID clientId) {
        return clientRepository
            .findByTenantIdAndId(tenantId, clientId)
            .orElseThrow(() -> {
                log.warn(
                    "Client {} not found for tenant {}",
                    clientId,
                    tenantId
                );
                return new ClientNotFoundException(
                    "Client not found or not associated with tenant"
                );
            });
    }

    public Occupation findOccupation(UUID tenantId, UUID occupationId) {
        return occupationRepository
            .findByTenantIdAndId(tenantId, occupationId)
            .orElseThrow(() -> {
                log.warn(
                    "Occupation {} not found for tenant {}",
                    occupationId,
                    tenantId
                );
                return new OccupationNotFoundException(
                    "Occupation not found or not associated with tenant"
                );
            });
    }
}
