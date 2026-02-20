package com.app.hotelsaas.catin.application.usecase.client;

import com.app.hotelsaas.catin.domain.exception.DuplicateClientException;
import com.app.hotelsaas.catin.domain.exception.TenantNotFoundException;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.domain.port.TenantRepository;
import com.app.hotelsaas.catin.web.rest.client.request.CreateClientRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Caso de uso: Crear cliente")
class CreateClientUseCaseTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private CreateClientUseCase createClientUseCase;

    private UUID tenantId;
    private Tenant tenantExistente;
    private CreateClientRequest requestValido;

    @BeforeEach
    void setup() {
        tenantId = UUID.randomUUID();

        tenantExistente = new Tenant(
                tenantId,
                "Hotel Pasay",
                "Pasaycacha",
                "Profesional",
                "ACTIVE"
        );

        requestValido = new CreateClientRequest(
                "Bryan Cardenas",
                "77296138",
                "brayan7br7@gmail.com",
                "99245988"
        );
    }

    @Nested
    @DisplayName("Creación exitosa de cliente")
    class CreacionExitosa {

        @Test
        @DisplayName("debería crear y retornar el cliente cuando el tenant existe y el documento es único")
        void deberiaCrearYRetornarClienteCuandoTodoEsValido() {

            when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenantExistente));
            when(clientRepository.existsByDocumentAndTenantId("77296138", tenantId)).thenReturn(false);
            when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

            Client clienteCreado = createClientUseCase.execute(tenantId, requestValido);

            assertThat(clienteCreado)
                    .isNotNull()
                    .extracting(
                            Client::getFullName,
                            Client::getDocument,
                            Client::getEmail,
                            Client::getPhone,
                            Client::getTenant
                    )
                    .containsExactly(
                            "Bryan Cardenas",
                            "77296138",
                            "brayan7br7@gmail.com",
                            "99245988",
                            tenantExistente
                    );

            verify(clientRepository, times(1)).save(any(Client.class));
        }
    }

    @Nested
    @DisplayName("Tenant no encontrado")
    class TenantNoEncontrado {

        @Test
        @DisplayName("debería lanzar TenantNotFoundException y no intentar guardar el cliente")
        void deberiaFallarCuandoElTenantNoExiste() {

            when(tenantRepository.findById(tenantId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> createClientUseCase.execute(tenantId, requestValido))
                    .isInstanceOf(TenantNotFoundException.class)
                    .hasMessageContaining("Tenant not found");

            verify(clientRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Documento duplicado en el tenant")
    class DocumentoDuplicado {

        @Test
        @DisplayName("debería lanzar DuplicateClientException cuando el documento ya existe para ese tenant")
        void deberiaFallarCuandoElDocumentoYaEstaRegistrado() {

            when(tenantRepository.findById(tenantId)).thenReturn(Optional.of(tenantExistente));
            when(clientRepository.existsByDocumentAndTenantId("77296138", tenantId)).thenReturn(true);

            assertThatThrownBy(() -> createClientUseCase.execute(tenantId, requestValido))
                    .isInstanceOf(DuplicateClientException.class)
                    .hasMessageContaining("77296138");

            verify(clientRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tenant ID inválido")
    class TenantIdInvalido {

        @Test
        @DisplayName("debería lanzar IllegalArgumentException cuando el tenantId no es un UUID válido")
        void deberiaFallarConTenantIdMalFormado() {

            UUID tenantIdInvalido = UUID.fromString("tenant-id-mal-formado");

            CreateClientRequest requestInvalido = new CreateClientRequest(
                    "Bryan Cardenas",
                    "77296138",
                    "brayan7br7@gmail.com",
                    "99245988"
            );

            assertThatThrownBy(() -> createClientUseCase.execute(tenantIdInvalido, requestInvalido))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(tenantRepository, never()).findById(any());
            verify(clientRepository, never()).save(any());
        }
    }
}