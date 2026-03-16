package com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa;

import com.app.hotelsaas.catin.infrastructure.persistence.Entity.OccupationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OccupationJpaRepository extends JpaRepository<OccupationEntity, UUID> {

    @Query("SELECT o FROM OccupationEntity o WHERE o.tenant.id = :tenantId")
    @EntityGraph(attributePaths = {"room", "client"})
    Page<OccupationEntity> findAllByTenantId(
            @Param("tenantId") UUID tenantId,
            Pageable pageable
    );

    Page<OccupationEntity> findByTenantIdAndStatus(UUID tenantId, String status, Pageable pageable);

    Optional<OccupationEntity> findByTenantIdAndId(UUID tenantId, UUID occupationId);

    @Query("SELECT o FROM OccupationEntity o " +
            "JOIN FETCH o.room " +
            "JOIN FETCH o.client " +
            "WHERE o.tenant.id = :tenantId " +
            "AND o.status = 'ACTIVE'")
    List<OccupationEntity> findActiveByTenantId(@Param("tenantId") UUID tenantId);

    long countByTenantIdAndStatus(UUID tenantId, String status);
    long countByTenantIdAndCheckInDate(UUID tenantId, LocalDate checkInDate);

    @Query("SELECT COUNT(o) FROM OccupationEntity o " +
            "WHERE o.tenant.id = :tenantId " +
            "AND o.status = 'FINISHED' " +
            "AND o.finishedAt >= :startOfDay " +
            "AND o.finishedAt <= :endOfDay")
    long countCheckOutsToday(
            @Param("tenantId") UUID tenantId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM OccupationEntity o " +
            "WHERE o.tenant.id = :tenantId " +
            "AND o.status = 'FINISHED' " +
            "AND o.finishedAt >= :startOfDay " +
            "AND o.finishedAt <= :endOfDay")
    BigDecimal sumRevenueTodayByFinishedAt(
            @Param("tenantId") UUID tenantId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM OccupationEntity o " +
            "WHERE o.tenant.id = :tenantId " +
            "AND o.status = 'FINISHED' " +
            "AND o.finishedAt >= :startOfMonth " +
            "AND o.finishedAt <= :endOfMonth")
    BigDecimal sumRevenueThisMonthByFinishedAt(
            @Param("tenantId") UUID tenantId,
            @Param("startOfMonth") LocalDateTime startOfMonth,
            @Param("endOfMonth") LocalDateTime endOfMonth
    );

    @Query("SELECT o FROM OccupationEntity o WHERE o.status = 'ACTIVE' AND o.checkOutDate <= :date")
    List<OccupationEntity> findActiveByCheckOutDateLessThanEqual(@Param("date") LocalDate date);
}