package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User updateUserFields(User newUser, User updateUser) {
        if (newUser.getEmail() != null) {
            updateUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            updateUser.setName(newUser.getName());
        }
        return updateUser;
    }
}
