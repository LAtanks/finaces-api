package br.com.latanks.finances.models.dto.response;

import br.com.latanks.finances.models.Category;
import br.com.latanks.finances.models.User;
import br.com.latanks.finances.models.enums.MovimentType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public record CreatedMovimentDTO (
        Double value,
        LocalDate date,
        MovimentType type,
        String description,

        Category category,
        @JsonIgnore
        User user
){

}
