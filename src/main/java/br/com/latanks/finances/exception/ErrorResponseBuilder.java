package br.com.latanks.finances.exception;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ErrorResponseBuilder(
        int status,
        String message,
        String exception,
        String path,
        @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
        LocalDateTime timestamp
) {
}
