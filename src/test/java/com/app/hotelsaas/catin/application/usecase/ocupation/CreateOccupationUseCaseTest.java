package com.app.hotelsaas.catin.application.usecase.ocupation;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.exception.InvalidOccupationDatesException;
import com.app.hotelsaas.catin.domain.exception.RoomNotAvailableException;
import com.app.hotelsaas.catin.domain.model.*;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import com.app.hotelsaas.catin.web.rest.ocupation.request.CreateOccupationRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Caso de uso: Crear ocupación")
class CreateOccupationUseCaseTest {

    @Mock
    private OccupationRepository occupationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private EntityFinder entityFinder;

    @InjectMocks
    private CreateOccupationUseCase createOccupationUseCase;

    private UUID tenantId;
    private UUID roomId;
    private UUID clientId;

    private Tenant tenantExistente;
    private Room roomDisponible;
    private Client clienteExistente;
    private CreateOccupationRequest requestValido;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        roomId = UUID.randomUUID();
        clientId = UUID.randomUUID();

        tenantExistente = new Tenant(
                tenantId,
                "Hotel Pasay",
                "Pasaycacha",
                "Profesional",
                "ACTIVE"
        );

        roomDisponible = new Room(
                roomId,
                tenantExistente,
                "101",
                "Simple",
                BigDecimal.valueOf(80.00),
                "AVAILABLE"
        );

        clienteExistente = new Client(
                clientId,
                tenantExistente,
                "Bryan Cardenas",
                "77296138",
                "bryan@gmail.com",
                "999888777",
                "ACTIVE",
                null
        );

