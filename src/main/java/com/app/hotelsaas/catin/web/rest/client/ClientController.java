package com.app.hotelsaas.catin.web.rest.client;

import com.app.hotelsaas.catin.application.usecase.client.CreateClientUseCase;
import com.app.hotelsaas.catin.application.usecase.client.GetClientUseCase;
import com.app.hotelsaas.catin.application.usecase.client.UpdateClientUseCase;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.web.rest.client.mapper.ClientRestMapper;
import com.app.hotelsaas.catin.web.rest.client.request.CreateClientRequest;
import com.app.hotelsaas.catin.web.rest.client.request.UpdateClientRequest;
import com.app.hotelsaas.catin.web.rest.client.response.ClientListItemResponse;
import com.app.hotelsaas.catin.web.rest.client.response.ClientDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/clients")
@RequiredArgsConstructor
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final GetClientUseCase getClientUseCase;
    private final UpdateClientUseCase updateClienteUseCase;
    private final ClientRestMapper mapper;

    @PostMapping
    public ResponseEntity<ClientDetailResponse> create(
            @PathVariable UUID tenantId,
            @Valid @RequestBody CreateClientRequest request) {

        Client saved = createClientUseCase.execute(tenantId, request);

        return ResponseEntity.created(
                URI.create("/tenants/" + tenantId + "/clients/" + saved.getId()))
                .body(mapper.toDetailResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<ClientListItemResponse>> findAll(
            @PathVariable UUID tenantId
    ){
       List<Client> clients = getClientUseCase.findAllByTenantId(tenantId);
       return ResponseEntity.ok(mapper.toListItemResponses(clients));
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientDetailResponse> update(
            @PathVariable UUID tenantId,
            @PathVariable UUID clientId,
            @Valid @RequestBody UpdateClientRequest request
    ){
        Client clientUpdated = updateClienteUseCase.execute(clientId, tenantId, request);
        return ResponseEntity.ok(mapper.toDetailResponse(clientUpdated));
    }
}
