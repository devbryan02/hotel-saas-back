package com.app.hotelsaas.catin.application.usecase.ocupation;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.model.Occupation;
import com.app.hotelsaas.catin.domain.port.OccupationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetOccupationUseCase {

    private final OccupationRepository occupationRepository;
    private final EntityFinder entityFinder;

    public Page<Occupation> findAllByTenantId(UUID tenantId, int page, int size){
        return occupationRepository.findAllByTenantId(tenantId, page, size);
    }

    public Occupation findByTenantIdAndId(UUID id, UUID tenantId){
        return entityFinder.findOccupation(id, tenantId);
    }

}
