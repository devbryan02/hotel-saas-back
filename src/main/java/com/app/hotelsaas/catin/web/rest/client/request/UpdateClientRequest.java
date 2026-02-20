package com.app.hotelsaas.catin.web.rest.client.request;

import jakarta.validation.constraints.*;

public record UpdateClientRequest(

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
        String fullName,

        @Email(message = "El email debe ser válido")
        @Size(max = 150, message = "El email es muy largo")
        String email,

        @Pattern(
                regexp = "^(\\+51\\s?)?9[0-9]{8}$|^[0-9+\\-\\s]{6,20}$",
                message = "Número inválido. Usa formato peruano: 999999999 o +51 999999999"
        )
        String phone
)
{ }
