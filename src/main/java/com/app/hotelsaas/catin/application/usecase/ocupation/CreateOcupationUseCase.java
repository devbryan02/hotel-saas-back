package com.app.hotelsaas.catin.application.usecase.ocupation;

import com.app.hotelsaas.catin.domain.model.Ocupation;
import com.app.hotelsaas.catin.domain.port.OcupationRepository;
import com.app.hotelsaas.catin.web.rest.ocupation.CreateOcupationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOcupationUseCase {

    private final OcupationRepository ocupationRepository;

    public Ocupation execute(UUID tenantId, UUID roomId, UUID clientId, CreateOcupationRequest request){

    }

}
