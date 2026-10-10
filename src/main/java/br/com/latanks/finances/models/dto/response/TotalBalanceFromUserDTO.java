package br.com.latanks.finances.models.dto.response;

public record TotalBalanceFromUserDTO(
        String year,
        String month,
        Double totalIncome,
        Double totaoExpense,
        Double balance
) {
}
