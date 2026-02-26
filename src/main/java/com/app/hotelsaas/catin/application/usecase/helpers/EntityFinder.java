package com.app.hotelsaas.catin.application.usecase.helpers;

import com.app.hotelsaas.catin.domain.exception.ClientNotFoundException;
import com.app.hotelsaas.catin.domain.exception.RoomNotFoundException;
import com.app.hotelsaas.catin.domain.exception.TenantNotFoundException;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import com.app.hotelsaas.catin.domain.port.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntityFinder {

    private final TenantRepository tenantRepository;
    private final RoomRepository roomRepository;
    private final ClientRepository clientRepository;

    public Tenant findTenant(UUID tenantId) {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> {
                    log.warn("Tenant not found: {}", tenantId);
                    return new TenantNotFoundException("Tenant not found");
                });
    }

    public Room findRoom(UUID roomId, UUID tenantId) {
        return roomRepository.findByIdAndTenantId(roomId, tenantId)
                .orElseThrow(() -> {
                    log.warn("Room {} not found for tenant {}", roomId, tenantId);
                    return new RoomNotFoundException("Room not found or not associated with tenant");
                });
    }

    public Client findClient(UUID clientId, UUID tenantId) {
        return clientRepository.findByIdAndTenantId(clientId, tenantId)
                .orElseThrow(() -> {
                    log.warn("Client {} not found for tenant {}", clientId, tenantId);
                    return new ClientNotFoundException("Client not found or not associated with tenant");
                });
    }
}