package com.app.hotelsaas.catin.application.usecase.client;

import com.app.hotelsaas.catin.domain.exception.ClientNotFoundException;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.web.rest.client.request.UpdateClientRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateClientUseCase {

    private final ClientRepository clientRepository;

    /**
     * Updates client with request data; persists changes
     */
    @Transactional
    public Client execute(UUID clientId, UUID tenantId, UpdateClientRequest request){

        Client client = clientRepository.findByIdAndTenantId(clientId, tenantId)
                .orElseThrow(() -> {
                    log.warn("Client not found or not associated with tenant");
                    return new ClientNotFoundException(
                            "Client not found or not associated with tenant");
                });

        Client updated = client.update(request.fullName(), request.email(), request.phone());

        return clientRepository.save(updated);
    }
}
