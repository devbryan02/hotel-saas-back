package com.app.hotelsaas.catin.web.rest.room.mapper;

import com.app.hotelsaas.catin.application.usecase.room.RoomWithOccupation;
import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.web.rest.room.response.RoomDetailResponse;
import com.app.hotelsaas.catin.web.rest.room.response.RoomListItemResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class RoomRestMapper {

    /**
     * Maps room to room detail response
     */
    public RoomDetailResponse toRoomDetailResponse(Room room) {
        return new RoomDetailResponse(
            room.getId().toString(),
            room.getRoomNumber(),
            room.getRoomType(),
            room.getPricePerNight(),
            room.getStatus()
        );
    }

    public List<RoomListItemResponse> toListItemResponses(
        List<RoomWithOccupation> rooms
    ) {
        return rooms.stream().map(this::toListItemResponse).toList();
    }

    private RoomListItemResponse toListItemResponse(RoomWithOccupation rwo) {
        RoomListItemResponse.ActiveClientResponse clientResponse = null;

        if (rwo.occupation() != null) {
            clientResponse = new RoomListItemResponse.ActiveClientResponse(
                rwo.occupation().getId().toString(),
                rwo.occupation().getClient().getId().toString(),
                rwo.occupation().getClient().getFullName(),
                rwo.occupation().getClient().getDocument(),
                rwo.occupation().getCheckInDate(),
                rwo.occupation().getCheckOutDate()
            );
        }

        return new RoomListItemResponse(
            rwo.room().getId().toString(),
            rwo.room().getRoomNumber(),
            rwo.room().getRoomType(),
            rwo.room().getPricePerNight(),
            rwo.room().getStatus(),
            clientResponse
        );
    }
}
