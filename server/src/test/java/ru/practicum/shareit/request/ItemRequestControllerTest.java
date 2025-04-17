package ru.practicum.shareit.request;

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

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestItemsDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@DisplayName("Контроллер ItemRequest")
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Создать запрос")
    void create() throws Exception {

        Long userId = 1L;
        Long itemRequestId = 1L;

        NewItemRequestDto newItemRequestDto = new NewItemRequestDto();
        newItemRequestDto.setDescription("Описание запроса");

        ItemRequestDto itemRequestDto = TestData.getItemRequestDto(itemRequestId);

        when(itemRequestService.create(newItemRequestDto, userId)).thenReturn(itemRequestDto);

        mockMvc.perform(
                post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDto)));
        verify(itemRequestService, times(1)).create(newItemRequestDto, userId);

    }

    @Test
    @DisplayName("Найти все запросы для пользователя")
    void getAllForUser() throws Exception {
        Long userId = 1L;
        List<ItemRequestDto> itemRequestDtoList = TestData.getALLItemRequestDto();

        when(itemRequestService.getAllForUser(userId)).thenReturn(itemRequestDtoList);

        mockMvc.perform(
                get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDtoList)));
        verify(itemRequestService, times(1)).getAllForUser(userId);

    }

    @Test
    @DisplayName("Найти все запросы")
    void getAll() throws Exception {
        List<ItemRequestDto> itemRequestDtoList = TestData.getALLItemRequestDto();

        when(itemRequestService.getAll()).thenReturn(itemRequestDtoList);
        mockMvc.perform(
                get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDtoList)));
        verify(itemRequestService, times(1)).getAll();
    }

    @Test
    @DisplayName("Найти запрос")
    void getRequest() throws Exception {
        Long requestId = 1L;
        ItemRequestItemsDto itemRequestDto = TestData.getItemRequestItemsDto(requestId);
        when(itemRequestService.getRequest(requestId)).thenReturn(itemRequestDto);
        mockMvc.perform(
                get("/requests/{requestId}", requestId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemRequestDto)));
        verify(itemRequestService, times(1)).getRequest(requestId);
    }
}