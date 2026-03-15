package com.app.hotelsaas.catin.web.rest.stats.mapper;

import com.app.hotelsaas.catin.domain.model.DashboardStats;
import com.app.hotelsaas.catin.web.rest.stats.response.DashboardStatsResponse;
import org.springframework.stereotype.Component;

@Component
public class StatsRestMapper {

    public DashboardStatsResponse toResponse(DashboardStats s) {
        return new DashboardStatsResponse(
                s.getTotalRooms(),
                s.getAvailableRooms(),
                s.getOccupiedRooms(),
                s.getMaintenanceRooms(),
                s.getCleaningRooms(),
                s.getActiveOccupations(),
                s.getCheckInsToday(),
                s.getCheckOutsToday(),
                s.getRevenueToday(),
                s.getRevenueThisMonth()
        );
    }

}
