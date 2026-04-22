package com.app.hotelsaas.catin.application.usecase.ocupation;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.exception.OccupationNotActiveException;
import com.app.hotelsaas.catin.domain.model.*;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Caso de uso: Check-out de ocupación")
class CheckOutOccupationUseCaseTest {

    @Mock private OccupationRepository occupationRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private ClientRepository clientRepository;
    @Mock private EntityFinder entityFinder;

    @InjectMocks
    private CheckOutOccupationUseCase checkOutOccupationUseCase;

    private UUID tenantId;
    private UUID occupationId;

    private Tenant tenant;
    private Room roomOcupada;
    private Client cliente;
    private Occupation ocupacionActiva;

    @BeforeEach
    void setUp() {
        tenantId     = UUID.randomUUID();
        occupationId = UUID.randomUUID();

        tenant = new Tenant(tenantId, "Hotel Pasay", "Pasaycacha", "Profesional", "ACTIVE");

        roomOcupada = new Room(
                UUID.randomUUID(), tenant,
                "101", "Simple",
                BigDecimal.valueOf(80.00), "OCCUPIED"
        );

        cliente = new Client(
                UUID.randomUUID(), tenant,
                "Bryan Cardenas", "77296138",
                "bryan@gmail.com", "999888777",
                "ACTIVE", null, LocalDateTime.now()
        );

        ocupacionActiva = new Occupation(
                occupationId, tenant, cliente, roomOcupada,
                LocalDate.now().minusDays(3), LocalDate.now(),
                "ACTIVE", BigDecimal.valueOf(240.00), LocalDateTime.now(), null
        );
    }

    @Nested
    @DisplayName("Check-out exitoso")
    class CheckOutExitoso {

        @Test
        @DisplayName("Debería finalizar la ocupación, liberar la habitación y registrar estadía del cliente")
        void deberiaHacerCheckOutCorrectamente() {

            when(entityFinder.findOccupation(tenantId, occupationId)).thenReturn(ocupacionActiva);
            when(occupationRepository.save(any(Occupation.class))).thenAnswer(inv -> inv.getArgument(0));
            when(roomRepository.save(any(Room.class))).thenAnswer(inv -> inv.getArgument(0));
            when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

            Occupation result = checkOutOccupationUseCase.checkOut(tenantId, occupationId);

            // Ocupación debe quedar FINISHED
            assertThat(result.getStatus()).isEqualTo("FINISHED");
            assertThat(result.getId()).isEqualTo(occupationId);

            // Verificar que se guardaron los 3 cambios
            verify(occupationRepository, times(1)).save(any(Occupation.class));
            verify(roomRepository, times(1)).save(any(Room.class));
            verify(clientRepository, times(1)).save(any(Client.class));
        }

        @Test
        @DisplayName("La habitación debe quedar AVAILABLE después del check-out")
        void habitacionDebeQuedarDisponible() {

            when(entityFinder.findOccupation(tenantId, occupationId)).thenReturn(ocupacionActiva);
            when(occupationRepository.save(any(Occupation.class))).thenAnswer(inv -> inv.getArgument(0));
            when(roomRepository.save(any(Room.class))).thenAnswer(inv -> inv.getArgument(0));
            when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

            checkOutOccupationUseCase.checkOut(tenantId, occupationId);

            // Capturamos la room que se guardó y verificamos su estado
            verify(roomRepository).save(argThat(room ->
                    "AVAILABLE".equals(room.getStatus())
            ));
        }

        @Test
        @DisplayName("El cliente debe tener lastStayAt registrado después del check-out")
        void clienteDebeRegistrarUltimaEstadia() {

            when(entityFinder.findOccupation(tenantId, occupationId)).thenReturn(ocupacionActiva);
            when(occupationRepository.save(any(Occupation.class))).thenAnswer(inv -> inv.getArgument(0));
            when(roomRepository.save(any(Room.class))).thenAnswer(inv -> inv.getArgument(0));
            when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

            checkOutOccupationUseCase.checkOut(tenantId, occupationId);

            // Capturamos el client guardado y verificamos lastStayAt
            verify(clientRepository).save(argThat(client ->
                    client.getLastStayAt() != null
            ));
        }
    }

    @Nested
    @DisplayName("Ocupación no activa")
    class OcupacionNoActiva {

        @Test
        @DisplayName("Debería fallar cuando la ocupación ya está FINISHED")
        void deberiaFallarCuandoOcupacionYaEstaFinalizada() {

            Occupation ocupacionFinalizada = new Occupation(
                    occupationId, tenant, cliente, roomOcupada,
                    LocalDate.now().minusDays(3), LocalDate.now(),
                    "FINISHED", BigDecimal.valueOf(240.00), LocalDateTime.now(), null
            );

            when(entityFinder.findOccupation(tenantId, occupationId)).thenReturn(ocupacionFinalizada);

            assertThatThrownBy(() ->
                    checkOutOccupationUseCase.checkOut(tenantId, occupationId))
                    .isInstanceOf(OccupationNotActiveException.class)
                    .hasMessageContaining("FINISHED");

            verify(occupationRepository, never()).save(any());
            verify(roomRepository, never()).save(any());
            verify(clientRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debería fallar cuando la ocupación está CANCELLED")
        void deberiaFallarCuandoOcupacionEstaCancelada() {

            Occupation ocupacionCancelada = new Occupation(
                    occupationId, tenant, cliente, roomOcupada,
                    LocalDate.now().minusDays(3), LocalDate.now(),
                    "CANCELLED", BigDecimal.valueOf(240.00), LocalDateTime.now(), null
            );

            when(entityFinder.findOccupation(tenantId, occupationId)).thenReturn(ocupacionCancelada);

            assertThatThrownBy(() ->
                    checkOutOccupationUseCase.checkOut(tenantId, occupationId))
                    .isInstanceOf(OccupationNotActiveException.class)
                    .hasMessageContaining("CANCELLED");

            verify(occupationRepository, never()).save(any());
            verify(roomRepository, never()).save(any());
            verify(clientRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Ocupación no encontrada")
    class OcupacionNoEncontrada {

        @Test
        @DisplayName("Debería fallar cuando la ocupación no existe para ese tenant")
        void deberiaFallarCuandoOcupacionNoExiste() {

            when(entityFinder.findOccupation(tenantId, occupationId))
                    .thenThrow(new com.app.hotelsaas.catin.domain.exception
                            .OccupationNotFoundException("Occupation not found or not associated with tenant"));

            assertThatThrownBy(() ->
                    checkOutOccupationUseCase.checkOut(tenantId, occupationId))
                    .isInstanceOf(com.app.hotelsaas.catin.domain.exception.OccupationNotFoundException.class)
                    .hasMessageContaining("Occupation not found");

            verify(occupationRepository, never()).save(any());
            verify(roomRepository, never()).save(any());
            verify(clientRepository, never()).save(any());
        }
    }
}