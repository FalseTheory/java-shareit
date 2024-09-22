package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User get(Long userId);

    List<User> getAll();

    User update(User user);

    boolean delete(Long userId);
}
