package com.app.hotelsaas.catin.application.usecase.room;

import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import com.app.hotelsaas.catin.domain.port.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetRoomUseCase {

    private final RoomRepository roomRepository;
    private final OccupationRepository occupationRepository;

    public List<RoomWithOccupation> findAllByTenantId(UUID tenantId) {

        List<Room> rooms = roomRepository.findAllByTenantId(tenantId);

        Map<UUID, Occupation> activeOccupationByRoomId = occupationRepository
                .findActiveByTenantId(tenantId)
                .stream()
                .collect(Collectors.toMap(
                        o -> o.getRoom().getId(),
                        o -> o
                ));

        log.info("Active occupations by room id: {}", activeOccupationByRoomId);

        return rooms.stream()
                .map(room -> new RoomWithOccupation(
                        room,
                        activeOccupationByRoomId.get(room.getId())
                ))
                .toList();
    }
}
