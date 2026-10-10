package br.com.latanks.finances.controllers;

import br.com.latanks.finances.models.dto.request.CreateUserDTO;
import br.com.latanks.finances.models.dto.response.CreatedUserDTO;
import br.com.latanks.finances.models.dto.response.LoggedDTO;
import br.com.latanks.finances.models.dto.request.LoginDTO;
import br.com.latanks.finances.services.AuthService;
import br.com.latanks.finances.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoggedDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<CreatedUserDTO> register(@RequestBody @Valid CreateUserDTO dto) {
        return ResponseEntity.ok(userService.create(dto));
    }
}
