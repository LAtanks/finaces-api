package br.com.latanks.finances.exception.impl;

public class InvalidBankCSVFormat extends RuntimeException {
    public InvalidBankCSVFormat(String message) {
        super(message);
    }
}
