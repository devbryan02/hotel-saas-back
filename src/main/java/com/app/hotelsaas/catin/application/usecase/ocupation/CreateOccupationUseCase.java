package com.app.hotelsaas.catin.application.usecase.ocupation;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.exception.InvalidOccupationDatesException;
import com.app.hotelsaas.catin.domain.exception.RoomNotAvailableException;
import com.app.hotelsaas.catin.domain.model.Client;
import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import com.app.hotelsaas.catin.web.rest.ocupation.request.CreateOccupationRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateOccupationUseCase {

    private final OccupationRepository occupationRepository;
    private final RoomRepository roomRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public Occupation execute(UUID tenantId, UUID roomId, UUID clientId, CreateOccupationRequest request) {

        if (!request.checkOutDate().isAfter(request.checkInDate())) {
            throw new InvalidOccupationDatesException("Check-out date must be after check-in date");
        }

        Tenant tenant = entityFinder.findTenant(tenantId);
        Room room = entityFinder.findRoom(roomId, tenantId);
        Client client = entityFinder.findClient(clientId, tenantId);

        if (!"AVAILABLE".equals(room.getStatus())) {
            throw new RoomNotAvailableException("Room is not available. Current status: " + room.getStatus());
        }

        long days = ChronoUnit.DAYS.between(request.checkInDate(), request.checkOutDate());
        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(days));

        Room occuipedRoom  = room.occupy();
        roomRepository.save(occuipedRoom);

        Occupation occupation = Occupation.create(
                tenant, client, occuipedRoom,
                request.checkInDate(),
                request.checkOutDate(),
                totalPrice
        );

        Occupation saved = occupationRepository.save(occupation);
        log.info("Occupation created: id={}, room={}, client={}, total={}",
                saved.getId(), roomId, clientId, totalPrice);

        return saved;
    }
}