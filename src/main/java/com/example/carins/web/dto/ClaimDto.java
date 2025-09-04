package com.example.carins.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClaimDto(
        Long id,
        Long carId,
        @NotNull(message = "claimDate must be provided")LocalDate claimDate,
        @NotBlank(message= "description must be provided") String description,
        @NotNull(message = "amount must be provided") @Positive BigDecimal amount
        ) {
}
