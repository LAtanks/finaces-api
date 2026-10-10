package br.com.latanks.finances.models.dto;

import br.com.latanks.finances.models.User;
import br.com.latanks.finances.models.dto.request.CreateUserDTO;
import br.com.latanks.finances.models.dto.request.UpdateUserDTO;
import br.com.latanks.finances.models.dto.response.CreatedUserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(CreateUserDTO dto);
    User toEntity(UpdateUserDTO dto);

    CreatedUserDTO toDTO(User user);


}
