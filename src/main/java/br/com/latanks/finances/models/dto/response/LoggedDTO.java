package br.com.latanks.finances.models.dto.response;

public record LoggedDTO(
        String token,
        String email,
        String role
) {
}
