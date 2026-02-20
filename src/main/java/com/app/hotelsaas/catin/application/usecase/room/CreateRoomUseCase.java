package com.app.hotelsaas.catin.application.usecase.room;

import com.app.hotelsaas.catin.domain.exception.DuplicateRoomException;
import com.app.hotelsaas.catin.domain.exception.TenantNotFoundException;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import com.app.hotelsaas.catin.domain.port.TenantRepository;
import com.app.hotelsaas.catin.web.rest.room.request.CreateRoomRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateRoomUseCase {

    private final RoomRepository roomRepository;
    private final TenantRepository tenantRepository;

    /**
     * Creates room if tenant exists and room is unique
     */
    public Room execute(UUID tenantId, CreateRoomRequest request){

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found"));

        if(roomRepository.existsByRoomNumberAndTenantId(request.roomNumber(), tenantId)){
            log.warn("Room with roomNumber {} already exists", request.roomNumber());
            throw new DuplicateRoomException("Room with roomNumber "+request.roomNumber()+" already exists");
        }

        Room room = Room.create(
                tenant,
                request.roomNumber(),
                request.roomType(),
                BigDecimal.valueOf(request.pricePerNight()).setScale(2, RoundingMode.HALF_UP)
        );

        return roomRepository.save(room);
    }

}
