package com.app.hotelsaas.catin.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    private UUID id;
    private Tenant tenant;
    private String roomNumber;
    private String roomType;
    private BigDecimal PricePerNight;
    private String status;

    public static Room create(Tenant tenant, String roomNumber, String roomType, BigDecimal pricePerNight){
        return new Room(null, tenant, roomNumber, roomType, pricePerNight, "AVAILABLE");
    }

    public Room update(String roomNumber, String roomType, BigDecimal pricePerNight, String status){
        return new Room(this.id, this.tenant, roomNumber, roomType, pricePerNight, status);
    }

}