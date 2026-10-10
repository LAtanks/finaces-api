package br.com.latanks.finances.repositories;

import br.com.latanks.finances.models.Moviment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMovimentRepository extends JpaRepository<Moviment, Long> {
}
