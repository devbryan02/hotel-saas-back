package com.app.hotelsaas.catin.web.rest.client.request;

import jakarta.validation.constraints.*;

public record CreateClientRequest(

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
        String fullName,

        @NotBlank(message = "El número de documento es obligatorio")
        @Size(min = 8, max = 20, message = "El documento debe tener entre 8 y 20 caracteres")
        @Pattern(
                regexp = "^[0-9]{8}$|^[A-Za-z0-9]{6,20}$",
                message = "Formato inválido. DNI peruano: 8 dígitos. Pasaporte/extranjero: 6-20 caracteres alfanuméricos"
        )
        String document,

        @Email(message = "El email debe ser válido")
        @Size(max = 150, message = "El email es demasiado largo")
        String email,

        @NotBlank(message = "El número de teléfono es obligatorio")
        @Pattern(
                regexp = "^(\\+51\\s?)?9[0-9]{8}$|^[0-9+\\-\\s]{9,15}$",
                message = "Número inválido. Formatos válidos: 999999999, +51 999999999, 987654321"
        )
        String phone

) {}