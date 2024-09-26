package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;


    @Override
    public UserDto create(UserCreateDto userDto) {
        User user = mapper.mapCreateDtoToUser(userDto);

        user = userRepository.create(user);

        return mapper.mapToUserDto(user);
    }

    @Override
    public UserDto get(Long userId) {
        return userRepository.getById(userId)
                .map(mapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(mapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto update(UserUpdateDto userDto) {
        User user = mapper.mapUpdateDtoToUser(userDto);
        userRepository.getById(userDto.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userDto.getId() + " не найден"));

        User updatedUser = userRepository.update(user);

        return mapper.mapToUserDto(updatedUser);
    }

    @Override
    public boolean delete(Long userId) {
        return userRepository.delete(userId);
    }
}
