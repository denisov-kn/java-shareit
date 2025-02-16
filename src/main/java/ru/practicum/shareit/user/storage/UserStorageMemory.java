package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserStorageMemory implements UserStorage {

    private final Map<Long, User> users = new HashMap<Long, User>();

    @Override
    public User getUser(Long userId) {
        checkUser(userId);
        return users.get(userId);
    }

    @Override
    public User createUser(User user) {
        Long userId = getId();
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        User updateUser = getUser(user.getId());
        return UserMapper.updateUserFields(user, updateUser);
    }

    @Override
    public void deleteUser(Long userId) {
        checkUser(userId);
        users.remove(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void checkUser(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с ID - " + userId + " не найден");
        }
    }

    private long getId() {
        long lastId = users.keySet().stream()
                .max(Long::compare)
                .orElse(0L);
        return lastId + 1;
    }

}
