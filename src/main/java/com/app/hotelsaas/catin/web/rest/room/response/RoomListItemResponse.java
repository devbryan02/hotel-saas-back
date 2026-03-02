package com.app.hotelsaas.catin.web.rest.room.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomListItemResponse(
        String id,
        String roomNumber,
        String roomType,
        BigDecimal pricePerNight,
        String status,
        ActiveClientResponse activeClient
) {
    public record ActiveClientResponse(
            String clientId,
            String fullName,
            String document,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkInDate,
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate checkOutDate
    ) {}
}