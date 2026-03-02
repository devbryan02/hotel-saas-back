package com.app.hotelsaas.catin.web.rest.ocupation.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OccupationListItemResponse(
        String id,

        String roomNumber,
        String roomType,

        String clientFullName,
        String clientDocument,

        LocalDate checkInDate,
        LocalDate checkOutDate,
        long nights,

        BigDecimal totalPrice,

        String status
)
{ }