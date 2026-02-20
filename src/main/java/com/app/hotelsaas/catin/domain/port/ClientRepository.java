package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    Client save(Client client);
    Optional<Client> findByIdAndTenantId(UUID id, UUID tenantId);
    List<Client> findAllByTenantId(UUID tenantId);
    Boolean existsByDocumentAndTenantId(String document, UUID tenantId);

}
