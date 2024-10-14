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

        user = userRepository.save(user);

        return mapper.mapToUserDto(user);
    }

    @Override
    public UserDto get(Long userId) {
        return userRepository.findById(userId)
                .map(mapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userId + " не найден"));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(mapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto update(UserUpdateDto userDto) {
        User updatedUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с ID = " + userDto.getId() + " не найден"));


        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            updatedUser.setEmail(userDto.getEmail());
        }


        return mapper.mapToUserDto(userRepository.save(updatedUser));
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
