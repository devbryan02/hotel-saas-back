package com.app.hotelsaas.catin.infrastructure.persistence.repository.impl;

import com.app.hotelsaas.catin.domain.model.AppUser;
import com.app.hotelsaas.catin.domain.port.AppUserRepository;
import com.app.hotelsaas.catin.infrastructure.persistence.mapper.AppUserEntityMapper;
import com.app.hotelsaas.catin.infrastructure.persistence.repository.jpa.AppUserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AppUserRepositoryImpl implements AppUserRepository {

    private final AppUserJpaRepository jpa;
    private final AppUserEntityMapper mapper;

    @Override
    public AppUser save(AppUser appUser) {
        return mapper.toDomain(jpa.save(mapper.toEntity(appUser)));
    }
}
