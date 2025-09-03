package com.example.carins.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record InsPolicyDTO(
        @NotNull(message = "startDate must be provided")
        LocalDate startDate,
        @NotNull(message = "endDate must be provided")
        LocalDate endDate,
        @NotBlank(message = "provider must be provided")
        String provider,
        @NotNull(message = "carId must be provided")
        Long car_id
) {
}
