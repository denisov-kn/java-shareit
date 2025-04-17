package ru.practicum.shareit.user;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@DisplayName("Сервис User")
public class UserServiceTest {
    private final UserService userService;
    private final EntityManager em;


    private UserDto getUserDtoFromDb(Long userId) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        query.setParameter("id", userId);
        User user = query.getSingleResult();
        return UserMapper.mapToUserDto(user);
    }


    @Test
    @DisplayName("Получить пользователя  - getUser")
    void getUser() {
        Long userId = TestData.getTestUserDto(1L).getId();
        UserDto userDto = userService.getUser(userId);
        assertEquals(TestData.getTestUserDto(userId), userDto);
    }

    @Test
    @DisplayName("Создать пользователя  - createUser")
    void createUser() {
        NewUserRequest newUserRequest = new NewUserRequest();
        newUserRequest.setName("User7");
        newUserRequest.setEmail("7@mail.ru");
        UserDto userDto = userService.createUser(newUserRequest);

        UserDto userDtoFromDB = getUserDtoFromDb(userDto.getId());

        assertEquals(userDto, userDtoFromDB);
    }

    @Test
    @DisplayName("Обновить пользователя  - updateUser")
    void updateUser() {

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("User10");
        updateUserRequest.setEmail("10@mail.ru");

        UserDto userDto = userService.updateUser(2L, updateUserRequest);

        UserDto userDtoFromDB = getUserDtoFromDb(userDto.getId());

        assertEquals(userDto, userDtoFromDB, "");

    }

    @Test
    @DisplayName("Удалить пользователя  - deleteUser")
    void deleteUser() {
        userService.deleteUser(3L);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        query.setParameter("id", 3L);
        List<User> users = query.getResultList();

        assertTrue(users.isEmpty(),"Удаленный пользователь не должен находится");
    }
}
