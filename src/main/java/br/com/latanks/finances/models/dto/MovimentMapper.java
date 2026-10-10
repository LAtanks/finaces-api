package br.com.latanks.finances.models.dto;

import br.com.latanks.finances.models.Moviment;
import br.com.latanks.finances.models.dto.request.CreateMovimentDTO;
import br.com.latanks.finances.models.dto.response.CreatedMovimentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimentMapper {

    @Mapping(target = "category", ignore = true)
    Moviment toEntity(CreateMovimentDTO dto);

    CreatedMovimentDTO toDTO(Moviment moviment);
}
