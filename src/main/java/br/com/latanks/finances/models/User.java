package br.com.latanks.finances.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "tb_users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;

    @NotEmpty(message = "Nome não pode ser vazio")
    @NotNull(message  = "Nome não pode ser nulo")
    @Size(min = 2, message = "O nome não pode ter menos que 2 caracteres")
    @Column(unique = false, nullable = false, updatable = true)
    private String name;

    @NotEmpty(message = "Senha não pode ser vazio")
    @NotNull(message  = "Senha não pode ser nulo")
    @Size(min = 8, message = "Senha precisa no minimo 8 caracteres")
    @Column(unique = false, nullable = false, updatable = true)
    private String password;

    @NotEmpty(message = "Email não pode ser vazio")
    @NotNull(message  = "Email não pode ser nulo")
    @Email(message = "Coloque um email valido")
    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @OneToMany(mappedBy = "user")
    @Getter(onMethod_ = @__( @JsonIgnore))
    private Set<Moviment> moviments = new HashSet<>();
}
