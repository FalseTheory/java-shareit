package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto mapToUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User mapCreateDtoToUser(UserCreateDto userDto);

    User mapUpdateDtoToUser(UserUpdateDto userUpdateDto);
}
