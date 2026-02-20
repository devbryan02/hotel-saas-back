package com.app.hotelsaas.catin.web.rest.client.mapper;

import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.web.rest.client.response.ClientListItemResponse;
import com.app.hotelsaas.catin.web.rest.client.response.ClientDetailResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClientRestMapper {

    public ClientDetailResponse toDetailResponse(Client client) {
        // Maps client fields to detail response
        return new ClientDetailResponse(
                client.getId().toString(),
                client.getFullName(),
                client.getDocument(),
                client.getEmail(),
                client.getPhone(),
                client.getStatus(),
                client.getCreatedAt()
        );
    }

    public List<ClientListItemResponse> toListItemResponses(List<Client> clients) {
        return clients.stream()
                // Maps client to data transfer object
                .map(c -> new ClientListItemResponse(
                        c.getId().toString(),
                        c.getFullName(),
                        c.getDocument(),
                        c.getPhone(),
                        c.getEmail(),
                        c.getStatus()
                ))
                .toList();
    }
}
