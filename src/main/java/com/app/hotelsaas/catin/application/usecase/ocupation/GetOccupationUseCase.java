package com.app.hotelsaas.catin.application.usecase.ocupation;

import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetOccupationUseCase {

    private final OccupationRepository occupationRepository;

    public List<Occupation> findAllByTenantId(UUID tenantId){
        return occupationRepository.findAllByTenantId(tenantId);
    }

}
