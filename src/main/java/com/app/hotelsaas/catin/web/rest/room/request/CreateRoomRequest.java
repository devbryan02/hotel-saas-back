package com.app.hotelsaas.catin.web.rest.room.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;


public record CreateRoomRequest(

        @NotBlank(message = "El número de habitación es obligatorio")
        @Size(min = 1, max = 10, message = "El número de habitación debe tener entre 1 y 10 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9\\-.]+$", message = "Número de habitación solo puede contener letras, números, guiones y puntos")
        String roomNumber,

        @NotBlank(message = "El tipo de habitación es obligatorio")
        @Size(min = 2, max = 60, message = "El tipo debe tener entre 2 y 60 caracteres")
        String roomType,

        @NotNull(message = "El precio por noche es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
        @DecimalMax(value = "9999.99", message = "Precio muy alto para hoteles regionales (máx 9999.99)")
        @Digits(integer = 4, fraction = 2, message = "Formato inválido: máximo 4 dígitos enteros y 2 decimales")
        Double pricePerNight

) {
}