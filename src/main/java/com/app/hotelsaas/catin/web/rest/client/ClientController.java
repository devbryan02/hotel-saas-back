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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cliente", description = "Gestión de clientes/huespedes")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final GetClientUseCase getClientUseCase;
    private final UpdateClientUseCase updateClienteUseCase;
    private final ClientRestMapper mapper;

    @Operation(summary = "Crea un nuevo cliente")
    @PostMapping
    public ResponseEntity<ClientDetailResponse> create(
            @PathVariable UUID tenantId,
            @Valid @RequestBody CreateClientRequest request) {

        Client saved = createClientUseCase.execute(tenantId, request);

        return ResponseEntity.created(
                URI.create("/tenants/" + tenantId + "/clients/" + saved.getId()))
                .body(mapper.toDetailResponse(saved));
    }

    @Operation(summary = "Obtiene todos los clientes")
    @GetMapping
    public ResponseEntity<List<ClientListItemResponse>> findAll(
            @PathVariable UUID tenantId
    ){
       List<Client> clients = getClientUseCase.findAllByTenantId(tenantId);
       return ResponseEntity.ok(mapper.toListItemResponses(clients));
    }

    @Operation(summary = "Busca clientes por palabra")
    @GetMapping("/search")
    public ResponseEntity<List<ClientListItemResponse>> searchByQuery(
            @PathVariable UUID tenantId,
            @RequestParam String query
    ){
        List<Client> clients = getClientUseCase.searchByTenantIdAndQuery(tenantId, query);
        return ResponseEntity.ok(mapper.toListItemResponses(clients));
    }

    @Operation(summary = "Busca clientes por palabra y estado")
    @GetMapping("/search/status")
    public ResponseEntity<List<ClientListItemResponse>> searchByQueryAndStatus(
            @PathVariable UUID tenantId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status
    ){
        List<Client> clients = getClientUseCase.searchByTenantIdAndQueryAndStatus(tenantId, query, status);
        return ResponseEntity.ok(mapper.toListItemResponses(clients));
    }

    @Operation(summary = "Obtiene un cliente por su id")
    @GetMapping("/{clientId}")
    public ResponseEntity<ClientDetailResponse> findById(
            @PathVariable UUID tenantId,
            @PathVariable UUID clientId
    ) {
        Client client = getClientUseCase.findTenantIdAnId(tenantId, clientId);
        return ResponseEntity.ok(mapper.toDetailResponse(client));
    }

    @Operation(summary = "Actualiza un cliente")
    @PutMapping("/{clientId}")
    public ResponseEntity<ClientDetailResponse> update(
            @PathVariable UUID tenantId,
            @PathVariable UUID clientId,
            @Valid @RequestBody UpdateClientRequest request
    ){
        Client clientUpdated = updateClienteUseCase.execute(tenantId, clientId, request);
        return ResponseEntity.ok(mapper.toDetailResponse(clientUpdated));
    }
}
