package com.app.hotelsaas.catin.application.usecase.ocupation;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.exception.OccupationNotActiveException;
import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckOutOccupationUseCase {

    private final OccupationRepository occupationRepository;
    private final RoomRepository roomRepository;
    private final EntityFinder entityFinder;

    @Transactional
    public Occupation checkOut(UUID occupationId, UUID tenantId){

        Occupation occupation = entityFinder.findOccupation(occupationId, tenantId);

        if(!"ACTIVE".equals(occupation.getStatus())) {
            log.warn("Occupation is not active");
            throw new OccupationNotActiveException("Occupation is not active. Current status: " + occupation.getStatus());
        }

        // OCCUPATION: ACTIVE -> FINISHED
        Occupation finished = occupation.checkOut();
        occupationRepository.save(finished);

        // ROOM: OCCUPIED -> AVAILABLE
        Room roomCurrent = entityFinder.findRoom(occupation.getRoom().getId(), tenantId);
        Room releaseRoom = roomCurrent.release();
        roomRepository.save(releaseRoom);

        return finished;
    }
}
