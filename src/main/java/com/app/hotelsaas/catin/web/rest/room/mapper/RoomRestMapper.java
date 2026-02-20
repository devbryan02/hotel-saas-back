package com.app.hotelsaas.catin.web.rest.room.mapper;

import com.app.hotelsaas.catin.domain.model.Room;
import com.app.hotelsaas.catin.web.rest.room.response.RoomDetailResponse;
import org.springframework.stereotype.Component;

@Component
public class RoomRestMapper {

    /**
     * Maps room to room detail response
     */
    public RoomDetailResponse toRoomDetailResponse(Room room){
        return new RoomDetailResponse(
                room.getId().toString(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPricePerNight(),
                room.getStatus()
        );
    }
}
