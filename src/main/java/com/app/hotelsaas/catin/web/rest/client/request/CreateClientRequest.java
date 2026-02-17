package com.app.hotelsaas.catin.web.rest.client.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateClientRequest(

        @NotNull(message = "El Id es obligatorio")
        @Pattern(regexp = "^[0-9a-fA-F\\-]{36}$", message = "Tenant id es invalido")
        String tenantId,

        @NotBlank(message = "Los nombre es obligatorio")
        @Size(min = 3, max = 150)
        String fullName,

        @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener 8 digitos exactamente")
        String document,

        @Email(message = "El email debe ser válido")
        @Size(max = 150)
        String email,

        @Pattern(regexp = "^[0-9+\\- ]{6,20}$", message = "El numero es invalido")
        String phone
) {}
