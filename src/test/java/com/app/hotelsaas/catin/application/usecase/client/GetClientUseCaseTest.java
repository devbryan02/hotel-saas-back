package com.app.hotelsaas.catin.application.usecase.client;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.exception.ClientNotFoundException;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Tenant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Caso de uso: Obtener clientes")
class GetClientUseCaseTest {

    @Mock private EntityFinder entityFinder;

    @InjectMocks
    private GetClientUseCase getClientUseCase;

    // Declaracion de datos para la prueba
    private UUID tenantId;
    private UUID clientId;
    private Client clienteExistente;

    @BeforeEach
    void setUp(){

        tenantId = UUID.randomUUID();
        clientId = UUID.randomUUID();

        // Simulacion de un tenant existente
        Tenant tenant = new Tenant(
                tenantId,
                "Juan",
                "Hotel A",
                "FREE",
                "ACTIVE"
        );

        // Simulacion de un cliente existente
        clienteExistente = new Client(
                clientId,
                tenant,
                "Pedro Sanchez Lopez",
                "87253678",
                "pedro@gmail.com",
                "908525467",
                "ACTIVE",
                null,
                LocalDateTime.now()
        );
    }

    @Nested
    @DisplayName("Obtener cliente por ID")
    class ObtenerClientePorId {

        @Test
        @DisplayName("Debería retornar el cliente cuando existe")
        void deberiaRetornarClienteCuandoExiste(){

            when(entityFinder.findClient(tenantId, clientId))
                    .thenReturn(clienteExistente);

            Client result = getClientUseCase.findTenantIdAndId(tenantId, clientId);

            assertEquals(clienteExistente, result);
            verify(entityFinder).findClient(tenantId, clientId);
        }

        @Test
        @DisplayName("Debería lanzar excepción cuando no existe")
        void deberiaLanzarExcepcionCuandoNoExiste(){

            when(entityFinder.findClient(tenantId, clientId))
                    .thenThrow(new ClientNotFoundException("Client not found"));

            assertThrows(ClientNotFoundException.class, () ->
                    getClientUseCase.findTenantIdAndId(tenantId, clientId)
            );

            verify(entityFinder).findClient(tenantId, clientId);
        }
    }
}