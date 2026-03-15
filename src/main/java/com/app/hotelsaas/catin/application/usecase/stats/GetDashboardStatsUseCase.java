package com.app.hotelsaas.catin.application.usecase.stats;

import com.app.hotelsaas.catin.domain.model.DashboardStats;
import com.app.hotelsaas.catin.domain.port.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetDashboardStatsUseCase {

    private final StatsRepository statsRepository;

    public DashboardStats execute(UUID tenantId) {
        return statsRepository.getStatsByTenantId(tenantId);
    }

}
