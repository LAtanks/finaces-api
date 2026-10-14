package br.com.latanks.finances.exception.impl;

public class InvalidCSVFileException extends RuntimeException {
    public InvalidCSVFileException(String message) {
        super(message);
    }
}
