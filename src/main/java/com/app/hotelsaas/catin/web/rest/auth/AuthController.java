package com.app.hotelsaas.catin.web.rest.auth;

import com.app.hotelsaas.catin.application.usecase.auth.LoginUseCase;
import com.app.hotelsaas.catin.application.usecase.auth.RegisterUseCase;
import com.app.hotelsaas.catin.web.rest.auth.request.LoginRequest;
import com.app.hotelsaas.catin.web.rest.auth.request.RegisterRequest;
import com.app.hotelsaas.catin.web.rest.auth.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "Registro e inicio de sesión")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    public static final String ADMIN_API_KEY_HEADER = "X-Admin-Api-Key";

    @Value("${admin.api-key}")
    private String adminApiKey;


    @Operation(summary = "Iniciar sesión")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginUseCase.execute(request));
    }

    @Operation(summary = "Crear nueva cuenta")
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestHeader(ADMIN_API_KEY_HEADER) String apiKey,
            @Valid @RequestBody RegisterRequest request
    ) {
        if (!adminApiKey.equals(apiKey)) {
            log.warn("Intento de registro con API Key inválida");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        registerUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}