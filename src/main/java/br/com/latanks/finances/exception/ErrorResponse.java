package br.com.latanks.finances.exception;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String message,
        String exception,
        String path,
        @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
        LocalDateTime timestamp
) {
}
