package com.app.hotelsaas.catin.web.rest.ocupation;

import com.app.hotelsaas.catin.application.usecase.ocupation.CheckOutOccupationUseCase;
import com.app.hotelsaas.catin.application.usecase.ocupation.CreateOccupationUseCase;
import com.app.hotelsaas.catin.application.usecase.ocupation.GetOccupationUseCase;
import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.web.rest.ocupation.mapper.OccupationRestMapper;
import com.app.hotelsaas.catin.web.rest.ocupation.request.CreateOccupationRequest;
import com.app.hotelsaas.catin.web.rest.ocupation.response.OccupationDetailResponse;
import com.app.hotelsaas.catin.web.rest.ocupation.response.OccupationListItemResponse;
import com.app.hotelsaas.catin.web.rest.shared.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/occupations")
@RequiredArgsConstructor
@Tag(name = "Occupaciones", description = "Gestión de ocupaciones ")
public class OccupationController {

    private final CreateOccupationUseCase createOccupationUseCase;
    private final GetOccupationUseCase getOccupationUseCase;
    private final CheckOutOccupationUseCase checkOutOccupationUseCase;
    private final OccupationRestMapper mapper;

    @Operation(summary = "Crea nueva ocupacion")
    @PostMapping("/rooms/{roomId}/clients/{clientId}")
    public ResponseEntity<OccupationDetailResponse> create(
            @PathVariable UUID tenantId,
            @PathVariable UUID roomId,
            @PathVariable UUID clientId,
            @Valid @RequestBody CreateOccupationRequest request
    ){
        Occupation saved = createOccupationUseCase.execute(tenantId, roomId, clientId, request);
        return ResponseEntity.created(
                URI.create("/tenants/"+tenantId+"/occupations/"+saved.getId())
        ).body(mapper.toDetailResponse(saved));
    }

    @Operation(summary = "Obtiene todas las ocupaciones")
    @GetMapping
    public ResponseEntity<PageResponse<OccupationListItemResponse>> findAll(
            @PathVariable UUID tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status
    ){
        Page<Occupation> occupations = getOccupationUseCase.findAllByTenantId(tenantId, page, size, status);
        return ResponseEntity.ok(PageResponse.from(mapper.toListItemResponses(occupations)));
    }

    @Operation(summary = "Obtiene una ocupacion por su id")
    @GetMapping("/{occupationId}")
    public ResponseEntity<OccupationDetailResponse> findById(
            @PathVariable UUID tenantId,
            @PathVariable UUID occupationId
    ){
        Occupation occupation = getOccupationUseCase.findByTenantIdAndId(tenantId, occupationId);
        return ResponseEntity.ok(mapper.toDetailResponse(occupation));
    }

    @Operation(summary = "Finaliza una ocupacion")
    @PostMapping("/{occupationId}/check-out")
    public ResponseEntity<OccupationDetailResponse> checkOut(
            @PathVariable UUID tenantId,
            @PathVariable UUID occupationId
    ){
        Occupation finished = checkOutOccupationUseCase.checkOut(tenantId, occupationId);
        return ResponseEntity.ok(mapper.toDetailResponse(finished));
    }
}
