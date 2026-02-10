package com.app.hotelsaas.catin.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    private UUID id;
    private Tenant tenant;
    private String roomNumber;
    private String roomType;
    private Double pricePerNight;
    private String status;

}