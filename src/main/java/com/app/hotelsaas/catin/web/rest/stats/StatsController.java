package com.app.hotelsaas.catin.web.rest.stats;

import com.app.hotelsaas.catin.application.usecase.stats.GetDashboardStatsUseCase;
import com.app.hotelsaas.catin.domain.model.DashboardStats;
import com.app.hotelsaas.catin.web.rest.stats.mapper.StatsRestMapper;
import com.app.hotelsaas.catin.web.rest.stats.response.DashboardStatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/stats")
@RequiredArgsConstructor
public class StatsController {

    private final GetDashboardStatsUseCase getDashboardStatsUseCase;
    private final StatsRestMapper mapper;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(
            @PathVariable UUID tenantId
    ) {
        DashboardStats stats = getDashboardStatsUseCase.execute(tenantId);
        return ResponseEntity.ok(mapper.toResponse(stats));
    }

}