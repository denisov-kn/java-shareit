package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    User getUser(Long userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);

    Collection<User> getAllUsers();

    void checkUser(Long userId);
}
