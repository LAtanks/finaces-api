package br.com.latanks.finances.controllers;

import br.com.latanks.finances.models.dto.request.CreateUserDTO;
import br.com.latanks.finances.models.dto.response.CreatedUserDTO;
import br.com.latanks.finances.models.dto.request.UpdateUserDTO;
import br.com.latanks.finances.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<CreatedUserDTO> getByIdOrEmail(@RequestParam(name = "id", required = false) Long id, @RequestParam(name = "email", required = false) String email) {
        return ResponseEntity.ok(id != null ? userService.getById(id) : userService.getByEmail(email));
    }

    @GetMapping("/my")
    public ResponseEntity<CreatedUserDTO> getMy(){
        return ResponseEntity.ok(userService.getMy());
    }

    @PostMapping
    public ResponseEntity<CreatedUserDTO> create(@RequestBody CreateUserDTO user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatedUserDTO> update(@PathVariable("id") Long id, @Valid  @RequestBody UpdateUserDTO user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.deleteById(id);


        return ResponseEntity.noContent().build();
    }
}
