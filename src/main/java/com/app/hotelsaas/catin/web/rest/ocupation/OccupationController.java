package com.app.hotelsaas.catin.web.rest.ocupation;

import com.app.hotelsaas.catin.application.usecase.ocupation.CreateOccupationUseCase;
import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.web.rest.ocupation.mapper.OccupationRestMapper;
import com.app.hotelsaas.catin.web.rest.ocupation.request.CreateOccupationRequest;
import com.app.hotelsaas.catin.web.rest.ocupation.response.OccupationDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tenants/{tenantId}/occupations")
@RequiredArgsConstructor
public class OccupationController {

    private final CreateOccupationUseCase createOccupationUseCase;
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
}
