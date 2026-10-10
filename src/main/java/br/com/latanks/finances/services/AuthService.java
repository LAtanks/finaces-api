package br.com.latanks.finances.services;

import br.com.latanks.finances.exception.impl.InvalidCredentialsException;
import br.com.latanks.finances.exception.impl.UserNotFoundException;
import br.com.latanks.finances.models.User;
import br.com.latanks.finances.models.dto.response.LoggedDTO;
import br.com.latanks.finances.models.dto.request.LoginDTO;
import br.com.latanks.finances.repositories.IUserRepository;
import br.com.latanks.finances.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTH_SERVICE")
public class AuthService {

    private final IUserRepository userRepository;
    private final JWTUtil  jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoggedDTO login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.email()).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + loginDTO.email())
        );

        if(!passwordEncoder.matches(loginDTO.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        log.debug("User logged with {} your token is {}", user.getEmail(), token);

        return new LoggedDTO(token, user.getEmail(),  "USER");
    }
}
