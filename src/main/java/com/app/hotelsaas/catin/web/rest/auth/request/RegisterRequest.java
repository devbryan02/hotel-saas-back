package com.app.hotelsaas.catin.web.rest.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RegisterRequest(

        @NotNull(message = "El tenantId es obligatorio")
        UUID tenantId,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe ser válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        @NotBlank(message = "El rol es obligatorio")
        String role
) {}
