package com.app.hotelsaas.catin.web.rest.client.mapper;

import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.web.rest.client.response.ClientResponse;
import org.springframework.stereotype.Component;

@Component
public class ClientHttpMapper {

    public ClientResponse toRResponse(Client client){

        // Maps client to client response object
        return new ClientResponse(
                client.getId().toString(),
                client.getTenant().getId().toString(),
                client.getTenant().getName(),
                client.getFullName(),
                client.getDocument(),
                client.getEmail(),
                client.getPhone(),
                client.getStatus(),
                client.getCreatedAt()
        );
    }

}
