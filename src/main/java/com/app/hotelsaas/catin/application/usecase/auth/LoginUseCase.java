package com.app.hotelsaas.catin.application.usecase.auth;

import com.app.hotelsaas.catin.domain.model.AppUser;
import com.app.hotelsaas.catin.domain.port.AppUserRepository;
import com.app.hotelsaas.catin.infrastructure.security.JwtService;
import com.app.hotelsaas.catin.web.rest.auth.request.LoginRequest;
import com.app.hotelsaas.catin.web.rest.auth.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginUseCase {

    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse execute(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        AppUser appUser = appUserRepository.findByEmail(request.email())
                .orElseThrow(() ->{
                    log.warn("User not found: {}", request.email());
                    return new UsernameNotFoundException("User not found");
                });
        String token = jwtService.generateToken(
                appUser.getId(),
                appUser.getEmail(),
                appUser.getTenant().getId(),
                appUser.getRole()
        );

        log.info("User logged in: {}", appUser.getEmail());

        return new AuthResponse(
                token,
                appUser.getEmail(),
                appUser.getRole(),
                appUser.getTenant().getId().toString()
        );

    }
}
