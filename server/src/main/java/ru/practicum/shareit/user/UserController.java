package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody UserCreateDto userCreateDto) {
        log.info("creating user - {}", userCreateDto);
        UserDto userDto = userService.create(userCreateDto);
        log.info("user creating successful");
        return userDto;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("getting user with id - {}", userId);
        return userService.get(userId);
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("getting all users");
        return userService.getAll();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @RequestBody UserUpdateDto userUpdateDto) {
        userUpdateDto.setId(userId);
        log.info("trying to update user - {}", userUpdateDto);
        UserDto userDto = userService.update(userUpdateDto);
        log.info("update successful");
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("deleting user with id - {}", userId);
        userService.delete(userId);
    }
}
