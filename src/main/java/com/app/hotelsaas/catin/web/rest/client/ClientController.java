package com.app.hotelsaas.catin.web.rest.client;

import com.app.hotelsaas.catin.application.usecase.client.CreateClienteUseCase;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.web.rest.client.mapper.ClientHttpMapper;
import com.app.hotelsaas.catin.web.rest.client.request.CreateClientRequest;
import com.app.hotelsaas.catin.web.rest.client.response.ClientResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final CreateClienteUseCase createClienteUseCase;
    private final ClientHttpMapper mapper;

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody CreateClientRequest request){
        Client client = createClienteUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toRResponse(client));
    }

}
