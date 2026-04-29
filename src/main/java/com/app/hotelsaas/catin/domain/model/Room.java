package com.app.hotelsaas.catin.domain.model;

import com.app.hotelsaas.catin.domain.enums.RoomStatus;
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
    private BigDecimal pricePerNight;
    private RoomStatus status;

    public static Room create(Tenant tenant, String roomNumber, String roomType, BigDecimal pricePerNight) {
        return new Room(null, tenant, roomNumber, roomType, pricePerNight, RoomStatus.AVAILABLE);
    }

    public Room update(String roomNumber, String roomType, BigDecimal pricePerNight, RoomStatus status) {
        return new Room(this.id, this.tenant, roomNumber, roomType, pricePerNight, status);
    }

    public Room occupy() {
        return new Room(this.id, this.tenant, this.roomNumber, this.roomType, this.pricePerNight, RoomStatus.OCCUPIED);
    }

    public Room release() {
        return new Room(this.id, this.tenant, this.roomNumber, this.roomType, this.pricePerNight, RoomStatus.AVAILABLE);
    }
}