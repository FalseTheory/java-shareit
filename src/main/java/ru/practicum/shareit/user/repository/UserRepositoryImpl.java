package ru.practicum.shareit.user.repository;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository{

    private final Map<Long, User> users;
    private final Set<String> usersEmails;

    private long idCount;

    public UserRepositoryImpl() {
        users = new HashMap<>();
        usersEmails = new HashSet<>();
        idCount = 0;
    }

    @Override
    public User create(User user) {
        user.setId(++idCount);
        if(!usersEmails.add(user.getEmail())) {
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
        if(user.getEmail()!=null && !user.getEmail().equals(updatedUser.getEmail())){
            if(!usersEmails.add(user.getEmail())) {
                throw new ConflictException("Данный email - " + user.getEmail() + " уже занят");
            }
            usersEmails.remove(updatedUser.getEmail());
            updatedUser.setEmail(user.getEmail());
        }
        if(user.getName()!=null) {
            updatedUser.setName(user.getName());
        }
        return updatedUser;
    }

    @Override
    public boolean delete(Long userId) {

        return users.remove(userId)!=null;
    }
}
