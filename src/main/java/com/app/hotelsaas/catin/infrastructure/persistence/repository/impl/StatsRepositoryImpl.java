package com.app.hotelsaas.catin.infrastructure.persistence.repository.impl;

import com.app.hotelsaas.catin.domain.model.DashboardStats;
import com.app.hotelsaas.catin.domain.port.StatsRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.OccupationJpaRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.RoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StatsRepositoryImpl implements StatsRepository {

    private final RoomJpaRepository roomJpa;
    private final OccupationJpaRepository occupationJpa;

    @Override
    public DashboardStats getStatsByTenantId(UUID tenantId) {

        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay   = today.atTime(23, 59, 59);
        LocalDateTime startOfMonth = today.withDayOfMonth(1).atStartOfDay();

        // ── Rooms ──────────────────────────────────────────
        long totalRooms       = roomJpa.countByTenantId(tenantId);
        long availableRooms   = roomJpa.countByTenantIdAndStatus(tenantId, "AVAILABLE");
        long occupiedRooms    = roomJpa.countByTenantIdAndStatus(tenantId, "OCCUPIED");
        long maintenanceRooms = roomJpa.countByTenantIdAndStatus(tenantId, "MAINTENANCE");
        long cleaningRooms    = roomJpa.countByTenantIdAndStatus(tenantId, "CLEANING");

        // ── Occupations ────────────────────────────────────
        long activeOccupations = occupationJpa.countByTenantIdAndStatus(tenantId, "ACTIVE");
        long checkInsToday = occupationJpa.countByTenantIdAndCheckInDate(tenantId, today);
        long checkOutsToday = occupationJpa.countCheckOutsToday(tenantId, startOfDay, endOfDay);

        // ── Revenue ────────────────────────────────────────
        BigDecimal revenueToday = occupationJpa.sumRevenueTodayByFinishedAt(
                tenantId, startOfDay, endOfDay
        );

        BigDecimal revenueThisMonth = occupationJpa.sumRevenueThisMonthByFinishedAt(
                tenantId, startOfMonth, endOfDay
        );

        return new DashboardStats(
                totalRooms,
                availableRooms,
                occupiedRooms,
                maintenanceRooms,
                cleaningRooms,
                activeOccupations,
                checkInsToday,
                checkOutsToday,
                revenueToday != null ? revenueToday : BigDecimal.ZERO,
                revenueThisMonth != null ? revenueThisMonth : BigDecimal.ZERO
        );
    }
}