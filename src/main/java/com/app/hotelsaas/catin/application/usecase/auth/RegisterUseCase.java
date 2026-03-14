package com.app.hotelsaas.catin.application.usecase.auth;

import com.app.hotelsaas.catin.application.usecase.helpers.EntityFinder;
import com.app.hotelsaas.catin.domain.model.AppUser;
import com.app.hotelsaas.catin.domain.model.Tenant;
import com.app.hotelsaas.catin.domain.port.AppUserRepository;
import com.app.hotelsaas.catin.web.rest.auth.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterUseCase {

    private final AppUserRepository appUserRepository;
    private final EntityFinder entityFinder;
    private final PasswordEncoder passwordEncoder;

    public AppUser execute(RegisterRequest request) {
        Tenant tenant = entityFinder.findTenant(request.tenantId());

        AppUser appUser = AppUser.create(
            tenant,
            request.email(),
            passwordEncoder.encode(request.password()),
            request.role()
        );

        AppUser saved = appUserRepository.save(appUser);
        log.info(
            "AppUser created: {}, role: {}, tenant: {}",
            saved.getEmail(),
            saved.getRole(),
            tenant.getName()
        );

        return saved;
    }
}
