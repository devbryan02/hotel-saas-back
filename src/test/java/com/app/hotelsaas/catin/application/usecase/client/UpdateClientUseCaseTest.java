package com.app.hotelsaas.catin.application.usecase.client;

import com.app.hotelsaas.catin.domain.exception.ClientNotFoundException;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.web.rest.client.request.UpdateClientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Caso de uso: Actualizar cliente")
class UpdateClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private UpdateClientUseCase updateClientUseCase;

    private UUID clientId;
    private UUID tenantId;
    private Client clienteExistente;
    private UpdateClientRequest requestValido;

    @BeforeEach
    void setUp() {

        clientId = UUID.randomUUID();
        tenantId = UUID.randomUUID();

        Tenant tenantExistente = new Tenant(
                tenantId,
                "Pedro Lopez",
                "Pasaycacha",
                "Premiun",
                "ACTIVE"
        );

        clienteExistente = new Client(
                clientId,
                tenantExistente,
                "Brayan Cardenas",
                "78234908",
                "brayan@gmail.com",
                "923475893",
                "ACTIVE",
                LocalDateTime.now()
        );

        requestValido = new UpdateClientRequest(
                "Brandy Lopez",
                "brandy@gmail.com",
                "982536172"
        );

    }

    @Nested
    @DisplayName("Actualizacion exitosa del cliente")
    class ActualizacionExitosa {

        @Test
        @DisplayName("Deberia actualizar correctamente cuando el cliente y tenant existan")
        void deberiaActualizarCorrectamente() {

            when(clientRepository.findByIdAndTenantId(clientId, tenantId)).thenReturn(Optional.of(clienteExistente));
            when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Client clienteActualizado = updateClientUseCase.execute(clientId, tenantId, requestValido);

            assertThat(clienteActualizado)
                    .isNotNull()
                    .extracting(
                            Client::getFullName,
                            Client::getEmail,
                            Client::getPhone
                    ).containsExactly(
                            "Brandy Lopez",
                            "brandy@gmail.com",
                            "982536172"
                    );

            verify(clientRepository, times(1)).save(any(Client.class));
        }

    }

    @Nested
    @DisplayName("Cliente no encontrado")
    class ClienteNoEncontrado {

        @Test
        @DisplayName("Deberia fallar cuando client no exista")
        void deberiaFallarCuandoElClienteNoExiste() {

            when(clientRepository.findByIdAndTenantId(clientId, tenantId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> updateClientUseCase.execute(clientId, tenantId, requestValido))
                    .isInstanceOf(ClientNotFoundException.class)
                    .hasMessageContaining("Client not found or not associated with tenant");

            verify(clientRepository, never()).save(any(Client.class));

        }
    }
}