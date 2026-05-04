package com.app.hotelsaas.catin.application.usecase.ocupation;

import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Caso de uso: AutoCheckout")
class AutoCheckoutUseCaseTest {

    @Mock OccupationRepository occupationRepository;
    @Mock RoomRepository roomRepository;
    @Mock ClientRepository clientRepository;

    @InjectMocks AutoCheckoutUseCase autoCheckoutUseCase;

    @Nested
    @DisplayName("Cuando no hay ocupaciones vencidas")
    class SinOcupacionesVencidas {

        @Test
        @DisplayName("Debe retornar 0 y no tocar ningún repositorio de sala o cliente")
        void debeRetornarCeroSinProcesarNada() {
            // Arrange
            when(occupationRepository.findActiveByCheckOutDateLessThanEqual(any(LocalDate.class)))
                    .thenReturn(List.of());

            // Act
            int resultado = autoCheckoutUseCase.execute();

            // Assert
            assertThat(resultado).isZero();
            verifyNoInteractions(roomRepository);
            verifyNoInteractions(clientRepository);
            verify(occupationRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Cuando hay ocupaciones vencidas")
    class ConOcupacionesVencidas {

        @Test
        @DisplayName("Debe hacer checkout, liberar sala y registrar estancia por cada ocupación")
        void debeProcesarTodasLasOcupacionesVencidas() {
            // Arrange
            Occupation occupation1 = mock(Occupation.class);
            Occupation occupation2 = mock(Occupation.class);

            Occupation finished1 = mock(Occupation.class);
            Occupation finished2 = mock(Occupation.class);

            Room room1 = mock(Room.class);
            Room room2 = mock(Room.class);
            Room released1 = mock(Room.class);
            Room released2 = mock(Room.class);

            Client client1 = mock(Client.class);
            Client client2 = mock(Client.class);
            Client updated1 = mock(Client.class);
            Client updated2 = mock(Client.class);

            // occupation1
            when(occupation1.checkOut()).thenReturn(finished1);
            when(occupation1.getRoom()).thenReturn(room1);
            when(room1.release()).thenReturn(released1);
            when(occupation1.getClient()).thenReturn(client1);
            when(client1.registerStay()).thenReturn(updated1);

            // occupation2
            when(occupation2.checkOut()).thenReturn(finished2);
            when(occupation2.getRoom()).thenReturn(room2);
            when(room2.release()).thenReturn(released2);
            when(occupation2.getClient()).thenReturn(client2);
            when(client2.registerStay()).thenReturn(updated2);

            when(occupationRepository.findActiveByCheckOutDateLessThanEqual(any(LocalDate.class)))
                    .thenReturn(List.of(occupation1, occupation2));

            // Act
            int resultado = autoCheckoutUseCase.execute();

            // Assert — retorna el número correcto
            assertThat(resultado).isEqualTo(2);

            // Assert — cada occupation fue guardada en su estado FINISHED
            verify(occupationRepository).save(finished1);
            verify(occupationRepository).save(finished2);

            // Assert — cada sala fue liberada
            verify(roomRepository).save(released1);
            verify(roomRepository).save(released2);

            // Assert — cada cliente registró su estancia
            verify(clientRepository).save(updated1);
            verify(clientRepository).save(updated2);
        }
    }

    @Nested
    @DisplayName("Cuando una ocupación falla durante el proceso")
    class UnaOcupacionFalla {

        @Test
        @DisplayName("Debe continuar con las demás ocupaciones y retornar el total de vencidas")
        void debeContinuarSiUnaOcupacionFalla() {
            // Arrange
            Occupation ocupacionMala  = mock(Occupation.class);
            Occupation ocupacionBuena = mock(Occupation.class);

            Occupation finished = mock(Occupation.class);
            Room room           = mock(Room.class);
            Room released       = mock(Room.class);
            Client client       = mock(Client.class);
            Client updated      = mock(Client.class);

            // La primera explota en checkOut()
            when(ocupacionMala.checkOut())
                    .thenThrow(new RuntimeException("Estado inválido"));

            // La segunda se procesa bien
            when(ocupacionBuena.checkOut()).thenReturn(finished);
            when(ocupacionBuena.getRoom()).thenReturn(room);
            when(room.release()).thenReturn(released);
            when(ocupacionBuena.getClient()).thenReturn(client);
            when(client.registerStay()).thenReturn(updated);

            when(occupationRepository.findActiveByCheckOutDateLessThanEqual(any(LocalDate.class)))
                    .thenReturn(List.of(ocupacionMala, ocupacionBuena));

            // Act — NO debe lanzar excepción, el catch interno la absorbe
            int resultado = autoCheckoutUseCase.execute();

            // Assert — igual retorna el total de vencidas encontradas
            assertThat(resultado).isEqualTo(2);

            // Assert — la buena sí fue procesada completamente
            verify(occupationRepository).save(finished);
            verify(roomRepository).save(released);
            verify(clientRepository).save(updated);
        }

        @Test
        @DisplayName("Debe retornar 0 saves si todas las ocupaciones fallan")
        void noProcesaNadaSiTodasFallan() {
            // Arrange
            Occupation mala1 = mock(Occupation.class);
            Occupation mala2 = mock(Occupation.class);

            when(mala1.checkOut()).thenThrow(new RuntimeException("fallo 1"));
            when(mala2.checkOut()).thenThrow(new RuntimeException("fallo 2"));

            when(occupationRepository.findActiveByCheckOutDateLessThanEqual(any(LocalDate.class)))
                    .thenReturn(List.of(mala1, mala2));

            // Act
            int resultado = autoCheckoutUseCase.execute();

            // Assert — retorna el total vencidas aunque todas fallaron
            assertThat(resultado).isEqualTo(2);

            // Assert — nunca se guardó nada
            verify(occupationRepository, never()).save(any());
            verifyNoInteractions(roomRepository);
            verifyNoInteractions(clientRepository);
        }
    }
}