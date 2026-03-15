package com.app.hotelsaas.catin.web.rest.stats.response;

import java.math.BigDecimal;

public record DashboardStatsResponse(
        long totalRooms,
        long availableRooms,
        long occupiedRooms,
        long maintenanceRooms,
        long cleaningRooms,
        long activeOccupations,
        long checkInsToday,
        long checkOutsToday,
        BigDecimal revenueToday,
        BigDecimal revenueThisMonth
) {}