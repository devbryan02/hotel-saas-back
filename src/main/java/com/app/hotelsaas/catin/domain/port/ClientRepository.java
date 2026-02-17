package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.Client;

import java.util.UUID;

public interface ClientRepository {

    Client save(Client client);
    boolean existsByDocumentAndTenantId(String document, UUID tenantId);

}
