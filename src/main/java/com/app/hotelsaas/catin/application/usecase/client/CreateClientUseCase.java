package com.app.hotelsaas.catin.application.usecase.client;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.exception.DuplicateClientException;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.infrastructure.metrics.HotelMetrics;
import com.app.hotelsaas.catin.web.rest.client.request.CreateClientRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateClientUseCase {

    private final ClientRepository clientRepository;
    private final EntityFinder entityFinder;
    private final HotelMetrics hotelMetrics;

    /**
     * Executes transactional client creation; persists and returns result
     */
    @Transactional
    public Client execute(UUID tenantId, CreateClientRequest request){

        Tenant tenant = entityFinder.findTenant(tenantId);

        if(clientRepository.existsByDocumentAndTenantId(request.document(), tenantId)){
            log.warn("Client with document {} already exists", request.document());
            throw new DuplicateClientException("Client with document "+request.document()+" already exists");
        }

        Client client = Client.create(
                tenant,
                request.fullName(),
                request.document(),
                request.email(),
                request.phone()
        );

        Client clientSaved = clientRepository.save(client);
        log.info("Client created: {}", clientSaved);

        // METRICS
        hotelMetrics.recordNewClient();

        return clientSaved;
    }
}
