package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validators.OnCreate;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody @Validated(OnCreate.class) UserDto user) {
        return userService.create(user);
    }
    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.get(userId);
    }
    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @RequestBody @Valid UserDto user) {
        user.setId(userId);
        return userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public boolean delete(@PathVariable Long userId) {
        return userService.delete(userId);
    }
}
