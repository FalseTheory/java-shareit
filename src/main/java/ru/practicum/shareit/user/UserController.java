package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.get(userId);
    }
    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public boolean delete(@PathVariable Long userId) {
        return userService.delete(userId);
    }
}
