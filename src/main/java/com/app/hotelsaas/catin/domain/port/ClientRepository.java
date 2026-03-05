package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    Client save(Client client);
    Optional<Client> findByTenantIdAndId(UUID tenantId, UUID clientId);
    List<Client> findAllByTenantId(UUID tenantId);
    boolean existsByDocumentAndTenantId(String document, UUID tenantId);
    List<Client> searchByTenantIdAndQuery(UUID tenantId, String query);
    List<Client> searchByTenantIdAndQueryAndStatus(UUID tenantId, String query, String status);

}
