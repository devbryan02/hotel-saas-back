package com.app.hotelsaas.catin.application.usecase.client;

import com.app.hotelsaas.catin.domain.exception.DuplicateClientException;
import com.app.hotelsaas.catin.domain.exception.TenantNotFoundException;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.domain.port.TenantRepository;
import com.app.hotelsaas.catin.web.rest.client.request.CreateClientRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateClienteUseCase {

    private final ClientRepository clientRepository;
    private final TenantRepository tenantRepository;

    /**
     * Executes transactional client creation; persists and returns result
     */
    @Transactional
    public Client execute(CreateClientRequest request){

        Tenant tenant = tenantRepository.findById(UUID.fromString(request.tenantId()))
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found"));

        if(clientRepository.existsByDocumentAndTenantId(request.document(), tenant.getId())){
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

        return clientSaved;
    }
}
