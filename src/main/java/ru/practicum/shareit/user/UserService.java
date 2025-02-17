package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.SameEmailException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto getUser(Long userId) {
        User user = userStorage.getUser(userId);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto createUser(NewUserRequest request) {

        checkEmail(request.getEmail());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return UserMapper.mapToUserDto(userStorage.createUser(user));
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request) {

        checkEmail(request.getEmail());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setId(userId);
        return UserMapper.mapToUserDto(userStorage.updateUser(user));
    }

    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }

    void checkEmail(String email) {
        Collection<User> users = userStorage.getAllUsers();
        users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .ifPresent(user -> {
                    throw new SameEmailException("Уже есть пользователь с таким email " + email);
                });
    }
}
