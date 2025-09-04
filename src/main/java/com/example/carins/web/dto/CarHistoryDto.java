package com.example.carins.web.dto;

import com.example.carins.model.EventType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CarHistoryDto(
        EventType type,
        LocalDate date,
        Long refId,
        String description,
        BigDecimal amount,
        String provider,
        LocalDate startDate,
        LocalDate endDate
) {
}
