package com.app.hotelsaas.catin.infrastructure.persistence.repository.impl;

import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.mapper.ClientEntityMapper;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.ClientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {

    private final ClientJpaRepository jpa;
    private final ClientEntityMapper mapper;

    @Override
    public Client save(Client client) {

        var entity = (client.getId() == null)
                ? mapper.toEntityForCreate(client)
                : mapper.toEntity(client);

        return mapper.toDomain(jpa.save(entity));
    }

    @Override
    public boolean existsByDocumentAndTenantId(String document, UUID tenantId) {
        return jpa.existsByDocumentAndTenantId(document, tenantId);
    }
}
