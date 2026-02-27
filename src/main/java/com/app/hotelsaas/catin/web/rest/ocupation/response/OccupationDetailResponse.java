package com.app.hotelsaas.catin.web.rest.ocupation.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record OccupationDetailResponse(

        String id,

        String roomNumber,
        String roomType,

        String clientFullName,
        String clientDocument,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate checkInDate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate checkOutDate,

        BigDecimal pricePerNight,
        long nights,
        BigDecimal totalPrice,

        String status,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt

) {}
