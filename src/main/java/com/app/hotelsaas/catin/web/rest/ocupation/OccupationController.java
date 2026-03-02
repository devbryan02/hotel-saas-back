package com.app.hotelsaas.catin.web.rest.ocupation;

import com.app.hotelsaas.catin.application.usecase.ocupation.CheckOutOccupationUseCase;
import com.app.hotelsaas.catin.application.usecase.ocupation.CreateOccupationUseCase;
import com.app.hotelsaas.catin.application.usecase.ocupation.GetOccupationUseCase;
import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.web.rest.ocupation.mapper.OccupationRestMapper;
import com.app.hotelsaas.catin.web.rest.ocupation.request.CreateOccupationRequest;
import com.app.hotelsaas.catin.web.rest.ocupation.response.OccupationDetailResponse;
import com.app.hotelsaas.catin.web.rest.ocupation.response.OccupationListItemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/occupations")
@RequiredArgsConstructor
public class OccupationController {

    private final CreateOccupationUseCase createOccupationUseCase;
    private final GetOccupationUseCase getOccupationUseCase;
    private final CheckOutOccupationUseCase checkOutOccupationUseCase;
    private final OccupationRestMapper mapper;

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

    @GetMapping
    public ResponseEntity<List<OccupationListItemResponse>> findAll(@PathVariable UUID tenantId){
        List<Occupation> occupations = getOccupationUseCase.findAllByTenantId(tenantId);
        return ResponseEntity.ok(mapper.toListItemResponses(occupations));
    }

    @GetMapping("/{occupationId}")
    public ResponseEntity<OccupationDetailResponse> findById(
            @PathVariable UUID occupationId,
            @PathVariable UUID tenantId
    ){
        Occupation occupation = getOccupationUseCase.findByIdAndTenantId(occupationId, tenantId);
        return ResponseEntity.ok(mapper.toDetailResponse(occupation));
    }

    @PostMapping("/{occupationId}/check-out")
    public ResponseEntity<OccupationDetailResponse> checkOut(
            @PathVariable UUID occupationId,
            @PathVariable UUID tenantId
    ){
        Occupation finished = checkOutOccupationUseCase.checkOut(occupationId, tenantId);
        return ResponseEntity.ok(mapper.toDetailResponse(finished));
    }
}
