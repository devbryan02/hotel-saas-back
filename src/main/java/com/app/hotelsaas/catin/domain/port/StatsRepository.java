package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.DashboardStats;

import java.util.UUID;

public interface StatsRepository {

    DashboardStats getStatsByTenantId(UUID tenantId);

}
