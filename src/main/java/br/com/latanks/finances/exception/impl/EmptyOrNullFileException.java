package br.com.latanks.finances.exception.impl;

public class EmptyOrNullFileException extends RuntimeException {
    public EmptyOrNullFileException(String message) {
        super(message);
    }
}
