package br.com.latanks.finances.security;

import br.com.latanks.finances.exception.ErrorResponseBuilder;
import br.com.latanks.finances.exception.impl.InvalidCredentialsException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@Slf4j(topic = "AUTHENTICATION_ENTRY_POINT")
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        var ex = new AuthenticationCredentialsNotFoundException(authException.getMessage());
        var body = Map.of(
                "status", 401,
                "error", authException.getMessage(),
                "exception", ex.getClass().getSimpleName(),
                "timestamp", LocalDateTime.now()
        );

        response.getWriter().write(mapper.writeValueAsString(body));

        throw ex;
    }
}
