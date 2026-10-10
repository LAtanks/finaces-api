package br.com.latanks.finances.models.dto.request;

public record CreateUserDTO (
        String name,
        String password,
        String email
){
}
