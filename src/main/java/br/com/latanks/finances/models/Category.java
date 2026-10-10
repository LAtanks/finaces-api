package br.com.latanks.finances.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "tb_categories")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String description;
}
