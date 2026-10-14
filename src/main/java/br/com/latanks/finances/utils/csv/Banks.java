package br.com.latanks.finances.utils.csv;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Banks {
    MERCADO_PAGO,
    BRADESCO, NUBANK;

    @JsonCreator
    public static Banks fromText(String text) {
        for(Banks bank : Banks.values()) {
            if(bank.toString().equalsIgnoreCase(text)) {
                return bank;
            }
        }

        throw new IllegalArgumentException("Banco não registrado no sistema");
    }
}
