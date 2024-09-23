package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private final Map<Long, User> users;
    private final Set<String> usersEmails;

    public UserRepositoryImpl() {
        users = new HashMap<>();
        usersEmails = new HashSet<>();
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean delete(Long userId) {
        return false;
    }
}
