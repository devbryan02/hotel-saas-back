package com.app.hotelsaas.catin.web.rest.ocupation.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateOccupationRequest(

        @NotNull(message = "La fecha de check-in es obligatoria")
        @FutureOrPresent(message = "El check-in debe ser una fecha presente o futura")
        LocalDate checkInDate,

        @NotNull(message = "La fecha de check-out es obligatoria")
        @Future(message = "El check-out debe ser una fecha futura")
        LocalDate checkOutDate

) { }