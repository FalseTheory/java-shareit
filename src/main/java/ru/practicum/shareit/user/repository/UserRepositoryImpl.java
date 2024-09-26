package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usersEmails = new HashSet<>();
    private long idCount = 0;

    @Override
    public User create(User user) {
        user.setId(++idCount);
        if (!usersEmails.add(user.getEmail())) {
            throw new ConflictException("Данный email - " + user.getEmail() + " уже занят");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public User update(User user) {
        User updatedUser = users.get(user.getId());
        if (user.getEmail() != null && !user.getEmail().equals(updatedUser.getEmail())) {
            if (!usersEmails.add(user.getEmail())) {
                throw new ConflictException("Данный email - " + user.getEmail() + " уже занят");
            }
            usersEmails.remove(updatedUser.getEmail());
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        return updatedUser;
    }

    @Override
    public boolean delete(Long userId) {

        return users.remove(userId) != null;
    }
}
