package by.tms.tmsmyproject.mapers;

import by.tms.tmsmyproject.dto.user.*;
import by.tms.tmsmyproject.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestCreateDto userRequestCreateDto);

    User toEntity(UserRegistrationDto userRegistrationDto);

    User toEntity(UserRequestUpdateAdminDto userRequestCreateDto);

    User toEntity(UserRequestUpdateClientDto userRequestCreateDto);

    UserResponseGetDto toDto(User user);

    UserRequestCreateDto toCreateDto(User user);

    UserRegistrationDto toRegistrationDto(User user);

    UserRequestUpdateClientDto toClientUpdateDto(User user);

    UserRequestUpdateAdminDto toAdminUpdateDto(User user);

    List<UserResponseGetDto> toDtoList(List<User> users);

}
