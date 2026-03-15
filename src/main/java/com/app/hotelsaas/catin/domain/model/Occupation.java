package com.app.hotelsaas.catin.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Occupation {

    private UUID id;
    private Tenant tenant;
    private Client client;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;

    public static Occupation create(Tenant tenant, Client client, Room room, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalPrice){
        return new Occupation(null, tenant, client, room, checkInDate, checkOutDate, "ACTIVE", totalPrice, LocalDateTime.now(), null);
    }

    public Occupation checkOut() {
        return new Occupation(this.id, this.tenant, this.client, this.room, this.checkInDate, this.checkOutDate, "FINISHED", this.totalPrice, this.createdAt, LocalDateTime.now());
    }

}
