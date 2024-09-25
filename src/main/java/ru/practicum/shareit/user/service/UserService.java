package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {

    UserDto create(UserCreateDto userDto);

    UserDto get(Long userId);

    List<UserDto> getAll();

    UserDto update(UserUpdateDto userDto);

    boolean delete(Long userId);
}
