package br.com.latanks.finances.models.dto.request;

import jakarta.validation.constraints.Email;

public record LoginDTO (
        @Email(message = "Coloque um email valido")
        String email,
        String password
){
}
