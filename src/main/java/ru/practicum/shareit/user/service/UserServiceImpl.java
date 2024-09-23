package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);

        user = userRepository.create(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto get(Long userId) {
        return userRepository.getById(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(()->new NotFoundException("Пользователь с ID = " + userId + " не найден"));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        userRepository.getById(userDto.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userDto.getId() + " не найден"));

        User updatedUser;
        updatedUser = userRepository.update(user);

        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public boolean delete(Long userId) {
        return userRepository.delete(userId);
    }
}
