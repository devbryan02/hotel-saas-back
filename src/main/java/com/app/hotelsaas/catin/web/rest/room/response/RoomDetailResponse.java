package com.app.hotelsaas.catin.web.rest.room.response;

import java.math.BigDecimal;

public record RoomDetailResponse(
        String id,
        String roomNumber,
        String roomType,
        BigDecimal pricePerNight,
        String status
){ }
