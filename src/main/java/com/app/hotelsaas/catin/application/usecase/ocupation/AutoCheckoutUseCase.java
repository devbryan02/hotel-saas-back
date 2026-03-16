package com.app.hotelsaas.catin.application.usecase.ocupation;

import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.port.ClientRepository;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoCheckoutUseCase {

    private final OccupationRepository occupationRepository;
    private final RoomRepository roomRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public int execute() {

        LocalDate today = LocalDate.now();

        // Todas las ocupaciones ACTIVE cuya checkOutDate ya pasó o es hoy
        List<Occupation> vencidas = occupationRepository.findActiveByCheckOutDateLessThanEqual(today);

        if (vencidas.isEmpty()) {
            log.info("No hay ocupaciones vencidas para hacer auto-checkout");
            return 0;
        }

        log.info("Procesando {} ocupaciones vencidas", vencidas.size());

        for (Occupation occupation : vencidas) {
            try {
                // OCCUPATION: ACTIVE → FINISHED
                Occupation finished = occupation.checkOut();
                occupationRepository.save(finished);

                // ROOM: OCCUPIED → AVAILABLE
                Room released = occupation.getRoom().release();
                roomRepository.save(released);

                // CLIENT: registrar última estancia
                Client updated = occupation.getClient().registerStay();
                clientRepository.save(updated);

                log.info("Auto-checkout OK — occupationId={}, room={}, client={}",
                        occupation.getId(),
                        occupation.getRoom().getRoomNumber(),
                        occupation.getClient().getFullName());

            } catch (Exception e) {
                // Si falla una, logueamos y seguimos con las demás
                log.error("Error en auto-checkout de occupationId={}: {}",
                        occupation.getId(), e.getMessage());
            }
        }

        return vencidas.size();
    }
}