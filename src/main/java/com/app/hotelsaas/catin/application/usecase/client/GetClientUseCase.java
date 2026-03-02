package com.app.hotelsaas.catin.application.usecase.client;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetClientUseCase {

    private final ClientRepository clientRepository;
    private final EntityFinder entityFinder;

    public List<Client> findAllByTenantId(UUID tenantId){
        return clientRepository.findAllByTenantId(tenantId);
    }

    public Client findByIdAndTenantId(UUID id, UUID tenantId){
        return entityFinder.findClient(id, tenantId);
    }

}
