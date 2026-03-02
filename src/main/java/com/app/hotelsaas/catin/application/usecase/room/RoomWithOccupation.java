package com.app.hotelsaas.catin.application.usecase.room;

import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.model.Room;

public record RoomWithOccupation(
        Room room,
        Occupation occupation
) {}