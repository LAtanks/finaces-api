package br.com.latanks.finances.services;

import br.com.latanks.finances.exception.impl.InvalidCredentialsException;
import br.com.latanks.finances.exception.impl.UserNotFoundException;
import br.com.latanks.finances.models.User;
import br.com.latanks.finances.models.dto.request.CreateUserDTO;
import br.com.latanks.finances.models.dto.response.CreatedUserDTO;
import br.com.latanks.finances.models.dto.request.UpdateUserDTO;
import br.com.latanks.finances.models.dto.UserMapper;
import br.com.latanks.finances.repositories.IUserRepository;
import br.com.latanks.finances.utils.AuthUtil;
import br.com.latanks.finances.utils.PropertiesUtil;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER_SERVICE")
public class UserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Getter
    private final UserMapper mapper;

    @Transactional
    public CreatedUserDTO create(CreateUserDTO user) {
        userRepository.findByEmail(user.email()).ifPresent(
                value -> {
                    throw new InvalidCredentialsException("Usuario já existente");
                }
        );
        for(Character caracter : user.name().toCharArray()){
            if(Character.isDigit(caracter)){
                throw new InvalidCredentialsException("Nome não pode conter numero");
            }
        }

        User userToCreate = mapper.toEntity(user);
        userToCreate.setPassword(passwordEncoder.encode(user.password()));

        log.debug("User registred with {} with name {}", user.email(), user.name());
        return mapper.toDTO(userRepository.save(userToCreate));
    }

    public CreatedUserDTO getMy(){
        User user = userRepository.findByEmail(AuthUtil.getAuthenticatedEmail()).orElseThrow(
                () -> new UserNotFoundException("User with email " + AuthUtil.getAuthenticatedEmail() + " not found")
        );

        return mapper.toDTO(user);
    }

    public CreatedUserDTO getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("User with id " + id + " not found")
        );

        return mapper.toDTO(user);
    }

    public CreatedUserDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new InvalidCredentialsException("User with " + email + " not found")
        );

        return mapper.toDTO(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void deleteById(Long id) {
        this.userRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("User with id " + id + " not found")
        );

        userRepository.deleteById(id);
    }

    public CreatedUserDTO update(Long id, UpdateUserDTO user) {
        User updatedUser = userRepository.findById(id).orElseThrow(
                () -> new InvalidCredentialsException("User with id " + id + " not found")
        );

        PropertiesUtil.copyProperties(user, updatedUser);

        return mapper.toDTO(userRepository.save(updatedUser));
    }


}
