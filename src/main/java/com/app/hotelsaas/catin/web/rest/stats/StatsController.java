package com.app.hotelsaas.catin.web.rest.stats;

import com.app.hotelsaas.catin.application.usecase.stats.GetDashboardStatsUseCase;
import com.app.hotelsaas.catin.domain.model.DashboardStats;
import com.app.hotelsaas.catin.web.rest.stats.mapper.StatsRestMapper;
import com.app.hotelsaas.catin.web.rest.stats.response.DashboardStatsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/stats")
@RequiredArgsConstructor
@Tag(name = "Estadisticas", description = "Muestra estadisticas del hotel")
public class StatsController {

    private final GetDashboardStatsUseCase getDashboardStatsUseCase;
    private final StatsRestMapper mapper;

    @Operation(summary = "Obtiene estadisticas del hotel")
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(
            @PathVariable UUID tenantId
    ) {
        DashboardStats stats = getDashboardStatsUseCase.execute(tenantId);
        return ResponseEntity.ok(mapper.toResponse(stats));
    }

}