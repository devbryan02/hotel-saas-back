package com.app.hotelsaas.catin.web.rest.ocupation.mapper;

import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.web.rest.ocupation.response.OccupationDetailResponse;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class OccupationRestMapper {

    public OccupationDetailResponse toDetailResponse(Occupation occupation) {
        long nights = ChronoUnit.DAYS.between(
                occupation.getCheckInDate(),
                occupation.getCheckOutDate()
        );
        return new OccupationDetailResponse(
                occupation.getId().toString(),
                occupation.getRoom().getRoomNumber(),
                occupation.getRoom().getRoomType(),
                occupation.getClient().getFullName(),
                occupation.getClient().getDocument(),
                occupation.getCheckInDate(),
                occupation.getCheckOutDate(),
                occupation.getRoom().getPricePerNight(),
                nights,
                occupation.getTotalPrice(),
                occupation.getStatus(),
                occupation.getCreatedAt()
        );
    }
}
