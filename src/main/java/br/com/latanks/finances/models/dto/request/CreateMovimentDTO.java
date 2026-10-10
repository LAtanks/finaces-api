package br.com.latanks.finances.models.dto.request;

import br.com.latanks.finances.models.enums.MovimentType;

import java.time.LocalDate;

public record CreateMovimentDTO (
        Double value,
        LocalDate date,
        MovimentType type,
        String description,
        String categoryDescription
){

    @Override
    public String toString() {
        return "CreateMovimentDTO{" +
                "value=" + value +
                ", date=" + date +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", categoryDescription=" + categoryDescription +
                '}';
    }
}
