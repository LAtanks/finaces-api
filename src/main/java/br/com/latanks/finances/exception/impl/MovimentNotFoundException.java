package br.com.latanks.finances.exception.impl;

public class MovimentNotFoundException extends RuntimeException {
    public MovimentNotFoundException(String message) {
        super(message);
    }
}
