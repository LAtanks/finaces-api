package br.com.latanks.finances.models.dto.request;

import br.com.latanks.finances.models.enums.MovimentType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateMovimentDTO(
        BigDecimal value,
        LocalDate date,
        MovimentType type,
        String description,
        Long categoryId
) {
}
