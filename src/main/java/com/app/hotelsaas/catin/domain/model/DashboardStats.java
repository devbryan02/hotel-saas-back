package com.app.hotelsaas.catin.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {

    // Rooms
    private long totalRooms;
    private long availableRooms;
    private long occupiedRooms;
    private long maintenanceRooms;
    private long cleaningRooms;

    // Occupations
    private long activeOccupations;
    private long checkInsToday;
    private long checkOutsToday;

    // Revenue
    private BigDecimal revenueToday;
    private BigDecimal revenueThisMonth;

}