        requestValido = new CreateOccupationRequest(
                LocalDate.now(),
                LocalDate.now().plusDays(3)
        );
    }

    @Nested
    @DisplayName("Creación exitosa de ocupación")
    class CreacionExitosa {

        @Test
        @DisplayName("Debería crear la ocupación, ocupar la habitación y retornar el resultado")
        void deberiaCrearOcupacionCorrectamente() {

            when(entityFinder.findTenant(tenantId)).thenReturn(tenantExistente);
            when(entityFinder.findRoom(roomId, tenantId)).thenReturn(roomDisponible);
            when(entityFinder.findClient(clientId, tenantId)).thenReturn(clienteExistente);
            when(roomRepository.save(any(Room.class))).thenAnswer(inv -> inv.getArgument(0));
            when(occupationRepository.save(any(Occupation.class))).thenAnswer(inv -> inv.getArgument(0));

            Occupation result = createOccupationUseCase.execute(tenantId, roomId, clientId, requestValido);

            assertThat(result).isNotNull();
            assertThat(result.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(240.00)); // 80 x 3 noches
            assertThat(result.getStatus()).isEqualTo("ACTIVE");
            assertThat(result.getRoom().getStatus()).isEqualTo("OCCUPIED");

            verify(roomRepository, times(1)).save(any(Room.class));
            verify(occupationRepository, times(1)).save(any(Occupation.class));
        }
    }

    @Nested
    @DisplayName("Fechas inválidas")
    class FechasInvalidas {

        @Test
        @DisplayName("Debería fallar cuando check-out es igual a check-in")
        void deberiaFallarCuandoCheckOutEsIgualACheckIn() {

            LocalDate mismaFecha = LocalDate.now();
            CreateOccupationRequest requestFechasIguales = new CreateOccupationRequest(
                    mismaFecha,
                    mismaFecha
            );

            assertThatThrownBy(() ->
                    createOccupationUseCase.execute(tenantId, roomId, clientId, requestFechasIguales))
                    .isInstanceOf(InvalidOccupationDatesException.class)
                    .hasMessageContaining("Check-out date must be after check-in date");

            verify(entityFinder, never()).findTenant(any());
            verify(occupationRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debería fallar cuando check-out es antes que check-in")
        void deberiaFallarCuandoCheckOutEsAntesQueCheckIn() {

            CreateOccupationRequest requestFechasInvertidas = new CreateOccupationRequest(
                    LocalDate.now().plusDays(3),
                    LocalDate.now()
            );

            assertThatThrownBy(() ->
                    createOccupationUseCase.execute(tenantId, roomId, clientId, requestFechasInvertidas))
                    .isInstanceOf(InvalidOccupationDatesException.class)
                    .hasMessageContaining("Check-out date must be after check-in date");

            verify(entityFinder, never()).findTenant(any());
            verify(occupationRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Habitación no disponible")
    class HabitacionNoDisponible {

        @Test
        @DisplayName("Debería fallar cuando la habitación está ocupada")
        void deberiaFallarCuandoHabitacionEstaOcupada() {

            Room roomOcupada = new Room(roomId, tenantExistente, "101", "Simple",
                    BigDecimal.valueOf(80.00), "OCCUPIED");

            when(entityFinder.findTenant(tenantId)).thenReturn(tenantExistente);
            when(entityFinder.findRoom(roomId, tenantId)).thenReturn(roomOcupada);
            when(entityFinder.findClient(clientId, tenantId)).thenReturn(clienteExistente);

            assertThatThrownBy(() ->
                    createOccupationUseCase.execute(tenantId, roomId, clientId, requestValido))
                    .isInstanceOf(RoomNotAvailableException.class)
                    .hasMessageContaining("Room is not available. Current status: OCCUPIED");

            verify(roomRepository, never()).save(any());
            verify(occupationRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debería fallar cuando la habitación está en mantenimiento")
        void deberiaFallarCuandoHabitacionEstaEnMantenimiento() {

            Room roomMantenimiento = new Room(roomId, tenantExistente, "101", "Simple",
                    BigDecimal.valueOf(80.00), "MAINTENANCE");

            when(entityFinder.findTenant(tenantId)).thenReturn(tenantExistente);
            when(entityFinder.findRoom(roomId, tenantId)).thenReturn(roomMantenimiento);
            when(entityFinder.findClient(clientId, tenantId)).thenReturn(clienteExistente);

            assertThatThrownBy(() ->
                    createOccupationUseCase.execute(tenantId, roomId, clientId, requestValido))
                    .isInstanceOf(RoomNotAvailableException.class)
                    .hasMessageContaining("MAINTENANCE");

            verify(roomRepository, never()).save(any());
            verify(occupationRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Cálculo del precio total")
    class CalculoPrecioTotal {

        @Test
        @DisplayName("Debería calcular correctamente el precio para 1 noche")
        void deberiaCalcularPrecioParaUnaNocha() {

            CreateOccupationRequest requestUnaNoche = new CreateOccupationRequest(
                    LocalDate.now(),
                    LocalDate.now().plusDays(1)
            );

            when(entityFinder.findTenant(tenantId)).thenReturn(tenantExistente);
            when(entityFinder.findRoom(roomId, tenantId)).thenReturn(roomDisponible);
            when(entityFinder.findClient(clientId, tenantId)).thenReturn(clienteExistente);
            when(roomRepository.save(any(Room.class))).thenAnswer(inv -> inv.getArgument(0));
            when(occupationRepository.save(any(Occupation.class))).thenAnswer(inv -> inv.getArgument(0));

            Occupation result = createOccupationUseCase.execute(tenantId, roomId, clientId, requestUnaNoche);

            assertThat(result.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(80.00));
        }

        @Test
        @DisplayName("Debería calcular correctamente el precio para 7 noches")
        void deberiaCalcularPrecioParaSieteNoches() {

            CreateOccupationRequest requestSieteNoches = new CreateOccupationRequest(
                    LocalDate.now(),
                    LocalDate.now().plusDays(7)
            );

            when(entityFinder.findTenant(tenantId)).thenReturn(tenantExistente);
            when(entityFinder.findRoom(roomId, tenantId)).thenReturn(roomDisponible);
            when(entityFinder.findClient(clientId, tenantId)).thenReturn(clienteExistente);
            when(roomRepository.save(any(Room.class))).thenAnswer(inv -> inv.getArgument(0));
            when(occupationRepository.save(any(Occupation.class))).thenAnswer(inv -> inv.getArgument(0));

            Occupation result = createOccupationUseCase.execute(tenantId, roomId, clientId, requestSieteNoches);

            assertThat(result.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(560.00)); // 80 x 7
        }
    }
}