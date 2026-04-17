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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            log.warn("Check-out date must be after check-in date");
            throw new InvalidOccupationDatesException("Check-out date must be after check-in date");
        }

        Tenant tenant = entityFinder.findTenant(tenantId);
        Room room = entityFinder.findRoom(tenantId, roomId);
        Client client = entityFinder.findClient(tenantId, clientId);

        if (!"AVAILABLE".equals(room.getStatus())) {
            log.warn("Room is not available. Current status: {}", room.getStatus());
            throw new RoomNotAvailableException("Room is not available. Current status: " + room.getStatus());
        }

        // Si el frontend manda totalPrice lo usamos, si no calculamos automático
        BigDecimal totalPrice;
        if (request.totalPrice() != null && request.totalPrice().compareTo(BigDecimal.ZERO) > 0) {
            totalPrice = request.totalPrice();
            log.info("Using custom totalPrice: {}", totalPrice);
        } else {
            long days = ChronoUnit.DAYS.between(request.checkInDate(), request.checkOutDate());
            totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(days));
            log.info("Calculated totalPrice: {} ({} days x {})", totalPrice, days, room.getPricePerNight());
        }

        Room occupiedRoom  = room.occupy();
        roomRepository.save(occupiedRoom);

        Occupation occupation = Occupation.create(
                tenant, client, occupiedRoom,
                request.checkInDate(),
                request.checkOutDate(),
                totalPrice
        );

        Occupation saved = occupationRepository.save(occupation);
        log.info("Occupation created: {}", saved);

        return saved;
    }
}