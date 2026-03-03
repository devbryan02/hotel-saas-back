package com.app.hotelsaas.catin.application.usecase.room;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.exception.DuplicateRoomException;
import com.app.hotelsaas.catin.domain.exception.RoomNotFoundException;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import com.app.hotelsaas.catin.web.rest.room.request.UpdateRoomRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateRoomUseCase {

    private final RoomRepository roomRepository;
    private final EntityFinder entityFinder;

    public Room execute(UUID tenantId, UUID roomId, UpdateRoomRequest request){

        Room room = entityFinder.findRoom(tenantId, roomId);

        if(roomRepository.existsByRoomNumberAndTenantIdAndIdNot(request.roomNumber(), tenantId, roomId)){
            log.warn("Room with roomNumber {} already exists", request.roomNumber());
            throw new DuplicateRoomException("Room with roomNumber "+request.roomNumber()+" already exists");
        }

        Room updated = room.update(
                request.roomNumber(),
                request.roomType(),
                BigDecimal.valueOf(request.pricePerNight()).setScale(2, RoundingMode.HALF_UP),
                request.status()
        );

        return roomRepository.save(updated);
    }
}
