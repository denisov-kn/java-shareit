package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;

import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.SameEmailException;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@DisplayName("Контроллер User")
class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Получить пользователя")
    void getUser() throws Exception {

        Long userId = 1L;
        UserDto userDto = TestData.getTestUserDto(userId);

        when(userService.getUser(userId)).thenReturn(userDto);
        mockMvc.perform(
                        get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
        verify(userService, times(1)).getUser(userId);
    }

    @Test
    @DisplayName("Не должен получать пользователя если он не существует")
    void shouldNotGetUser() throws Exception {

        Long userId = 99L;
        when(userService.getUser(userId)).thenThrow(NotFoundException.class);
        mockMvc.perform(
                        get("/users/{userId}", userId))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).getUser(userId);
    }

    @Test
    @DisplayName("Создать пользователя")
    void createUser() throws Exception {

        Long userId = 1L;
        UserDto userDto = TestData.getTestUserDto(userId);

        NewUserRequest newUserRequest = new NewUserRequest();
        newUserRequest.setName("test");
        newUserRequest.setEmail("test@test.com");
        when(userService.createUser(newUserRequest)).thenReturn(userDto);
        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
        verify(userService, times(1)).createUser(newUserRequest);
    }

    @Test
    @DisplayName("Не создавать пользователя если такой email уже есть ")
    void shouldNotCreateUserWithSameEmail() throws Exception {

        NewUserRequest newUserRequest = new NewUserRequest();
        newUserRequest.setName("test");
        newUserRequest.setEmail("test@test.com");
        when(userService.createUser(newUserRequest)).thenThrow(SameEmailException.class);
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newUserRequest)))
                .andExpect(status().isConflict());
        verify(userService, times(1)).createUser(newUserRequest);
    }

    @Test
    @DisplayName("Обновить пользователя")
    void updateUser() throws Exception {
        Long userId = 1L;
        UserDto userDto = TestData.getTestUserDto(userId);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("test");
        updateUserRequest.setEmail("test@test.com");
        when(userService.updateUser(userId, updateUserRequest)).thenReturn(userDto);
        mockMvc.perform(
                patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
        verify(userService, times(1)).updateUser(userId, updateUserRequest);
    }

    @Test
    @DisplayName("Удалять пользователя")
    void deleteUser() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUser(userId);
    }

}